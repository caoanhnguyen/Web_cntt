package com.kma.services.Impl;

import com.kma.constants.fileDirection;
import com.kma.converter.nhanVienDTOConverter;
import com.kma.enums.UserType;
import com.kma.models.nhanVienDTO;
import com.kma.models.nhanVienRequestDTO;
import com.kma.models.nhanVienResponseDTO;
import com.kma.models.paginationResponseDTO;
import com.kma.repository.entities.MonHoc;
import com.kma.repository.entities.NhanVien;
import com.kma.repository.entities.Role;
import com.kma.repository.entities.User;
import com.kma.repository.monHocRepo;
import com.kma.repository.nhanVienRepo;
import com.kma.repository.roleRepo;
import com.kma.repository.userRepo;
import com.kma.services.fileService;
import com.kma.services.nhanVienService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service("nvServ")
@Transactional
public class nhanVienServImpl implements nhanVienService{
	@Autowired
	nhanVienRepo nvRepo;
	@Autowired
	nhanVienDTOConverter nvDTOConverter;
	@Autowired
	fileService fileServ;
	@Autowired
	userRepo userrepo;
	@Autowired
	roleRepo rolerepo;
	@Autowired
	monHocRepo mhRepo;

	@Override
	public nhanVienDTO getById(Integer idUser) {
		NhanVien nv = nvRepo.findById(idUser).orElse(null);
		if(nv!=null)
			return nvDTOConverter.convertToNhanVienDTO(nv);
		throw new EntityNotFoundException("Employee not found with id: " + idUser);
	}

	@Override
	public paginationResponseDTO<nhanVienDTO> getAllNhanVien(Map<String, Object> params, int page, int size) {
		// Tạo Pageable
		Pageable pageable = PageRequest.of(page, size);

		// Lấy dữ liệu từ repository
		Page<NhanVien> nvPage = fetchNhanViens(params, pageable);

		// Chuyển đổi sang DTO
		List<nhanVienDTO> nvDTOList = nvPage.getContent().stream()
				.map(nvDTOConverter::convertToNhanVienDTO)
				.toList();


		// Đóng gói dữ liệu và meta vào DTO
		return new paginationResponseDTO<>(
				nvDTOList,
				nvPage.getTotalPages(),
				(int) nvPage.getTotalElements(),
				nvPage.isFirst(),
				nvPage.isLast(),
				nvPage.getNumber(),
				nvPage.getSize()
		);
	}

	@Override
	public paginationResponseDTO<nhanVienResponseDTO> getAllNhanVienSummary(Map<String, Object> params, int page, int size) {
		// Tạo Pageable
		Pageable pageable = PageRequest.of(page, size);

		// Lấy dữ liệu từ repository
		Page<NhanVien> nvPage = fetchNhanViens(params, pageable);

		// Chuyển đổi sang DTO
		List<nhanVienResponseDTO> nvDTOList = nvPage.getContent().stream()
				.map(nvDTOConverter::convertToNVResDTO)
				.toList();


		// Đóng gói dữ liệu và meta vào DTO
		return new paginationResponseDTO<>(
				nvDTOList,
				nvPage.getTotalPages(),
				(int) nvPage.getTotalElements(),
				nvPage.isFirst(),
				nvPage.isLast(),
				nvPage.getNumber(),
				nvPage.getSize()
		);
	}


	private Page<NhanVien> fetchNhanViens(Map<String, Object> params, Pageable pageable){
		// Lấy giá trị từ params
		String tenNhanVien = ( params.get("tenNhanVien") != null ? (String) params.get("tenNhanVien") : "");
		String tenMonHoc = ( params.get("tenMonHoc") != null ? (String) params.get("tenMonHoc") : "");
		String tenPhongBan = ( params.get("tenPhongBan") != null ? (String) params.get("tenPhongBan") : "");

		// Lấy dữ liệu từ repository
		return nvRepo.findByAllCondition(tenNhanVien, tenMonHoc, tenPhongBan, pageable);
	}

	@Override
	public void addNhanVien(MultipartFile file, nhanVienRequestDTO nvReqDTO, String userName) throws IOException {
		// Kiểm tra mã nv đã tồn tại
		if (nvRepo.existsByMaNhanVien(nvReqDTO.getMaNhanVien())) {
			throw new IllegalArgumentException("Mã nhân viên: " + nvReqDTO.getMaNhanVien() + " đã tồn tại, vui lòng kiểm tra lại!");
		}

		// Lưu avaFile, lấy avaFileCode
		String avaFileCode = "";
		if(file!=null){
			String fileDirec = fileDirection.pathForProfile_NV + "/" + nvReqDTO.getMaPhongBan() + "/" + nvReqDTO.getMaNhanVien();
			avaFileCode = fileServ.uploadFile(file, fileDirec);
		}

		User user = createUserForNV(userName);

		// Tạo nhân viên để lưu
		NhanVien nv = new NhanVien();
		nvDTOConverter.convertNVReqToNV(nvReqDTO, nv, avaFileCode);
		nv.setUser(user);

		nvRepo.save(nv);
		if(avaFileCode!=null){
			System.out.println("hihi");
		}
	}

	private User createUserForNV(String userName){
		// Mã hóa mật khẩu bằng BCrypt
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(userName);  // Mật khẩu nhân viên sẽ là userName của họ

		// Tạo user mới với username là mã nhân viên và mật khẩu đã mã hóa
		User user = new User();
		user.setUserName(userName);  // Tên đăng nhập là mã nhân viên
		user.setPassword(encodedPassword);  // Mật khẩu đã mã hóa
		user.setLocked(false);  // Set tài khoản ở trạng thái hoạt động (active)
		user.setUserType(UserType.NHANVIEN);

		// Tạo và gán role EMPLOYEE cho user
		Role employeeRole = rolerepo.findByRoleName("EMPLOYEE");
		user.setRoleList(Collections.singletonList(employeeRole));  // Gán role STUDENT cho tài khoản này

		// Lưu tài khoản người dùng vào cơ sở dữ liệu
		userrepo.save(user);
		return user;
	}

	@Transactional
	@Override
	public void updateNhanVien(Integer idUser, nhanVienRequestDTO nvReqDTO, MultipartFile file) throws IOException {

		// Kiểm tra nhân viên có tồn tại
		NhanVien nv = nvRepo.findById(idUser).orElse(null);
		if (nv == null) {
			throw new EntityNotFoundException("Employee not found with id: " + idUser);
		}

		// xử lí file ava
		String avaFileCode = nv.getAvaFileCode();
		if(file!=null){
			if(!avaFileCode.isEmpty()) {
				// Xử lí xóa bỏ file cũ
				fileServ.deleteFile(idUser, 4);
			}

			// Xử lí lưu avaFile mới
			String fileDirec = fileDirection.pathForProfile_NV + "/" + nvReqDTO.getMaPhongBan() + "/" + nvReqDTO.getMaNhanVien();
			avaFileCode = fileServ.uploadFile(file, fileDirec);
		}

		// Cập nhật các trường từ DTO
		nvDTOConverter.convertNVReqToNV(nvReqDTO, nv, avaFileCode);

		// Lưu lại vào cơ sở dữ liệu
		nvRepo.save(nv);
	}

	@Override
	public void updateMGDC(Integer idUser, Integer idMGDC) {
		NhanVien existedNV = nvRepo.findById(idUser).orElse(null);
		MonHoc mh = mhRepo.findById(idMGDC).orElse(null);
		if(existedNV != null) {
			if(mh!=null){
				existedNV.setIdMonGiangDayChinh(idMGDC);
				existedNV.getMonHocList().add(mh);
				mh.getNvList().add(existedNV);

				nvRepo.save(existedNV);
				mhRepo.save(mh);
			}else{
				throw new EntityNotFoundException("Subject not found with id: " + idUser);
			}

		}else {
			throw new EntityNotFoundException("Employee not found with id: " + idUser);
		}
	}

	@Override
	@Transactional
	public void updateMonHocLienQuan(Integer idUser, List<Integer> idMonHocList) {
		NhanVien existedNV = nvRepo.findById(idUser).orElse(null);
		if(existedNV != null) {
			List<MonHoc> mhList = idMonHocList.stream()
					.map(i -> mhRepo.findById(i).orElse(null))  // Trả về null nếu không tìm thấy
					.filter(Objects::nonNull)  // Loại bỏ các giá trị null
					.collect(Collectors.toList());

			existedNV.setMonHocList(mhList);

			// Cập nhật danh sách nhân viên cho môn học
			for (MonHoc monHoc : mhList) {
				// Thêm nhân viên vào danh sách nhân viên của môn học
				if (!monHoc.getNvList().contains(existedNV)) {
					monHoc.getNvList().add(existedNV);
				}
			}
			nvRepo.save(existedNV);// Lưu nhân viên
			mhRepo.saveAll(mhList);  // Lưu các môn học (nếu có thay đổi)
		}else {
			throw new EntityNotFoundException("Employee not found with id: " + idUser);
		}
	}

	@Override
	public void deleteNhanVien(Integer idUser) {
		NhanVien existedNV = nvRepo.findById(idUser).orElse(null);
		if(existedNV != null) {
			if(!existedNV.getAvaFileCode().isEmpty() & existedNV.getPhongBan() != null) {
				// Xử lí xóa bỏ profile
				fileServ.deleteFile(idUser, 4);
			}
			nvRepo.delete(existedNV);
		}else {
			throw new EntityNotFoundException("Employee not found with id: " + idUser);
		}
	}

	@Override
	public boolean isOwner(Integer idUser, Integer nvId) {
		return (Objects.equals(idUser, nvId));
	}
}

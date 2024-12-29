package com.kma.services.Impl;

import com.kma.constants.fileDirection;
import com.kma.converter.sinhVienDTOConverter;
import com.kma.converter.suKienDTOConverter;
import com.kma.enums.UserType;
import com.kma.models.paginationResponseDTO;
import com.kma.models.sinhVienDTO;
import com.kma.models.sinhVienResponseDTO;
import com.kma.models.suKienResponseDTO;
import com.kma.repository.entities.DangKySuKien;
import com.kma.repository.entities.Role;
import com.kma.repository.entities.SinhVien;
import com.kma.repository.entities.User;
import com.kma.repository.roleRepo;
import com.kma.repository.sinhVienRepo;
import com.kma.repository.userRepo;
import com.kma.services.fileService;
import com.kma.services.sinhVienService;
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
import java.util.*;

@Service("svServ")
@Transactional
public class sinhVienServImpl implements sinhVienService {
    @Autowired
    sinhVienRepo svRepo;
    @Autowired
    sinhVienDTOConverter svDTOConverter;
    @Autowired
    fileService fileServ;
    @Autowired
    suKienDTOConverter skDTOConverter;
    @Autowired
    roleRepo rolerepo;
    @Autowired
    userRepo userrepo;


    @Override
    public paginationResponseDTO<sinhVienResponseDTO> getAllSinhVien(Map<String, Object> params, Integer page, Integer size) {
        // Tạo Pageable
        Pageable pageable = PageRequest.of(page, size);

        // Lấy dữ liệu từ repository
        Page<SinhVien> svPage = fetchSinhViens(params, pageable);

        // Chuyển đổi sang DTO
        List<sinhVienResponseDTO> svResDTOList = svPage.getContent().stream()
                .map(svDTOConverter::convertToSVResDTO)
                .toList();

        // Đóng gói dữ liệu và meta vào DTO
        return new paginationResponseDTO<>(
                svResDTOList,
                svPage.getTotalPages(),
                (int) svPage.getTotalElements(),
                svPage.isFirst(),
                svPage.isLast(),
                svPage.getNumber(),
                svPage.getSize()
        );
    }

    @Override
    public List<suKienResponseDTO> getAllParicipatedEvent(String maSinhVien) {
        // Kiểm tra sinh viên có tồn tại không
        SinhVien sv = svRepo.findById(maSinhVien).orElse(null);

        if(sv!=null){
            List<DangKySuKien> dkskList = sv.getDkskList();
            return dkskList.stream()
                           .sorted(Comparator.comparing(i->i.getEvent().getCreateAt()))
                           .map(i->(skDTOConverter.convertToSKResDTO(i.getEvent())))
                           .toList().reversed();
        }else{
            throw new EntityNotFoundException("Student not found with id: " + maSinhVien);
        }
    }

    private Page<SinhVien> fetchSinhViens(Map<String, Object> params, Pageable pageable){
        // Lấy giá trị từ params
        String maSinhVien = (params.get("maSinhVien") != null ? (String) params.get("maSinhVien") : "");
        String tenSinhVien = (params.get("tenSinhVien") != null ? (String) params.get("tenSinhVien") : "");
        String tenLop = (params.get("tenLop") != null ? (String) params.get("tenLop") : "");

        return svRepo.findByAllCondition(tenSinhVien, maSinhVien, tenLop, pageable);
    }

    @Override
    public sinhVienDTO findById(String maSinhVien) {
        SinhVien sv = svRepo.findById(maSinhVien).orElse(null);
        if(sv!=null)
            return svDTOConverter.convertToSVDTO(sv);
        throw new EntityNotFoundException("Student not found with id: " + maSinhVien);
    }

    @Override
    public void addSinhVien(MultipartFile file, sinhVienDTO svDTO) throws IOException {
        // Kiểm tra xem mã sinh viên tồn tại
        if(svRepo.existsById(svDTO.getMaSinhVien())){
            throw new IllegalArgumentException("Mã sinh viên: " +svDTO.getMaSinhVien() + " đã tồn tại, vui lòng kiểm tra lại!");
        }

        // Lưu avaFile, lấy avaFileCode
        String avaFileCode = "";
        if(file!=null){
            String fileDirec = fileDirection.pathForProfile_SV + "/" + svDTO.getTenLop() + "/" + svDTO.getMaSinhVien();
            avaFileCode = fileServ.uploadFile(file, fileDirec);
        }

        // Tạo sinh viên mới
        SinhVien sv = new SinhVien();
        svDTOConverter.convertToSV(svDTO, sv, avaFileCode);

        // Tạo tài khoản cho sinh viên
        User user = createUserForSV(sv.getMaSinhVien());
        sv.setUser(user);

        svRepo.save(sv);

    }

    private User createUserForSV(String maSinhVien){
        // Mã hóa mật khẩu bằng BCrypt
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(maSinhVien);  // Mật khẩu sinh viên sẽ là mã SV của họ

        // Tạo user mới với username là mã sinh viên và mật khẩu đã mã hóa
        User user = new User();
        user.setUserName(maSinhVien);  // Tên đăng nhập là mã sinh viên
        user.setPassword(encodedPassword);  // Mật khẩu đã mã hóa
        user.setLocked(false);  // Set tài khoản ở trạng thái hoạt động (active)
        user.setUserType(UserType.SINHVIEN);

        // Tạo và gán role STUDENT cho user
        Role studentRole = rolerepo.findByRoleName("STUDENT");
        user.setRoleList(Collections.singletonList(studentRole));  // Gán role STUDENT cho tài khoản này

        // Lưu tài khoản người dùng vào cơ sở dữ liệu
        userrepo.save(user);
        return user;
    }

    @Transactional
    @Override
    public void updateSinhVien(String maSinhVien, sinhVienDTO svDTO, MultipartFile file) throws IOException {
        // Kiểm tra xem sinh viên tồn tại
        SinhVien sv = svRepo.findById(maSinhVien).orElse(null);
        if(sv==null){
            throw new EntityNotFoundException("Student with id: " + svDTO.getMaSinhVien() + "not found!");
        }

        // Xử lí ava
        String avaFileCode = sv.getAvaFileCode();
        if(file!=null){
            if(!avaFileCode.isEmpty()) {
                // Xử lí xóa bỏ file cũ
                fileServ.deleteFile(maSinhVien, 3);
            }
            // Xử lí lưu avaFile mới
            String fileDirec = fileDirection.pathForProfile_SV + "/" + svDTO.getTenLop() + "/" + svDTO.getMaSinhVien();
            avaFileCode = fileServ.uploadFile(file, fileDirec);
        }

        // Update
        svDTOConverter.convertToSV(svDTO, sv, avaFileCode);
        svRepo.save(sv);
    }

    @Override
    public void deleteSinhVien(String maSinhVien) {
        SinhVien sv = svRepo.findById(maSinhVien).orElse(null);
        if(sv != null){
            if(!sv.getAvaFileCode().isEmpty() & sv.getLop() != null){
                // Xử lí xóa bỏ profile
                fileServ.deleteFile(maSinhVien, 3);
            }
            svRepo.delete(sv);
        }else{
            throw new EntityNotFoundException("Student not found with id: " + maSinhVien);
        }
    }

    @Override
    public boolean isOwner(String maSinhVien, String maSV) {
        return (Objects.equals(maSinhVien, maSV));
    }
}

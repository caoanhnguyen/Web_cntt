package com.kma.services.Impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.kma.constants.fileDirection;
import com.kma.repository.entities.*;
import com.kma.repository.nhanVienRepo;
import com.kma.repository.sinhVienRepo;
import com.kma.repository.taiLieuMonHocRepo;
import com.kma.repository.taiNguyenRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.kma.models.fileDTO;
import com.kma.services.fileService;
import com.kma.utilities.fileUploadUtil;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class fileServImpl implements fileService{
	
	@Autowired
	taiNguyenRepo tnRepo;
	@Autowired
	taiLieuMonHocRepo tlmhRepo;
	@Autowired
	sinhVienRepo svRepo;
	@Autowired
	nhanVienRepo nvRepo;

	@Override
	public void deleteImg(Integer imgId) {
		if(tnRepo.existsById(imgId)){
			deleteFile(imgId, 1);
			tnRepo.deleteById(imgId);
		}
	}

	@Override
	public String uploadFile(MultipartFile multipartFile, String fileDirec) throws IOException {
		// TODO Auto-generated method stub
		String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));

        return fileUploadUtil.saveFile(fileName, multipartFile, fileDirec);
	}

	@Override
	public fileDTO uploadImg(MultipartFile file) throws IOException {	// Dùng để lưu và trả ra downloadUrl của ảnh cho bài viết, sự kiện và discussion trong content
		String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
		String fileCode = fileUploadUtil.saveFile(fileName, file, fileDirection.pathForTaiNguyen);
		String url = "/downloadFile/" + fileCode;

		// Tạo tài nguyên để lưu
		TaiNguyen tn = new TaiNguyen();
		tn.setFileCode(fileCode);
		tn.setCreateAt(new Date(System.currentTimeMillis()));
		tnRepo.save(tn);

		// Tạo DTO
		fileDTO dto = new fileDTO();
		dto.setId(tn.getResourceId());
		dto.setDownloadUrl(url);

		return dto;
	}

	@Override
	public List<fileDTO> getListFileDTO(List<TaiNguyen> tnList) {
		// TODO Auto-generated method stub
		List<fileDTO> fileDTO = new ArrayList<>();
		
		for(TaiNguyen tn: tnList) {
			Integer id = tn.getResourceId();
			String downloadUrl = "/downloadFile/" + tn.getFileCode();
			
			fileDTO fdto = new fileDTO();
			fdto.setId(id);
			fdto.setDownloadUrl(downloadUrl);
			
			fileDTO.add(fdto);
		}
		return fileDTO;
	}

	@Override
	public List<fileDTO> getListFileDTOByTL(List<TaiLieuMonHoc> taiLieuMonHocList) {
		// TODO Auto-generated method stub
		List<fileDTO> fileDTO = new ArrayList<>();

		for(TaiLieuMonHoc tn: taiLieuMonHocList) {
			Integer id = tn.getDocId();
			String downloadUrl = "/downloadDocs/" + tn.getFileCode();

			fileDTO fdto = new fileDTO();
			fdto.setId(id);
			fdto.setDownloadUrl(downloadUrl);

			fileDTO.add(fdto);
		}
		return fileDTO;
	}

	@Override
	public void deleteFile(Object fileId, Integer fType) {
		// Lấy entity và xác định đường dẫn
		Object entity;
		String directoryPath = switch (fType) {
            case 1 -> {
                entity = tnRepo.findById((Integer) fileId).orElse(null);
                yield fileDirection.pathForTaiNguyen; // Loại tài nguyên
            }
            case 2 -> {
                entity = tlmhRepo.findById((Integer) fileId).orElse(null);
                yield fileDirection.pathForTaiLieuMonHoc; // Loại tài liệu môn học
            }
			case 3 -> {
				entity = svRepo.findById((String) fileId).orElse(null);
				yield fileDirection.pathForProfile_SV; // Loại profile SV
			}
			case 4 -> {
				entity = nvRepo.findById((Integer)fileId).orElse(null);
				yield fileDirection.pathForProfile_NV; // Loại profile NV
			}
            default -> throw new IllegalArgumentException("Invalid file type!");
        };

        // Kiểm tra nếu không tìm thấy entity
		if (entity == null) {
			throw new EntityNotFoundException("File not found!");
		}

		// Lấy fileCode từ entity
		String fileCode;
        switch (entity) {
            case TaiNguyen taiNguyen -> fileCode = taiNguyen.getFileCode();

            case TaiLieuMonHoc taiLieuMonHoc -> fileCode = taiLieuMonHoc.getFileCode();

            case SinhVien sinhVien -> {
                fileCode = sinhVien.getAvaFileCode();
                String tenLop = sinhVien.getLop().getTenLop();
                directoryPath += "/" + tenLop + "/" + sinhVien.getMaSinhVien();
            }
            case NhanVien nhanVien -> {
                fileCode = nhanVien.getAvaFileCode();
				String maPhongBan = nhanVien.getPhongBan().getMaPhongBan();
                directoryPath += "/" + maPhongBan + "/" +nhanVien.getMaNhanVien();
            }
            default -> throw new IllegalStateException("Unexpected entity type!");
        }

		if(fileCode!=null){
			deleteFromDisk(fileCode, directoryPath);
		}
	}

	private void deleteFromDisk(String fileCode, String fileDirec) {
		try {
			Path dirPath = Paths.get(fileDirec);

			// Kiểm tra xem thư mục có tồn tại và là thư mục không
			if (Files.exists(dirPath) && Files.isDirectory(dirPath)) {
				// Duyệt qua tất cả các file trong thư mục
				Files.list(dirPath).forEach(file -> {
					if (file.getFileName().toString().startsWith(fileCode)) {
						try {
							// Xóa tệp nếu tồn tại
							Files.deleteIfExists(file);
						} catch (IOException e) {
							throw new RuntimeException("Failed to delete file: " + file.getFileName(), e);
						}
					}
				});

				// Kiểm tra nếu thư mục trống trước khi xóa
				try {
					if (Files.list(dirPath).findAny().isEmpty()) {
						Files.deleteIfExists(dirPath); // Xóa thư mục nếu trống
					} else {
						System.out.println("Directory is not empty, skipping deletion: " + dirPath);
					}
				} catch (IOException e) {
					throw new RuntimeException("Failed to check or delete directory: " + dirPath, e);
				}
			}

		} catch (IOException e) {
			throw new RuntimeException("Error accessing files directory", e);
		}
	}




}

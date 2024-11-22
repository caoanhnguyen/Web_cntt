package com.kma.services.Impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.kma.constants.fileDirection;
import com.kma.repository.entities.TaiLieuMonHoc;
import com.kma.repository.taiLieuMonHocRepo;
import com.kma.repository.taiNguyenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.kma.models.fileDTO;
import com.kma.repository.entities.TaiNguyen;
import com.kma.services.fileService;
import com.kma.utilities.fileUploadUtil;

import jakarta.persistence.EntityNotFoundException;

@Service
public class fileServImpl implements fileService{
	
	@Autowired
	taiNguyenRepo tnRepo;
	@Autowired
	taiLieuMonHocRepo tlmhRepo;

	@Override
	public String uploadFile(MultipartFile multipartFile, String fileDirec) throws IOException {
		// TODO Auto-generated method stub
		String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));

        return fileUploadUtil.saveFile(fileName, multipartFile, fileDirec);
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

//	@Override
//	public void deleteFile(Integer resources_id) {
//		// TODO Auto-generated method stub
//		TaiNguyen tn = tnRepo.findById(resources_id).orElse(null);
//		if(tn==null) {
//			throw new EntityNotFoundException("File not found!");
//		}
//		try {
//	        Path dirPath = Paths.get(fileDirection.pathForTaiNguyen);
//	        Files.list(dirPath).forEach(file -> {
//	            if (file.getFileName().toString().startsWith(tn.getFileCode())) {
//	                try {
//	                    Files.deleteIfExists(file);
//	                } catch (IOException e) {
//	                    throw new RuntimeException("Failed to delete file: " + file.getFileName(), e);
//	                }
//	            }
//	        });
//	    } catch (IOException e) {
//	        throw new RuntimeException("Error accessing files directory", e);
//	    }
//	}
//
//	@Override
//	public void deleteDoc(Integer docId) {
//		// TODO Auto-generated method stub
//		TaiLieuMonHoc tn = tlmhRepo.findById(docId).orElse(null);
//		if(tn==null) {
//			throw new EntityNotFoundException("Document not found!");
//		}
//		try {
//			Path dirPath = Paths.get(fileDirection.pathForTaiLieuMonHoc);
//			Files.list(dirPath).forEach(file -> {
//				if (file.getFileName().toString().startsWith(tn.getFileCode())) {
//					try {
//						Files.deleteIfExists(file);
//					} catch (IOException e) {
//						throw new RuntimeException("Failed to delete document: " + file.getFileName(), e);
//					}
//				}
//			});
//		} catch (IOException e) {
//			throw new RuntimeException("Error accessing docs directory", e);
//		}
//	}
	@Override
	public void deleteFile(Integer fileId, Integer fType) {
		// Lấy entity và xác định đường dẫn
		Object entity;
		String directoryPath = switch (fType) {
            case 1 -> {
                entity = tnRepo.findById(fileId).orElse(null);
                yield fileDirection.pathForTaiNguyen; // Loại tài nguyên
            }
            case 2 -> {
                entity = tlmhRepo.findById(fileId).orElse(null);
                yield fileDirection.pathForTaiLieuMonHoc; // Loại tài liệu môn học
            }
            default -> throw new IllegalArgumentException("Invalid file type!");
        };

        // Kiểm tra nếu không tìm thấy entity
		if (entity == null) {
			throw new EntityNotFoundException("File not found!");
		}

		// Lấy fileCode từ entity
		String fileCode;
		if (entity instanceof TaiNguyen) {
			fileCode = ((TaiNguyen) entity).getFileCode();
		} else if (entity instanceof TaiLieuMonHoc) {
			fileCode = ((TaiLieuMonHoc) entity).getFileCode();
		} else {
			throw new IllegalStateException("Unexpected entity type!");
		}

		deleteFromDisk(fileCode, directoryPath);
	}

	private void deleteFromDisk(String fileCode, String fileDirec){
		// Xóa file
		try {
			Path dirPath = Paths.get(fileDirec);
			Files.list(dirPath).forEach(file -> {
				if (file.getFileName().toString().startsWith(fileCode)) {
					try {
						Files.deleteIfExists(file);
					} catch (IOException e) {
						throw new RuntimeException("Failed to delete file: " + file.getFileName(), e);
					}
				}
			});
		} catch (IOException e) {
			throw new RuntimeException("Error accessing files directory", e);
		}
	}

}

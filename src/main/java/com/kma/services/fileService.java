package com.kma.services;

import java.io.IOException;
import java.util.List;

import com.kma.repository.entities.TaiLieuMonHoc;
import org.springframework.web.multipart.MultipartFile;

import com.kma.models.fileDTO;
import com.kma.repository.entities.TaiNguyen;


public interface fileService {
	String uploadFile(MultipartFile multipartFile, String fileDirec) throws IOException;
	
	List<fileDTO> getListFileDTO(List<TaiNguyen> tnList);

	List<fileDTO> getListFileDTOByTL(List<TaiLieuMonHoc> taiLieuMonHocList);
	
	void deleteFile(Integer fileId, Integer fType);
}

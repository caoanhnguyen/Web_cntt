package com.kma.services;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.kma.models.fileDTO;
import com.kma.repository.entities.TaiNguyen;


public interface fileService {
	String uploadFile(MultipartFile multipartFile) throws IOException;
	
	List<fileDTO> getListFileDTO(List<TaiNguyen> tnList);
	
	void deleteFile(Integer resources_id);
}

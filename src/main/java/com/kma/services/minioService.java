package com.kma.services;

import org.springframework.web.multipart.MultipartFile;

public interface minioService {
	
	String uploadFile(MultipartFile file);
	
	void deleteFile(String filePath);
	
}

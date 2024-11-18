package com.kma.services.Impl;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kma.services.minioService;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;

@Service
public class minioServImpl implements minioService{
	
	@Autowired
    private MinioClient minioClient;

    private final String bucketName = "resources";

	@Override
	public String uploadFile(MultipartFile file) {
		// TODO Auto-generated method stub
		try {
            String fileName = file.getOriginalFilename();
            InputStream inputStream = file.getInputStream();
            
            // Lấy timestamp hiện tại
	        String timestamp = String.valueOf(System.currentTimeMillis());
	        
	        fileName = timestamp + "_" + fileName;

            // Upload file lên MinIO
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .stream(inputStream, file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build()
            );
            String filePath = "http://localhost:9000/" + bucketName + "/" + fileName;
            return filePath;

        }catch(Exception e) {
            e.printStackTrace();
            return null;
        }
	}

	@Override
	public void deleteFile(String filePath) {
		// TODO Auto-generated method stub
		try {
	        minioClient.removeObject(RemoveObjectArgs.builder()
	                .bucket(bucketName)
	                .object(filePath)
	                .build());
//	        System.out.println("File has been deleted successfully.");
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	

}

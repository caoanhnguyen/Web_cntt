package com.kma.services.Impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.kma.models.fileDTO;
import com.kma.repository.entities.TaiNguyen;
import com.kma.services.fileService;
import com.kma.utilities.fileUploadUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;

@Service
public class fileServImpl implements fileService{
	
	@Autowired
	EntityManager entityManager;

	@Override
	public String uploadFile(MultipartFile multipartFile) throws IOException {
		// TODO Auto-generated method stub
		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        String filecode = fileUploadUtil.saveFile(fileName, multipartFile);
             
        return filecode;
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
	public void deleteFile(Integer resources_id) {
		// TODO Auto-generated method stub
		TaiNguyen tn = entityManager.find(TaiNguyen.class, resources_id);
		if(tn==null) {
			throw new EntityNotFoundException("File not found!");
		}
		try {
	        Path dirPath = Paths.get("Files-Upload");
	        Files.list(dirPath).forEach(file -> {
	            if (file.getFileName().toString().startsWith(tn.getFileCode())) {
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

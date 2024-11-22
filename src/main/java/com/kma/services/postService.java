package com.kma.services;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.kma.models.postResponseDTO;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.kma.models.postDTO;
import com.kma.models.postRequestDTO;

public interface postService {
	postDTO getById(Integer post_id);

	List<postResponseDTO> getAllPost(Map<String,Object> params);

	List<postDTO> getLatestPosts();
	
	void addPost(@RequestParam(value = "file", required = false) List<MultipartFile> files,
			@ModelAttribute postRequestDTO postRequestDTO) throws IOException;
	
	void updatePost(@PathVariable Integer post_id, 
					@ModelAttribute postRequestDTO postRequestDTO,
					@RequestParam(value = "files", required = false) List<MultipartFile> files,
		            @RequestParam(value = "deleteFiles", required = false) List<Integer> deleteFileIds) throws IOException;
	
	void deletePost(Integer post_id);
}

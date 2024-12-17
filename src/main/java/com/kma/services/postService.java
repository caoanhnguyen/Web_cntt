package com.kma.services;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

import com.kma.models.paginationResponseDTO;
import com.kma.models.postResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import com.kma.models.postDTO;
import com.kma.models.postRequestDTO;

public interface postService {
	postDTO getById(Integer post_id);

	paginationResponseDTO<postResponseDTO> getAllPost(Map<String,Object> params, Integer page, Integer size);

	List<postDTO> getLatestPosts();
	
	void addPost(List<MultipartFile> files,
				 postRequestDTO postRequestDTO, Principal principal) throws IOException;
	
	void updatePost(Integer post_id,
					postRequestDTO postRequestDTO,
					List<MultipartFile> files,
		            List<Integer> deleteFileIds) throws IOException;
	
	void deletePost(Integer post_id);
}

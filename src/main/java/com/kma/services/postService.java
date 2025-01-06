package com.kma.services;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.kma.models.*;
import org.springframework.web.multipart.MultipartFile;

public interface postService {

	paginationResponseDTO<postResponseDTO> getAllPostOfUser(Integer page, Integer size);

	postDTO getById(Integer post_id);

	paginationResponseDTO<postResponseDTO> getAllPost(Map<String,Object> params, Integer page, Integer size);

	List<postDTO> getLatestPosts();
	
	void addPost(List<MultipartFile> files, postRequestDTO postRequestDTO) throws IOException;
	
	void updatePost(Integer post_id,
					postRequestDTO postRequestDTO,
					List<MultipartFile> files,
		            List<Integer> deleteFileIds) throws IOException;
	
	void deletePost(Integer post_id);

	boolean isOwner(Integer discussionId, Integer userId);
}

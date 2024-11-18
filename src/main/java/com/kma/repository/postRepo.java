package com.kma.repository;

import java.util.List;

import com.kma.models.postRequestDTO;
import com.kma.repository.entities.Post;

public interface postRepo {
	List<Post> getAllPost(postRequestDTO prDTO);
	
	void addPost(Post post);
	
	Post findByID(Integer post_id);
}

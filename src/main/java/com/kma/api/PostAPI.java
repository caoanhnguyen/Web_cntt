package com.kma.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kma.models.errorResponseDTO;
import com.kma.models.postDTO;
import com.kma.models.postRequestDTO;
import com.kma.services.postService;

import jakarta.persistence.EntityNotFoundException;

@RestController
public class PostAPI {
	
	@Autowired
	private postService postServ;
	
	
	@GetMapping(value="/api/posts")
	public ResponseEntity<Object> getAllPost(@RequestParam Map<String,Object> params){
		try {
			List<postDTO> DTO = postServ.getAllPost(params);
			return new ResponseEntity<>(DTO, HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			errorResponseDTO errorDTO = new errorResponseDTO();
			errorDTO.setError(e.getMessage());
			List<String> details = new ArrayList<>();
			details.add("An error occurred!");
			errorDTO.setDetails(details);
			
			return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping(value = "/api/posts")
	public ResponseEntity<Object> addPost(@RequestParam(value = "file", required = false) List<MultipartFile> files,
										  @ModelAttribute postRequestDTO postRequestDTO) throws IOException {		
		try {
			postServ.addPost(files, postRequestDTO);
			return ResponseEntity.ok("Add successful!");
		} catch (IllegalArgumentException e) {
			// TODO: handle exception
			errorResponseDTO errorDTO = new errorResponseDTO();
			errorDTO.setError(e.getMessage());
			List<String> details = new ArrayList<>();
			details.add("Author not exist!");
			errorDTO.setDetails(details);
			
			return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
		}
	}
	
	@PutMapping(value = "/api/posts/{post_id}")
	public ResponseEntity<Object> updatePost(@PathVariable Integer post_id, 
											 @ModelAttribute postRequestDTO postRequestDTO,
											 @RequestParam(value = "file", required = false) List<MultipartFile> files,
		                                     @RequestParam(value = "deleteFileIds", required = false) List<Integer> deleteFileIds) {
		
		try {
			postServ.updatePost(post_id,postRequestDTO, files, deleteFileIds);
			return ResponseEntity.ok("Update successful!"); 
		} catch (EntityNotFoundException e) {
			// TODO: handle exception
			errorResponseDTO errorDTO = new errorResponseDTO();
			errorDTO.setError(e.getMessage());
			List<String> details = new ArrayList<>();
			details.add("Post not found!");
			errorDTO.setDetails(details);
			
			return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
		}
	}
	
	@DeleteMapping(value = "api/posts/{post_id}")
	public ResponseEntity<Object> deletePost(@PathVariable Integer post_id) {
		try {
			postServ.deletePost(post_id);
			return ResponseEntity.ok("Delete successful!"); 
		} catch (EntityNotFoundException e) {
			// TODO: handle exception
			errorResponseDTO errorDTO = new errorResponseDTO();
			errorDTO.setError(e.getMessage());
			List<String> details = new ArrayList<>();
			details.add("Post not found!");
			errorDTO.setDetails(details);
			
			return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
		}
	}
}

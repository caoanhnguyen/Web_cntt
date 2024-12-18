package com.kma.api;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.kma.models.*;
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

import com.kma.services.postService;

import jakarta.persistence.EntityNotFoundException;

@RestController
public class PostAPI {
	
	@Autowired
	postService postServ;

	@GetMapping(value = "/api/posts/{post_id}")
	public ResponseEntity<Object> getById(@PathVariable Integer post_id){
		try {
			postDTO DTO = postServ.getById(post_id);
			return new ResponseEntity<>(DTO, HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			// TODO: handle exception
			errorResponseDTO errorDTO = new errorResponseDTO();
			errorDTO.setError(e.getMessage());
			List<String> details = new ArrayList<>();
			details.add("Post not found!");
			errorDTO.setDetails(details);

			return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
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
	
	@GetMapping(value="/api/posts")
	public ResponseEntity<Object> getAllPost(@RequestParam Map<String,Object> params,
											 @RequestParam(required = false, defaultValue = "0") int page,
											 @RequestParam(required = false, defaultValue = "10") int size){
		try {
			paginationResponseDTO<postResponseDTO> DTO = postServ.getAllPost(params, page, size);
			// Log kiểm tra dữ liệu trước khi trả về
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

	@GetMapping(value = "/api/posts/latest")
	public ResponseEntity<Object> getLatestPosts() {
		try {
			List<postDTO> posts = postServ.getLatestPosts();
			return new ResponseEntity<>(posts, HttpStatus.OK);
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
										  @ModelAttribute postRequestDTO postRequestDTO, Principal principal) {
		try {
			postServ.addPost(files, postRequestDTO, principal);
			return ResponseEntity.ok("Add successfully!");
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
	
	@PutMapping("/api/posts/{post_id}")
	public ResponseEntity<Object> updatePost(@PathVariable Integer post_id, 
											 @ModelAttribute postRequestDTO postRequestDTO,
											 @RequestParam(value = "file", required = false) List<MultipartFile> files,
		                                     @RequestParam(value = "deleteFileIds", required = false) List<Integer> deleteFileIds) {
		
		try {
			postServ.updatePost(post_id,postRequestDTO, files, deleteFileIds);
			return ResponseEntity.ok("Update successfully!");
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
	
	@DeleteMapping(value = "/api/posts/{post_id}")
	public ResponseEntity<Object> deletePost(@PathVariable Integer post_id) {
		try {
			postServ.deletePost(post_id);
			return ResponseEntity.ok("Delete successfully!");
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

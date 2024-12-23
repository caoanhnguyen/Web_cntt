package com.kma.api;

import java.util.List;
import java.util.Map;

import com.kma.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.kma.services.postService;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api")
public class PostAPI {
	
	@Autowired
	postService postServ;
	@Autowired
	com.kma.utilities.buildErrorResUtil buildErrorResUtil;

	@GetMapping(value="/user/my_posts")
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_STUDENT')")
	public ResponseEntity<Object> getAllPostOfUser(@RequestParam(required = false, defaultValue = "0") int page,
											       @RequestParam(required = false, defaultValue = "10") int size){
		try {
			paginationResponseDTO<postResponseDTO> DTO = postServ.getAllPostOfUser(page, size);
			return new ResponseEntity<>(DTO, HttpStatus.OK);
		} catch (Exception e) {
			errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
			return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(value = "/public/posts/{post_id}")
	public ResponseEntity<Object> getById(@PathVariable Integer post_id){
		try {
			postDTO DTO = postServ.getById(post_id);
			return new ResponseEntity<>(DTO, HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "Post not found!");
			return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
			return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(value="/public/posts")
	public ResponseEntity<Object> getAllPost(@RequestParam Map<String,Object> params,
											 @RequestParam(required = false, defaultValue = "0") int page,
											 @RequestParam(required = false, defaultValue = "10") int size){
		try {
			paginationResponseDTO<postResponseDTO> DTO = postServ.getAllPost(params, page, size);
			// Log kiểm tra dữ liệu trước khi trả về
			return new ResponseEntity<>(DTO, HttpStatus.OK);
		} catch (Exception e) {
			errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
			return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(value = "/public/posts/latest")
	public ResponseEntity<Object> getLatestPosts() {
		try {
			List<postDTO> posts = postServ.getLatestPosts();
			return new ResponseEntity<>(posts, HttpStatus.OK);
		} catch (Exception e) {
			errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
			return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping(value = "/posts")
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
	public ResponseEntity<Object> addPost(@RequestParam(value = "file", required = false) List<MultipartFile> files,
										  @ModelAttribute postRequestDTO postRequestDTO) {
		try {
			postServ.addPost(files, postRequestDTO);
			return ResponseEntity.ok("Add successfully!");
		} catch (IllegalArgumentException e) {
			errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "Post not found!");
			return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
			return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/posts/{post_id}")
	@PreAuthorize("@postServ.isOwner(#post_id, principal.nhanVien.idUser) or hasRole('ADMIN')")
	public ResponseEntity<Object> updatePost(@PathVariable Integer post_id, 
											 @ModelAttribute postRequestDTO postRequestDTO,
											 @RequestParam(value = "file", required = false) List<MultipartFile> files,
		                                     @RequestParam(value = "deleteFileIds", required = false) List<Integer> deleteFileIds) {
		
		try {
			postServ.updatePost(post_id,postRequestDTO, files, deleteFileIds);
			return ResponseEntity.ok("Update successfully!");
		} catch (EntityNotFoundException e) {
			errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "Post not found!");
			return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
			return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping(value = "/posts/{post_id}")
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_STUDENT')")
	public ResponseEntity<Object> deletePost(@PathVariable Integer post_id) {
		try {
			postServ.deletePost(post_id);
			return ResponseEntity.ok("Delete successfully!");
		} catch (EntityNotFoundException e) {
			errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "Post not found!");
			return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
			return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}

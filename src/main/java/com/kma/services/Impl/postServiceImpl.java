package com.kma.services.Impl;

import java.io.IOException;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

import com.kma.converter.postDTOConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.kma.models.postDTO;
import com.kma.models.postRequestDTO;
import com.kma.repository.nhanVienRepo;
import com.kma.repository.postRepo;
import com.kma.repository.entities.NhanVien;
import com.kma.repository.entities.Post;
import com.kma.repository.entities.TaiNguyen;
import com.kma.services.fileService;
import com.kma.services.nhanVienService;
import com.kma.services.postService;
import com.kma.services.taiNguyenService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class postServiceImpl implements postService{

	@Autowired
	postRepo postRepo;
	
	@Autowired
	nhanVienRepo nvRepo;
	
	@Autowired
	nhanVienService nvServ;
	
	@Autowired
	fileService fileServ;
	
	@Autowired
	taiNguyenService taiNguyenServ;

	@Autowired
	postDTOConverter dtoConverter;

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public List<postDTO> getAllPost(Map<String,Object> params) {
		// Lấy giá trị từ params
		String title = (String) params.get("title");
		String authorName = (String) params.get("author_name");

		// Lấy danh sách id của các nhân viên từ tên
		List<Integer> idUsers = Optional.ofNullable(authorName)
				.map(nvServ::findByName)
				.orElse(Collections.emptyList())
					.stream()
					.map(NhanVien::getIdUser)
					.toList();

		// Tìm kiếm bài viết theo điều kiện
		List<Post> posts;

		if (idUsers.isEmpty()) {
			// Không có author_name, chỉ tìm theo title hoặc lấy tất cả nếu title null
			if (title == null) {
				posts = postRepo.findAll(); // Lấy toàn bộ bài viết
			} else {
				posts = postRepo.findByTitleContaining(title); // Tìm bài viết theo title
			}
		} else {
			// Có author_name, tìm theo title và idUsers
			posts = idUsers.stream()
					.flatMap(id -> {
						if (title == null) {
							return postRepo.findByNhanVien_idUser(id).stream();	// Tìm bài viết theo danh sánh nhân viên
						} else {
							return postRepo.findByTitleContainingAndNhanVien_idUser(title, id).stream(); // Tìm bài viết theo cả title và danh sách nhân viên
						}
					})
					.toList();
		}

		return posts.stream()
				.map(i->dtoConverter.convertToPostDTO(i))
				.collect(Collectors.toList());
	}

	@Override
	public void addPost(@RequestParam(value = "file", required = false) List<MultipartFile> files,
						@ModelAttribute postRequestDTO postRequestDTO) throws IOException {
		
		// Kiểm tra tính hợp lệ của author_id
		if(postRequestDTO.getAuthor_id() == null) {
			throw new IllegalArgumentException("Invalid Author ID");
		}
		// Kiểm tra xem nhân viên có tồn tại không
		NhanVien nhanVien = nvRepo.findByID(postRequestDTO.getAuthor_id());
		if(nhanVien == null) {
			throw new IllegalArgumentException("Invalid Author ID");
		}
		//Tạo post để lưu 
		Post post = new Post();
		post.setTitle(postRequestDTO.getTitle());
		post.setContent(postRequestDTO.getContent());
		post.setCreateAt(new Date(System.currentTimeMillis()));
		
		post.setNhanVien(nhanVien);

		List<TaiNguyen> tnList = post.getTaiNguyenList();
		
		// Upload file và lấy đường dẫn
		for (MultipartFile item: files) {
			
			String fileCode = fileServ.uploadFile(item);
			// Tạo tài nguyên
			TaiNguyen resources = new TaiNguyen();
			resources.setDescription(postRequestDTO.getTitle());
			resources.setCreate_at(new Date(System.currentTimeMillis()));
			resources.setFileCode(fileCode);
			
			//Them post vao list bai viet cua tai nguyen
			resources.setPost(post);
			
			//Them tai nguyen vao list tai nguyen cua post
			tnList.add(resources);
			
			taiNguyenServ.addTaiNguyen(resources);
		}

//		postRepo.addPost(post);
		postRepo.save(post);
	}
	

	@Override
	public void updatePost(Integer post_id, postRequestDTO postRequestDTO, List<MultipartFile> files,
			List<Integer> deleteFileIds) throws IOException {
		// TODO Auto-generated method stub
		Post post = entityManager.find(Post.class, post_id);
		
		if(post != null) {
			post.setTitle(postRequestDTO.getTitle());
			post.setContent(postRequestDTO.getContent());
			post.setCreateAt(new Date(System.currentTimeMillis()));
			
			List<TaiNguyen> tnList = post.getTaiNguyenList();
			
			//Xử lí file thêm mới
			if(files != null && !files.isEmpty()) {
				for(MultipartFile file: files) {
					String fileCode = fileServ.uploadFile(file);
					
					// Tạo tài nguyên
					TaiNguyen resources = new TaiNguyen();
					resources.setDescription(postRequestDTO.getTitle());
					resources.setCreate_at(new Date(System.currentTimeMillis()));
					resources.setFileCode(fileCode);
					
					//Them post vao list bai viet cua tai nguyen
					resources.setPost(post);
					
					//Them tai nguyen vao list tai nguyen cua post
					tnList.add(resources);
					
					taiNguyenServ.addTaiNguyen(resources);
				}
			}
			//Xử lí các file cần xóa
		    if (deleteFileIds != null) {
		        for (Integer fileId : deleteFileIds) {
		        	
		            TaiNguyen f = entityManager.find(TaiNguyen.class, fileId);
		            if(f != null) {
		            	
		            	// Xóa file trên server, xóa tài nguyên khỏi list tài nguyên của bài viết và xóa bản ghi tài nguyên
		            	fileServ.deleteFile(f.getResourceId());
			            tnList.remove(f);
			            entityManager.remove(f);
		            }
		        }
		    }
		    post.setTaiNguyenList(tnList);
			entityManager.merge(post);
		}else {
			throw new EntityNotFoundException("Post not found with id: " + post_id);
		}
	}

	@Override
	public void deletePost(Integer post_id) {
		// TODO Auto-generated method stub
		Post existedPost = postRepo.findById(post_id).get();
		if(existedPost != null) {
			List<TaiNguyen> tnlist = existedPost.getTaiNguyenList();
			for(TaiNguyen tn: tnlist) {
				fileServ.deleteFile(tn.getResourceId());
			}
			entityManager.remove(existedPost);
		}else {
			throw new EntityNotFoundException("Post not found with id: " + post_id);
		}
	}
	
	
	
	
}

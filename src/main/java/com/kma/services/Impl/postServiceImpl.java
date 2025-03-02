package com.kma.services.Impl;

import java.io.IOException;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

import com.kma.constants.fileDirection;
import com.kma.converter.postDTOConverter;
import com.kma.models.paginationResponseDTO;
import com.kma.models.postResponseDTO;
import com.kma.repository.entities.User;
import com.kma.repository.taiNguyenRepo;
import com.kma.utilities.taiNguyenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kma.models.postDTO;
import com.kma.models.postRequestDTO;
import com.kma.repository.postRepo;
import com.kma.repository.entities.NhanVien;
import com.kma.repository.entities.Post;
import com.kma.repository.entities.TaiNguyen;
import com.kma.services.fileService;
import com.kma.services.postService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service(value = "postServ")
@Transactional
public class postServiceImpl implements postService{
	@Autowired
	postRepo postRepo;
	@Autowired
	fileService fileServ;
	@Autowired
	taiNguyenRepo taiNguyenRepo;
	@Autowired
	postDTOConverter dtoConverter;
	@Autowired
	NotificationService notiServ;

	@Override
	public paginationResponseDTO<postResponseDTO> getAllPostOfUser(Integer page, Integer size) {

		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		// Tạo Pageable
		Pageable pageable = PageRequest.of(page, size);

		// Lấy dữ liệu từ repository
		Page<Post> postPage = postRepo.findByNhanVien_IdUserOrderByCreateAtDesc(user.getNhanVien().getIdUser(), pageable);

		// Chuyển đổi Post sang postResponseDTO
		List<postResponseDTO> postResDTOList = postPage.getContent().stream()
				.map(dtoConverter::convertToPostResDTO)
				.toList();

		// Đóng gói dữ liệu và meta vào DTO
		return new paginationResponseDTO<>(
				postResDTOList,
				postPage.getTotalPages(),
				(int) postPage.getTotalElements(),
				postPage.isFirst(),
				postPage.isLast(),
				postPage.getNumber(),
				postPage.getSize()
		);
	}

	@Override
	public postDTO getById(Integer post_id) {

		Post post = postRepo.findById(post_id).orElse(null);
		if(post!=null)
        	return dtoConverter.convertToPostDTO(post);
		throw new EntityNotFoundException("Post not found with id: " + post_id);
	}

	@Override
	public paginationResponseDTO<postResponseDTO> getAllPost(Map<String, Object> params, Integer page, Integer size) {
		// Lấy giá trị từ params
		String title = ( params.get("title") != null ? (String) params.get("title") : "");
		String authorName = ( params.get("authorName") != null ? (String) params.get("authorName") : "");

		// Tạo Pageable
		Pageable pageable = PageRequest.of(page, size);

		// Lấy dữ liệu từ repository
		Page<Post> postPage = postRepo.findByAllCondition(title, authorName, pageable);

		// Chuyển đổi Post sang postResponseDTO
		List<postResponseDTO> postResDTOList = postPage.getContent().stream()
				.map(dtoConverter::convertToPostResDTO)
				.toList();

        // Đóng gói dữ liệu và meta vào DTO
		return new paginationResponseDTO<>(
				postResDTOList,
				postPage.getTotalPages(),
				(int) postPage.getTotalElements(),
				postPage.isFirst(),
				postPage.isLast(),
				postPage.getNumber(),
				postPage.getSize()
		);
	}

	@Override
	public List<postDTO> getLatestPosts() {

		// Tìm kiếm bài viết mới nhất
		List<Post> posts = postRepo.findTop6ByOrderByCreateAtDesc();
		return posts.stream()
				.map(i->dtoConverter.convertToPostDTO(i))
				.collect(Collectors.toList());
	}

	@Override
	public void addPost(List<MultipartFile> files,
						postRequestDTO postRequestDTO) throws IOException {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		NhanVien nhanVien = user.getNhanVien();
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
		
		// Upload file và lấy đường dẫn nếu cần
		if(files!=null){
			for (MultipartFile item: files) {
				// Lưu file và lấy fileCode
				String fileCode = fileServ.uploadFile(item, fileDirection.pathForTaiNguyen);
				// Tạo tài nguyên
				TaiNguyen resources = taiNguyenUtil.createResource(fileCode, post);
				// Thêm tài nguyên vào list tài nguyên của post
				tnList.add(resources);
				// Lưu tài nguyên vào DB
				taiNguyenRepo.save(resources);
			}
		}
		post.setTaiNguyenList(tnList);
		postRepo.save(post);

		// Gửi thông báo bài viết mới
//		String title = "Có bài viết mới. Xem ngay!";
//		String content = nhanVien.getTenNhanVien() + " vừa tạo 1 bài viết mới!\n" + post.getTitle();
//		String url = "api/public/posts/" + post.getPost_id();
//		notiServ.sendNotificationToAllUsers(title, content, url);
	}
	

	@Override
	public void updatePost(Integer post_id, postRequestDTO postRequestDTO,
						   List<MultipartFile> files,
						   List<Integer> deleteFileIds) throws IOException {
		Post post = postRepo.findById(post_id).orElse(null);
		
		if(post != null) {
			post.setTitle(postRequestDTO.getTitle());
			post.setContent(postRequestDTO.getContent());
			post.setCreateAt(new Date(System.currentTimeMillis()));
			List<TaiNguyen> tnList = post.getTaiNguyenList();
			
			//Xử lí file thêm mới
			if(files != null && !files.isEmpty()) {
				for(MultipartFile file: files) {
					// Lưu file và lấy fileCode
					String fileCode = fileServ.uploadFile(file, fileDirection.pathForTaiNguyen);
					// Tạo tài nguyên
					TaiNguyen resources = taiNguyenUtil.createResource(fileCode, post);
					// Thêm tài nguyên vào list tài nguyên của post
					tnList.add(resources);
					// Lưu tài nguyên vào DB
					taiNguyenRepo.save(resources);
				}
			}
			//Xử lí các file cần xóa
		    if (deleteFileIds != null) {
		        for (Integer fileId : deleteFileIds) {
		            TaiNguyen tn = taiNguyenRepo.findById(fileId).orElse(null);
		            if(tn != null) {
		            	// Xóa file trên server, xóa tài nguyên khỏi list tài nguyên của bài viết và xóa bản ghi tài nguyên
		            	fileServ.deleteFile(tn.getResourceId(), 1);
			            tnList.remove(tn);
			            taiNguyenRepo.delete(tn);
		            }
		        }
		    }
		    post.setTaiNguyenList(tnList);
			postRepo.save(post);

		}else {
			throw new EntityNotFoundException("Post not found with id: " + post_id);
		}
	}

	@Override
	public void deletePost(Integer post_id) {
		Post existedPost = postRepo.findById(post_id).orElse(null);
		if(existedPost != null) {
			List<TaiNguyen> tnlist = existedPost.getTaiNguyenList();
			// Xóa hết các tài nguyên liên quan đến bài viết
			for(TaiNguyen tn: tnlist) {
				fileServ.deleteFile(tn.getResourceId(), 1);
			}
			postRepo.delete(existedPost);
		}else {
			throw new EntityNotFoundException("Post not found with id: " + post_id);
		}
	}

	@Override
	public boolean isOwner(Integer postId, Integer authorId) {
		boolean isOwner = postRepo.existsByPostIdAndNhanVien_IdUser(postId, authorId);
		if (!isOwner) {
			throw new AccessDeniedException("You do not have permission to modify this post.");
		}
		return true;
	}
}

package com.kma.services.Impl;

import java.io.IOException;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

import com.kma.constants.fileDirection;
import com.kma.converter.postDTOConverter;
import com.kma.models.paginationResponseDTO;
import com.kma.models.postResponseDTO;
import com.kma.repository.taiNguyenRepo;
import com.kma.utilities.taiNguyenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kma.models.postDTO;
import com.kma.models.postRequestDTO;
import com.kma.repository.nhanVienRepo;
import com.kma.repository.postRepo;
import com.kma.repository.entities.NhanVien;
import com.kma.repository.entities.Post;
import com.kma.repository.entities.TaiNguyen;
import com.kma.services.fileService;
import com.kma.services.postService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class postServiceImpl implements postService{
	@Autowired
	postRepo postRepo;
	@Autowired
	nhanVienRepo nvRepo;
	@Autowired
	fileService fileServ;
	@Autowired
	taiNguyenRepo taiNguyenRepo;
	@Autowired
	postDTOConverter dtoConverter;

	@Override
	public postDTO getById(Integer post_id) {
		Post post = postRepo.findById(post_id).orElse(null);
        return dtoConverter.convertToPostDTO(post);
	}

	@Override
	public paginationResponseDTO<postResponseDTO> getAllPost(Map<String, Object> params, Integer page, Integer size) {
		// Lấy giá trị từ params
		String title = (String) params.get("title");
		String authorName = (String) params.get("author_name");

		// Lấy danh sách ID nhân viên từ tên
		List<Integer> idUsers = Optional.ofNullable(authorName)
				.map(nvRepo::findByTenNhanVienContaining)
				.orElse(Collections.emptyList())
				.stream()
				.map(NhanVien::getIdUser)
				.toList();

		// Tạo Pageable
		Pageable pageable = PageRequest.of(page, size);

		// Lấy dữ liệu từ repository
		Page<Post> postPage = fetchPosts(title, idUsers, pageable);

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

	// Lấy bài viết từ repository
	private Page<Post> fetchPosts(String title, List<Integer> idUsers, Pageable pageable) {
		if (idUsers.isEmpty()) {
			// Không có authorName
			if (title == null) {
				return postRepo.findAllByOrderByPostIdDesc(pageable);
			} else {
				return postRepo.findByTitleContainingOrderByPostIdDesc(title, pageable);
			}
		} else {
			// Có authorName
			if (title == null) {
				return postRepo.findByNhanVien_idUserInOrderByPostIdDesc(idUsers, pageable);
			} else {
				return postRepo.findByTitleContainingAndNhanVien_idUserInOrderByPostIdDesc(title, idUsers, pageable);
			}
		}
	}

	@Override
	public List<postDTO> getLatestPosts() {
		// Tìm kiếm bài viết mới nhất
		List<Post> posts = postRepo.findTop6ByOrderByPostIdDesc();
		return posts.stream()
				.map(i->dtoConverter.convertToPostDTO(i))
				.collect(Collectors.toList());
	}

	@Override
	public void addPost(List<MultipartFile> files,
						postRequestDTO postRequestDTO) throws IOException {
		
		// Kiểm tra tính hợp lệ của author_id
		if(postRequestDTO.getAuthor_id() == null) {
			throw new IllegalArgumentException("Invalid Author ID");
		}
		// Kiểm tra xem nhân viên có tồn tại không
		NhanVien nhanVien = nvRepo.findById(postRequestDTO.getAuthor_id()).orElse(null);
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
	}
	

	@Override
	public void updatePost(Integer post_id, postRequestDTO postRequestDTO,
						   List<MultipartFile> files,
						   List<Integer> deleteFileIds) throws IOException {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
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
}

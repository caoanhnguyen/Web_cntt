package com.kma.services.Impl;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.kma.models.fileDTO;
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
import com.kma.utilities.mapUtil;

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
	ModelMapper modelMapper;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public List<postDTO> getAllPost(Map<String,Object> params) {
		postRequestDTO prDTO = new postRequestDTO();
		
		if(params.containsKey("title")) {
			prDTO.setTitle(mapUtil.getObject(params, "title", String.class));
		}
	
		if(params.containsKey("author_name")) {
			String TenNhanVien = mapUtil.getObject(params, "author_name", String.class);
			
			List<NhanVien> nvList = nvServ.findByName(TenNhanVien);
			prDTO.setAuthor_id(nvList.get(0).getId_User());
		}
		
		List<Post> posts = postRepo.getAllPost(prDTO);
		List<postDTO> DTO = new ArrayList<postDTO>();
		for(Post items: posts) {
			postDTO dto = new postDTO();
			dto.setPost_id(items.getPost_id());
			dto.setTitle(items.getTitle());
			dto.setContent(items.getContent());
			dto.setCreate_at(items.getCreate_at());
			
			List<TaiNguyen> tnList = items.getTaiNguyenList();
			List<fileDTO> fileDTO = fileServ.getListFileDTO(tnList);
			
			dto.setFile_dto(fileDTO);
			dto.setAuthor(items.getNhanVien().getTenNhanVien());
			
			DTO.add(dto);
		}
		return DTO;
	}

	@Override
	public void addPost(@RequestParam(value = "file", required = false) List<MultipartFile> files,
						@ModelAttribute postRequestDTO postRequestDTO) throws IOException {
		
		// Kiểm tra tính hợp lệ của author_id
		if(postRequestDTO.getAuthor_id() == null) {
			throw new IllegalArgumentException("Invalid Author ID");
		}
		
		NhanVien nhanVien = nvRepo.findByID(postRequestDTO.getAuthor_id());
		if(nhanVien == null) {
			throw new IllegalArgumentException("Invalid Author ID");
		}
		//Tạo post để lưu 
		Post post = new Post();
		post.setTitle(postRequestDTO.getTitle());
		post.setContent(postRequestDTO.getContent());
		post.setCreate_at(new Date(System.currentTimeMillis()));
		
		post.setNhanVien(nhanVien);
		
		List<TaiNguyen> tnList = post.getTaiNguyenList();
		
		// Upload file và lấy đường dẫn
		for (MultipartFile item: files) {
			
			String fileCode = fileServ.uploadFile(item);
			// Tạo tài nguyên
			TaiNguyen resources = new TaiNguyen();
			resources.setDescription(postRequestDTO.getTitle());
			resources.setCreate_at(new Date(System.currentTimeMillis()));
			resources.setFile_code(fileCode);
			
			//Them post vao list bai viet cua tai nguyen
			resources.setPost(post);
			
			//Them tai nguyen vao list tai nguyen cua post
			tnList.add(resources);
			
			taiNguyenServ.addTaiNguyen(resources);
		}
		
		postRepo.addPost(post);	
	}
	

	@Override
	public void updatePost(Integer post_id, postRequestDTO postRequestDTO, List<MultipartFile> files,
			List<Integer> deleteFileIds) throws IOException {
		// TODO Auto-generated method stub
		Post post = entityManager.find(Post.class, post_id);
		
		if(post != null) {
			post.setTitle(postRequestDTO.getTitle());
			post.setContent(postRequestDTO.getContent());
			post.setCreate_at(new Date(System.currentTimeMillis()));
			
			List<TaiNguyen> tnList = post.getTaiNguyenList();
			
			//Xử lí file thêm mới
			if(files != null && !files.isEmpty()) {
				for(MultipartFile file: files) {
					String fileCode = fileServ.uploadFile(file);
					
					// Tạo tài nguyên
					TaiNguyen resources = new TaiNguyen();
					resources.setDescription(postRequestDTO.getTitle());
					resources.setCreate_at(new Date(System.currentTimeMillis()));
					resources.setFile_code(fileCode);
					
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
		            	fileServ.deleteFile(f.getResource_id());
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
		Post existedPost = postRepo.findByID(post_id);
		if(existedPost != null) {
			List<TaiNguyen> tnlist = existedPost.getTaiNguyenList();
			for(TaiNguyen tn: tnlist) {
				fileServ.deleteFile(tn.getResource_id());
			}
			entityManager.remove(existedPost);
		}else {
			throw new EntityNotFoundException("Post not found with id: " + post_id);
		}
	}
	
	
	
	
}

package com.kma.repository.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import com.kma.models.postRequestDTO;
import com.kma.repository.nhanVienRepo;
import com.kma.repository.postRepo;
import com.kma.repository.entities.Post;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

@Repository
@Primary
@Transactional
public class postRepoImpl implements postRepo{
	
	@Autowired
	nhanVienRepo nvRepo;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Post> getAllPost(postRequestDTO prDTO) {
		// TODO Auto-generated method stub
		
		//JPQL
//		StringBuilder sql = new StringBuilder("FROM Post b ");
//		if(!prDTO.getTitle().isEmpty()) {
//			sql.append("");
//		}
//		Query query = entityManager.createQuery(sql.toString(),Post.class); 
//		return query.getResultList();
		
		//SQL Native
		StringBuilder sql = new StringBuilder("SELECT * FROM bai_viet b where 1=1 ");
		if(prDTO.getTitle() != null) {
			sql.append("and b.title LIKE '%" + prDTO.getTitle() + "%'");
		}
		if(prDTO.getAuthor_id() != null) {
			sql.append("and b.author_id = " + prDTO.getAuthor_id());
		}
		Query query = entityManager.createNativeQuery(sql.toString(),Post.class); 
		return query.getResultList();
	}

	@Override
	public void addPost(Post post) {
		// TODO Auto-generated method stub
		try {
			//SQL Native
			if (post.getPost_id() == null) { // Kiểm tra nếu ID chưa có nghĩa là đối tượng mới
		        entityManager.persist(post); // persist đối tượng mới
		    } else {
		        entityManager.merge(post); // merge nếu đối tượng có ID (trường hợp update)
		    }
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public Post findByID(Integer post_id) {
		Post post = entityManager.find(Post.class, post_id);
		return post;
	}
		
}

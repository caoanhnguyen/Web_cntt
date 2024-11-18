package com.kma.repository.Impl;

import org.springframework.stereotype.Repository;

import com.kma.repository.taiNguyenRepo;
import com.kma.repository.entities.TaiNguyen;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class taiNguyenRepoImpl implements taiNguyenRepo{
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public void addTaiNguyen(TaiNguyen resources) {
		// TODO Auto-generated method stub
		try {
			if (resources.getResource_id() == null) { // Kiểm tra nếu ID chưa có nghĩa là đối tượng mới
		        entityManager.persist(resources); // persist đối tượng mới
		    } else {
		        entityManager.merge(resources); // merge nếu đối tượng có ID (trường hợp update)
		    }
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public Integer getIDByFileCode(String fileCode) {
		// TODO Auto-generated method stub
		return null;
	}	
}

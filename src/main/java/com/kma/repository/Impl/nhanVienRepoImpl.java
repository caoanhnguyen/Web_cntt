package com.kma.repository.Impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kma.models.nhanVienRequestDTO;
import com.kma.repository.nhanVienRepo;
import com.kma.repository.entities.NhanVien;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

@Repository
@Transactional

@SuppressWarnings("unchecked")
public class nhanVienRepoImpl implements nhanVienRepo{
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public String getNameByID(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NhanVien findByID(Integer id) {
		// TODO Auto-generated method stub
		NhanVien nv = entityManager.find(NhanVien.class, id);
		return nv;
	}

	@Override
	public List<NhanVien> getAllNhanVien(nhanVienRequestDTO nvRequestDTO) {
		// TODO Auto-generated method stub
		//SQL Native
		StringBuilder sql = new StringBuilder("SELECT * FROM nhan_vien nv where 1=1 ");
		if(nvRequestDTO.getTenNhanVien() != null) {
			sql.append("and nv.TenNhanVien LIKE '%" + nvRequestDTO.getTenNhanVien() + "%'");
		}
		if(nvRequestDTO.getTenMonHoc() != null) {
			sql.append("and nv.MonGiangDayChinh LIKE '%" + nvRequestDTO.getTenMonHoc() + "%'");
		}
		Query query = entityManager.createNativeQuery(sql.toString(),NhanVien.class); 
		return query.getResultList();
	}

	@Override
	public List<NhanVien> findByName(String TenNhanVien) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder("SELECT * FROM nhan_vien nv where 1=1 and nv.TenNhanVien like '%" + TenNhanVien + "%'");
		Query query = entityManager.createNativeQuery(sql.toString(),NhanVien.class); 
		return query.getResultList();
	}
	
	
	
}

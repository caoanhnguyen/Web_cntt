package com.kma.repository;

import java.util.List;

import com.kma.models.nhanVienRequestDTO;
import com.kma.repository.entities.NhanVien;

public interface nhanVienRepo {
	String getNameByID(Integer id);
	
	NhanVien findByID(Integer id);
	
	List<NhanVien> getAllNhanVien(nhanVienRequestDTO nvRequestDTO);
	
	List<NhanVien> findByName(String TenNhanVien);
}

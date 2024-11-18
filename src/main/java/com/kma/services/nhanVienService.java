package com.kma.services;

import java.util.List;
import java.util.Map;

import com.kma.models.nhanVienDTO;
import com.kma.repository.entities.NhanVien;

public interface nhanVienService {
	String getNameByID(int id);
	
	List<nhanVienDTO> getAllNhanVien(Map<String, Object> params);
	
	List<NhanVien> findByName(String TenNhanVien);
}

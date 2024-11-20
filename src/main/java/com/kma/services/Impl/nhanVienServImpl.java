package com.kma.services.Impl;

import com.kma.converter.nhanVienDTOConverter;
import com.kma.models.nhanVienDTO;
import com.kma.repository.entities.MonHoc;
import com.kma.repository.entities.NhanVien;
import com.kma.repository.monHocRepo;
import com.kma.repository.nhanVienRepo;
import com.kma.services.nhanVienService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class nhanVienServImpl implements nhanVienService{
	@Autowired
	nhanVienRepo nvRepo;
	@Autowired
	monHocRepo mhRepo;

	@Override
	public List<nhanVienDTO> getAllNhanVien(Map<String, Object> params) {
		// TODO Auto-generated method stub
		// Lấy giá trị từ params
		String tenNhanVien = (String) params.get("name");
		String tenMonHoc = (String) params.get("ten_mon");
		// Tìm môn học theo tên môn
		MonHoc mh = mhRepo.findByTenMonHocContaining(tenMonHoc);
		// Tìm kiếm nhân viên theo điều kiện
		List<NhanVien> nvList;
		if(tenNhanVien==null){
			if(mh==null){
				nvList = nvRepo.findAll();
			}else{
				nvList = mh.getNvList();
			}
		}else{
			if(mh==null){
				nvList = nvRepo.findByTenNhanVienContaining(tenNhanVien);
			}else{
				nvList = nvRepo.findByNameAndMonHoc(tenNhanVien, tenMonHoc);
			}
		}

		return nvList.stream()
					 .map(nhanVienDTOConverter::convertToNhanVienDTO)
					 .toList();
	}

}

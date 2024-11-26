package com.kma.services.Impl;

import com.kma.converter.nhanVienDTOConverter;
import com.kma.models.nhanVienDTO;
import com.kma.repository.entities.MonHoc;
import com.kma.repository.entities.NhanVien;
import com.kma.repository.monHocRepo;
import com.kma.repository.nhanVienRepo;
import com.kma.services.nhanVienService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class nhanVienServImpl implements nhanVienService{
	@Autowired
	nhanVienRepo nvRepo;
	@Autowired
	monHocRepo mhRepo;
	@Autowired
	nhanVienDTOConverter nvDTOConverter;

	@Override
	public nhanVienDTO getById(Integer idUser) {
		NhanVien nv = nvRepo.findById(idUser).orElse(null);
		if(nv!=null)
			return nvDTOConverter.convertToNhanVienDTO(nv);
		throw new EntityNotFoundException("Employee not found with id: " + idUser);
	}

	@Override
	public List<nhanVienDTO> getAllNhanVien(Map<String, Object> params) {
		// TODO Auto-generated method stub
		// Lấy giá trị từ params
		String tenNhanVien = (String) params.get("name");
		String tenMonHoc = (String) params.get("ten_mon");
		// Tìm môn học theo tên môn
		List<MonHoc> mhList = mhRepo.findByTenMonHocContaining(tenMonHoc);
		// Tìm kiếm nhân viên theo điều kiện
		List<NhanVien> nvList;
		if(tenNhanVien==null){
			if(mhList==null || mhList.isEmpty()){
				nvList = nvRepo.findAll();
			}else{
				nvList = mhList.stream() // Duyệt qua từng môn học trong danh sách
						.flatMap(mh -> mh.getNvList().stream()) // Lấy danh sách nhân viên liên quan đến từng môn học
						.distinct() // Loại bỏ trùng lặp nhân viên (nếu có nhân viên dạy nhiều môn)
						.toList(); // Thu thập thành một danh sách
			}
		}else{
			if(mhList==null){
				nvList = nvRepo.findByTenNhanVienContaining(tenNhanVien);
			}else{
				nvList = nvRepo.findByNameAndMonHoc(tenNhanVien, tenMonHoc);
			}
		}
		return nvList.stream()
					 .map(nvDTOConverter::convertToNhanVienDTO)
					 .toList();
	}



	@Override
	public void deleteNhanVien(Integer idUser) {
		// TODO Auto-generated method stub
		NhanVien existedNV = nvRepo.findById(idUser).orElse(null);
		if(existedNV != null) {
			nvRepo.delete(existedNV);
		}else {
			throw new EntityNotFoundException("User not found with id: " + idUser);
		}
	}
}

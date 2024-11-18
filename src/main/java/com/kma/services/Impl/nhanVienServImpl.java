package com.kma.services.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kma.converter.nhanVienDTOConverter;
import com.kma.models.nhanVienDTO;
import com.kma.models.nhanVienRequestDTO;
import com.kma.repository.nhanVienRepo;
import com.kma.repository.entities.NhanVien;
import com.kma.services.nhanVienService;
import com.kma.utilities.mapUtil;

@Service
public class nhanVienServImpl implements nhanVienService{
	@Autowired
	nhanVienRepo nvRepo;
	
	@Autowired
	nhanVienDTOConverter nvDTOConverter;
	
	@Override
	public String getNameByID(int id) {
		// TODO Auto-generated method stub
		String name = nvRepo.getNameByID(id);
		return name;
	}

	@Override
	public List<nhanVienDTO> getAllNhanVien(Map<String, Object> params) {
		// TODO Auto-generated method stub
		nhanVienRequestDTO nvRequestDTO = new nhanVienRequestDTO();
		nvRequestDTO.setTenNhanVien(mapUtil.getObject(params, "name", String.class));
		nvRequestDTO.setTenMonHoc(mapUtil.getObject(params, "ten_mon", String.class));
		
		List<NhanVien> nvList = nvRepo.getAllNhanVien(nvRequestDTO);
		List<nhanVienDTO> nvDTO = new ArrayList<nhanVienDTO>();
		for(NhanVien nv: nvList) {
			nhanVienDTO dto = nvDTOConverter.convertToNhanVienDTO(nv);
			nvDTO.add(dto);
		}
		
		return nvDTO;
	}
	
	@Override
	public List<NhanVien> findByName(String TenNhanVien){
		// TODO Auto-generated method stub
		List<NhanVien> nvList = nvRepo.findByName(TenNhanVien);
		return nvList;
	}
	
}

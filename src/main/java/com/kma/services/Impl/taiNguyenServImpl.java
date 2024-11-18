package com.kma.services.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kma.repository.taiNguyenRepo;
import com.kma.repository.entities.TaiNguyen;
import com.kma.services.taiNguyenService;

@Service
public class taiNguyenServImpl implements taiNguyenService{

	@Autowired
	taiNguyenRepo taiNguyenRepo;
	
	@Override
	public void addTaiNguyen(TaiNguyen resources) {
		// TODO Auto-generated method stub
		taiNguyenRepo.addTaiNguyen(resources);
	}

	@Override
	public Integer getIDByFileCode(String fileCode) {
		// TODO Auto-generated method stub
		Integer id = taiNguyenRepo.getIDByFileCode(fileCode);
		return id;
	}
	
	
	
}

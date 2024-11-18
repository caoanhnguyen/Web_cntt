package com.kma.services;

import com.kma.repository.entities.TaiNguyen;

public interface taiNguyenService {
	void addTaiNguyen(TaiNguyen resources);
	
	Integer getIDByFileCode(String fileCode);
}

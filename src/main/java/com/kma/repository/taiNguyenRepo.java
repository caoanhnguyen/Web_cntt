package com.kma.repository;

import com.kma.repository.entities.TaiNguyen;

public interface taiNguyenRepo {
	void addTaiNguyen(TaiNguyen resources);
	
	Integer getIDByFileCode(String fileCode);
}

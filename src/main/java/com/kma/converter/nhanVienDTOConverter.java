package com.kma.converter;
import org.springframework.stereotype.Component;

import com.kma.models.nhanVienDTO;
import com.kma.repository.entities.NhanVien;

@Component
public class nhanVienDTOConverter {

    public nhanVienDTO convertToNhanVienDTO(NhanVien nv) {
		nhanVienDTO dto = new nhanVienDTO();
		dto.setTenNhanVien(nv.getTenNhanVien());
		dto.setNgaySinh(nv.getNgaySinh());
		dto.setSDT(nv.getDienThoai());
		dto.setDiaChi(nv.getDiaChiCCCD());
		dto.setPhongBan(nv.getMaPhongBan()); //để tạm là mã phòng ban vì chưa tạo phòng ban chưa lấy được tên
		dto.setChucVu(nv.getChucVu());
		dto.setCacMonLienQuan(null);
		dto.setAva_img_code(null);
		
		return dto;
	}
}

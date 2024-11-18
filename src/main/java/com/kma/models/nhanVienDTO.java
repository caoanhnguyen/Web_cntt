package com.kma.models;

import java.sql.Date;

public class nhanVienDTO {
	private String TenNhanVien;
	private Date NgaySinh;
	private String SDT;
	private String diaChi;
	private String PhongBan;
	private String ChucVu;
	private String CacMonLienQuan;
	private String ava_img_code;
	
	public String getTenNhanVien() {
		return TenNhanVien;
	}
	public void setTenNhanVien(String tenNhanVien) {
		TenNhanVien = tenNhanVien;
	}
	public Date getNgaySinh() {
		return NgaySinh;
	}
	public void setNgaySinh(Date ngaySinh) {
		NgaySinh = ngaySinh;
	}
	public String getSDT() {
		return SDT;
	}
	public void setSDT(String sDT) {
		SDT = sDT;
	}
	public String getDiaChi() {
		return diaChi;
	}
	public void setDiaChi(String diaChi) {
		this.diaChi = diaChi;
	}
	public String getPhongBan() {
		return PhongBan;
	}
	public void setPhongBan(String phongBan) {
		PhongBan = phongBan;
	}
	public String getChucVu() {
		return ChucVu;
	}
	public void setChucVu(String chucVu) {
		ChucVu = chucVu;
	}
	public String getCacMonLienQuan() {
		return CacMonLienQuan;
	}
	public void setCacMonLienQuan(String cacMonLienQuan) {
		CacMonLienQuan = cacMonLienQuan;
	}
	public String getAva_img_code() {
		return ava_img_code;
	}
	public void setAva_img_code(String ava_img_code) {
		this.ava_img_code = ava_img_code;
	}
}

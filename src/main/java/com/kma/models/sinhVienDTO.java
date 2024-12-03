package com.kma.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Date;

public class sinhVienDTO {
	private String maSinhVien;
	private String tenSinhVien;
	private String gioiTinh;
	@JsonFormat(pattern = "yyyy-MM-dd")  // Định dạng ngày tháng năm
	private Date ngaySinh;
	private String dienThoai;
	private String email;
	private String CCCD;
	private String diaChiHienTai;
	private String queQuan;
	private String khoa;
	private String chucVu;
	private String avaDownloadUrl;
	private String tenLop;

	public String getMaSinhVien() {
		return maSinhVien;
	}

	public void setMaSinhVien(String maSinhVien) {
		this.maSinhVien = maSinhVien;
	}

	public String getTenSinhVien() {
		return tenSinhVien;
	}

	public void setTenSinhVien(String tenSinhVien) {
		this.tenSinhVien = tenSinhVien;
	}

	public String getGioiTinh() {
		return gioiTinh;
	}

	public void setGioiTinh(String gioiTinh) {
		this.gioiTinh = gioiTinh;
	}

	public Date getNgaySinh() {
		return ngaySinh;
	}

	public void setNgaySinh(Date ngaySinh) {
		this.ngaySinh = ngaySinh;
	}

	public String getDienThoai() {
		return dienThoai;
	}

	public void setDienThoai(String dienThoai) {
		this.dienThoai = dienThoai;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCCCD() {
		return CCCD;
	}

	public void setCCCD(String CCCD) {
		this.CCCD = CCCD;
	}

	public String getDiaChiHienTai() {
		return diaChiHienTai;
	}

	public void setDiaChiHienTai(String diaChiHienTai) {
		this.diaChiHienTai = diaChiHienTai;
	}

	public String getQueQuan() {
		return queQuan;
	}

	public void setQueQuan(String queQuan) {
		this.queQuan = queQuan;
	}

	public String getKhoa() {
		return khoa;
	}

	public void setKhoa(String khoa) {
		this.khoa = khoa;
	}

	public String getChucVu() {
		return chucVu;
	}

	public void setChucVu(String chucVu) {
		this.chucVu = chucVu;
	}

	public String getAvaDownloadUrl() {
		return avaDownloadUrl;
	}

	public void setAvaDownloadUrl(String avaDownloadUrl) {
		this.avaDownloadUrl = avaDownloadUrl;
	}

	public String getTenLop() {
		return tenLop;
	}

	public void setTenLop(String tenLop) {
		this.tenLop = tenLop;
	}
}

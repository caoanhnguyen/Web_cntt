package com.kma.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Date;

public class nhanVienRequestDTO {
	private String maNhanVien;
	private String tenNhanVien;
	@JsonFormat(pattern = "yyyy-MM-dd")  // Định dạng ngày tháng năm
	private Date ngaySinh;
	private String gioiTinh;
	private String dienThoai;
	private String hocVi;
	private String cccd;
	@JsonFormat(pattern = "yyyy-MM-dd")  // Định dạng ngày tháng năm
	private Date ngayCapCCCD;
	private String noiCapCCCD;
	private String diaChiCCCD;
	private String diaChiHienNay;
	private String chucVu;
	private String noiCongTac;
	private String maSoThue;
	private String soTaiKhoan;
	private String nganHang;
	private String chiNhanh;
	private String fileLyLich;
	private String maPhongBan;

	public String getGioiTinh() {
		return gioiTinh;
	}

	public void setGioiTinh(String gioiTinh) {
		this.gioiTinh = gioiTinh;
	}

	public String getMaPhongBan() {
		return maPhongBan;
	}

	public void setMaPhongBan(String maPhongBan) {
		this.maPhongBan = maPhongBan;
	}

	public String getMaNhanVien() {
		return maNhanVien;
	}

	public void setMaNhanVien(String maNhanVien) {
		this.maNhanVien = maNhanVien;
	}

	public String getTenNhanVien() {
		return tenNhanVien;
	}

	public void setTenNhanVien(String tenNhanVien) {
		this.tenNhanVien = tenNhanVien;
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

	public String getHocVi() {
		return hocVi;
	}

	public void setHocVi(String hocVi) {
		this.hocVi = hocVi;
	}

	public String getCccd() {
		return cccd;
	}

	public void setCccd(String cccd) {
		this.cccd = cccd;
	}

	public Date getNgayCapCCCD() {
		return ngayCapCCCD;
	}

	public void setNgayCapCCCD(Date ngayCapCCCD) {
		this.ngayCapCCCD = ngayCapCCCD;
	}

	public String getNoiCapCCCD() {
		return noiCapCCCD;
	}

	public void setNoiCapCCCD(String noiCapCCCD) {
		this.noiCapCCCD = noiCapCCCD;
	}

	public String getDiaChiCCCD() {
		return diaChiCCCD;
	}

	public void setDiaChiCCCD(String diaChiCCCD) {
		this.diaChiCCCD = diaChiCCCD;
	}

	public String getDiaChiHienNay() {
		return diaChiHienNay;
	}

	public void setDiaChiHienNay(String diaChiHienNay) {
		this.diaChiHienNay = diaChiHienNay;
	}

	public String getChucVu() {
		return chucVu;
	}

	public void setChucVu(String chucVu) {
		this.chucVu = chucVu;
	}

	public String getNoiCongTac() {
		return noiCongTac;
	}

	public void setNoiCongTac(String noiCongTac) {
		this.noiCongTac = noiCongTac;
	}

	public String getMaSoThue() {
		return maSoThue;
	}

	public void setMaSoThue(String maSoThue) {
		this.maSoThue = maSoThue;
	}

	public String getSoTaiKhoan() {
		return soTaiKhoan;
	}

	public void setSoTaiKhoan(String soTaiKhoan) {
		this.soTaiKhoan = soTaiKhoan;
	}

	public String getNganHang() {
		return nganHang;
	}

	public void setNganHang(String nganHang) {
		this.nganHang = nganHang;
	}

	public String getChiNhanh() {
		return chiNhanh;
	}

	public void setChiNhanh(String chiNhanh) {
		this.chiNhanh = chiNhanh;
	}

	public String getFileLyLich() {
		return fileLyLich;
	}

	public void setFileLyLich(String fileLyLich) {
		this.fileLyLich = fileLyLich;
	}
}

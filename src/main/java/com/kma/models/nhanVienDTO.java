package com.kma.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kma.repository.entities.PhongBan;

import java.sql.Date;
import java.util.List;

public class nhanVienDTO {
	private Integer idUser;
	private String tenNhanVien;
	private String maNhanVien;

	@JsonFormat(pattern = "yyyy-MM-dd")  // Định dạng ngày tháng năm
	private Date ngaySinh;
	private String gioiTinh;
	private String dienThoai;
	private String diaChi;
	private phongBanResponseDTO phongBan;
	private String chucVu;
	private monHocResponseDTO monGiangDayChinh;
	private List<monHocResponseDTO> cacMonLienQuan;
	private String avaFileCode;

	public String getMaNhanVien() {
		return maNhanVien;
	}

	public void setMaNhanVien(String maNhanVien) {
		this.maNhanVien = maNhanVien;
	}

	public String getGioiTinh() {
		return gioiTinh;
	}

	public void setGioiTinh(String gioiTinh) {
		this.gioiTinh = gioiTinh;
	}

	public Integer getIdUser() {
		return idUser;
	}

	public void setIdUser(Integer idUser) {
		this.idUser = idUser;
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

	public String getDiaChi() {
		return diaChi;
	}

	public void setDiaChi(String diaChi) {
		this.diaChi = diaChi;
	}

	public phongBanResponseDTO getPhongBan() {
		return phongBan;
	}

	public void setPhongBan(phongBanResponseDTO phongBan) {
		this.phongBan = phongBan;
	}

	public String getChucVu() {
		return chucVu;
	}

	public void setChucVu(String chucVu) {
		this.chucVu = chucVu;
	}

	public monHocResponseDTO getMonGiangDayChinh() {
		return monGiangDayChinh;
	}

	public void setMonGiangDayChinh(monHocResponseDTO monGiangDayChinh) {
		this.monGiangDayChinh = monGiangDayChinh;
	}

	public List<monHocResponseDTO> getCacMonLienQuan() {
		return cacMonLienQuan;
	}

	public void setCacMonLienQuan(List<monHocResponseDTO> cacMonLienQuan) {
		this.cacMonLienQuan = cacMonLienQuan;
	}

	public String getAvaFileCode() {
		return avaFileCode;
	}

	public void setAvaFileCode(String avaFileCode) {
		this.avaFileCode = avaFileCode;
	}
}

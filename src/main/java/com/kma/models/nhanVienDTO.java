package com.kma.models;

import java.sql.Date;
import java.util.List;

public class nhanVienDTO {
	private Integer idUser;
	private String tenNhanVien;
	private Date ngaySinh;
	private String sdt;
	private String diaChi;
	private String phongBan;
	private String chucVu;
	private String monGiangDayChinh;
	private List<monHocResponseDTO> cacMonLienQuan;
	private String avaFileCode;

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

	public String getSdt() {
		return sdt;
	}

	public void setSdt(String sdt) {
		this.sdt = sdt;
	}

	public String getDiaChi() {
		return diaChi;
	}

	public void setDiaChi(String diaChi) {
		this.diaChi = diaChi;
	}

	public String getPhongBan() {
		return phongBan;
	}

	public void setPhongBan(String phongBan) {
		this.phongBan = phongBan;
	}

	public String getChucVu() {
		return chucVu;
	}

	public void setChucVu(String chucVu) {
		this.chucVu = chucVu;
	}

	public String getMonGiangDayChinh() {
		return monGiangDayChinh;
	}

	public void setMonGiangDayChinh(String monGiangDayChinh) {
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

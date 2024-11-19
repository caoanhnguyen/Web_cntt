package com.kma.repository.entities;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="nhan_vien")
public class NhanVien {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUser;

    @Column(name = "MaNhanVien")
    private String maNhanVien;

    @Column(name = "TenNhanVien")
    private String tenNhanVien;

    @Column(name = "NgaySinh")
    private Date ngaySinh;

    @Column(name = "DienThoai")
    private String dienThoai;

    @Column(name = "HocVi")
    private String hocVi;

    @Column(name = "CCCD")
    private String cccd;

    @Column(name = "NgayCapCCCD")
    private Date ngayCapCCCD;

    @Column(name = "NoiCapCCCD")
    private String noiCapCCCD;

    @Column(name = "DiaChiCCCD")
    private String diaChiCCCD;

    @Column(name = "DiaChiHienNay")
    private String diaChiHienNay;

    @Column(name = "ChucVu")
    private String chucVu;

    @Column(name = "NoiCongTac")
    private String noiCongTac;

    @Column(name = "MaPhongBan")
    private String maPhongBan;

    @Column(name = "MaSoThue")
    private String maSoThue;

    @Column(name = "SoTaiKhoan")
    private String soTaiKhoan;

    @Column(name = "NganHang")
    private String nganHang;

    @Column(name = "ChiNhanh")
    private String chiNhanh;

    @Column(name = "FileLyLich")
    private String fileLyLich;

    @Column(name = "MonGiangDayChinh")
    private String monGiangDayChinh;

    @Column(name = "CacMonLienQuan")
    private String cacMonLienQuan;
    
    //Config relation to Post
    @OneToMany(mappedBy = "nhanVien")
	private List<Post> posts = new ArrayList<>();
    
    
	public List<Post> getPosts() {
		return posts;
	}
	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}
	public Integer getIdUser() {
		return idUser;
	}
	public void setIdUser(Integer idUser) {
		this.idUser = idUser;
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
	public String getMaPhongBan() {
		return maPhongBan;
	}
	public void setMaPhongBan(String maPhongBan) {
		this.maPhongBan = maPhongBan;
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
	public String getMonGiangDayChinh() {
		return monGiangDayChinh;
	}
	public void setMonGiangDayChinh(String monGiangDayChinh) {
		this.monGiangDayChinh = monGiangDayChinh;
	}
	public String getCacMonLienQuan() {
		return cacMonLienQuan;
	}
	public void setCacMonLienQuan(String cacMonLienQuan) {
		this.cacMonLienQuan = cacMonLienQuan;
	}
    
    

}

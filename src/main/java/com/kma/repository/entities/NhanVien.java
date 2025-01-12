package com.kma.repository.entities;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.kma.enums.GioiTinh;
import jakarta.persistence.*;

@Entity
@Table(name="nhan_vien")
public class NhanVien {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUser;

    @Column(name = "maNhanVien")
    private String maNhanVien;

    @Column(name = "tenNhanVien")
    private String tenNhanVien;

    @Column(name = "ngaySinh")
    private Date ngaySinh;

	@Enumerated(EnumType.STRING)
	@Column(name = "gioiTinh")
	private GioiTinh gioiTinh;

    @Column(name = "dienThoai")
    private String dienThoai;

    @Column(name = "hocVi")
    private String hocVi;

    @Column(name = "cccd")
    private String cccd;

    @Column(name = "ngayCapCCCD")
    private Date ngayCapCCCD;

    @Column(name = "noiCapCCCD")
    private String noiCapCCCD;

    @Column(name = "diaChiCCCD")
    private String diaChiCCCD;

    @Column(name = "diaChiHienNay")
    private String diaChiHienNay;

    @Column(name = "chucVu")
    private String chucVu;

    @Column(name = "noiCongTac")
    private String noiCongTac;

    @Column(name = "maSoThue")
    private String maSoThue;

    @Column(name = "soTaiKhoan")
    private String soTaiKhoan;

    @Column(name = "nganHang")
    private String nganHang;

    @Column(name = "chiNhanh")
    private String chiNhanh;

    @Column(name = "idMonGiangDayChinh")
    private Integer idMonGiangDayChinh;

    @Column(name = "avaFileCode")
    private String avaFileCode;
    
    //Config relation to Post
    @OneToMany(mappedBy = "nhanVien")
	private List<Post> posts;

	//Config relation to mon_hoc
	@ManyToMany(mappedBy = "nvList")
	private List<MonHoc> monHocList;

	//Config relation to lop
	@OneToMany(mappedBy = "chuNhiem")
	private List<Lop> lopList = new ArrayList<>();

	// Config relation to phong_ban
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "maPhongBan")
	private PhongBan phongBan;

	// Quan hệ 1-1 với User
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JoinColumn(name = "userId", referencedColumnName = "userId") // Khoá ngoại trỏ về User
	private User user;

	// Quan hệ 1-1 với CV
	@OneToOne(mappedBy = "nhanVien", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	private CV cv;

	public CV getCv() {
		return cv;
	}

	public void setCv(CV cv) {
		this.cv = cv;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public GioiTinh getGioiTinh() {
		return gioiTinh;
	}

	public void setGioiTinh(GioiTinh gioiTinh) {
		this.gioiTinh = gioiTinh;
	}

	public Date getNgaySinh() {
		return ngaySinh;
	}

	public void setNgaySinh(Date ngaySinh) {
		this.ngaySinh = ngaySinh;
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

	public Integer getIdMonGiangDayChinh() {
		return idMonGiangDayChinh;
	}

	public void setIdMonGiangDayChinh(Integer idMonGiangDayChinh) {
		this.idMonGiangDayChinh = idMonGiangDayChinh;
	}

	public String getAvaFileCode() {
		return avaFileCode;
	}

	public void setAvaFileCode(String avaFileCode) {
		this.avaFileCode = avaFileCode;
	}

	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

	public List<MonHoc> getMonHocList() {
		return monHocList;
	}

	public void setMonHocList(List<MonHoc> monHocList) {
		this.monHocList = monHocList;
	}

	public List<Lop> getLopList() {
		return lopList;
	}

	public void setLopList(List<Lop> lopList) {
		this.lopList = lopList;
	}

	public PhongBan getPhongBan() {
		return phongBan;
	}

	public void setPhongBan(PhongBan phongBan) {
		this.phongBan = phongBan;
	}
}

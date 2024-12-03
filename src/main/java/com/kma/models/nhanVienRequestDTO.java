package com.kma.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Date;

public class nhanVienRequestDTO {
	private String maNhanVien;
	private String tenNhanVien;
	@JsonFormat(pattern = "yyyy-MM-dd")  // Định dạng ngày tháng năm
	private Date ngaySinh;
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
	private String maPhongBan;
	private String maSoThue;
	private String soTaiKhoan;
	private String nganHang;
	private String chiNhanh;
	private String fileLyLich;
	private String monGiangDayChinh;
	private String AvaFileCode;

}

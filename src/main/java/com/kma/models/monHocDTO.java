package com.kma.models;

import java.util.List;

public class monHocDTO {
    private Integer idMonHoc;
    private String tenMonHoc;
    private String moTa;
    private Integer soTinChi;
    private List<fileDTO> taiLieuMHList;

    public Integer getIdMonHoc() {
        return idMonHoc;
    }

    public void setIdMonHoc(Integer idMonHoc) {
        this.idMonHoc = idMonHoc;
    }

    public String getTenMonHoc() {
        return tenMonHoc;
    }

    public void setTenMonHoc(String tenMonHoc) {
        this.tenMonHoc = tenMonHoc;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public Integer getSoTinChi() {
        return soTinChi;
    }

    public void setSoTinChi(Integer soTinChi) {
        this.soTinChi = soTinChi;
    }

    public List<fileDTO> getTaiLieuMHList() {
        return taiLieuMHList;
    }

    public void setTaiLieuMHList(List<fileDTO> taiLieuMHList) {
        this.taiLieuMHList = taiLieuMHList;
    }
}

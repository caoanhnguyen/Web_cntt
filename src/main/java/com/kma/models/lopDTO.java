package com.kma.models;

public class lopDTO {
    private Integer idLop;
    private String tenLop;
    private nhanVienResponseDTO chuNhiemDTO;

    public Integer getIdLop() {
        return idLop;
    }

    public void setIdLop(Integer idLop) {
        this.idLop = idLop;
    }

    public String getTenLop() {
        return tenLop;
    }

    public void setTenLop(String tenLop) {
        this.tenLop = tenLop;
    }

    public nhanVienResponseDTO getChuNhiemDTO() {
        return chuNhiemDTO;
    }

    public void setChuNhiemDTO(nhanVienResponseDTO chuNhiemDTO) {
        this.chuNhiemDTO = chuNhiemDTO;
    }
}

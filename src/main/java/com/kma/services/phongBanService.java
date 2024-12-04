package com.kma.services;

import com.kma.models.paginationResponseDTO;
import com.kma.models.phongBanResponseDTO;

import java.util.Map;

public interface phongBanService {
    paginationResponseDTO<phongBanResponseDTO> getAllPhongBan(Map<Object,Object> params, int page, int size);

    phongBanResponseDTO getById(String maPhongBan);

    void addPhongBan(phongBanResponseDTO pbResDTO);

    void updatePhongBan(String maPhongBan, phongBanResponseDTO pbResDTO);

    void deletePhongBan(String maPhongBan);
}

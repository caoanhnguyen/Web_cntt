package com.kma.services;

import com.kma.models.paginationResponseDTO;
import com.kma.models.postResponseDTO;
import com.kma.models.sinhVienResponseDTO;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface sinhVienService {
    paginationResponseDTO<sinhVienResponseDTO> getAllSinhVien(Map<String,Object> params, Integer page, Integer size);

}

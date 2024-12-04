package com.kma.services;

import com.kma.models.lopDTO;
import com.kma.models.lopRequestDTO;
import com.kma.models.paginationResponseDTO;

import java.util.Map;

public interface lopService {
    paginationResponseDTO<lopDTO> getAllClass(Map<String,Object> params, Integer page, Integer size);

    lopDTO findById(Integer idLop);

    void addLop(lopDTO DTO);

    void updateLop(Integer idLop, lopRequestDTO DTO);

    void deleteLop(Integer idLop);
}

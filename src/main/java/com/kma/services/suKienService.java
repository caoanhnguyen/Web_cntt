package com.kma.services;

import com.kma.models.paginationResponseDTO;
import com.kma.models.sinhVienResponseDTO;
import com.kma.models.suKienDTO;
import com.kma.models.suKienResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface suKienService {
    paginationResponseDTO<suKienResponseDTO> getAllEvent(Map<String,Object> params, Integer page, Integer size);

    suKienDTO findById(Integer eventId);

    void addEvent(List<MultipartFile> files, suKienResponseDTO skResDTO) throws IOException;

    void deleteEvent(Integer eventId);

    void updateEvent(Integer eventId, suKienResponseDTO skResDTO, List<MultipartFile> files, List<Integer> deleteFileIds) throws IOException;

    paginationResponseDTO<sinhVienResponseDTO> getAllSVInEvent(Integer eventId, String searchTerm, Integer page, Integer size);
}

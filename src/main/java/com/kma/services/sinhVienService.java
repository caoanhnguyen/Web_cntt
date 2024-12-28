package com.kma.services;

import com.kma.models.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface sinhVienService {
    paginationResponseDTO<sinhVienResponseDTO> getAllSinhVien(Map<String,Object> params, Integer page, Integer size);

    sinhVienDTO findById(String maSinhVien);

    void addSinhVien(MultipartFile file,
                     sinhVienDTO svDTO) throws IOException;

    void updateSinhVien(String maSinhVien,
                        sinhVienDTO svDTO,
                        MultipartFile file) throws IOException;

    void deleteSinhVien(String maSinhVien);

    List<suKienResponseDTO> getAllParicipatedEvent(String maSinhVien);

    boolean isOwner(String maSinhVien, String maSV);

}

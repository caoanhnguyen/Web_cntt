package com.kma.services.Impl;

import com.kma.converter.sinhVienDTOConverter;
import com.kma.models.paginationResponseDTO;
import com.kma.models.sinhVienResponseDTO;
import com.kma.repository.entities.SinhVien;
import com.kma.repository.sinhVienRepo;
import com.kma.services.sinhVienService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class sinhVienServImpl implements sinhVienService {
    @Autowired
    sinhVienRepo svRepo;
    @Autowired
    sinhVienDTOConverter svDTOConverter;


    @Override
    public paginationResponseDTO<sinhVienResponseDTO> getAllSinhVien(Map<String, Object> params, Integer page, Integer size) {
        // Tạo Pageable
        Pageable pageable = PageRequest.of(page, size);

        // Lấy dữ liệu từ repository
        Page<SinhVien> svPage = fetchSinhViens(params, pageable);

        // Chuyển đổi sang DTO
        List<sinhVienResponseDTO> svResDTOList = svPage.getContent().stream()
                .map(svDTOConverter::convertToSVResDTO)
                .toList();

        // Đóng gói dữ liệu và meta vào DTO
        return new paginationResponseDTO<>(
                svResDTOList,
                svPage.getTotalPages(),
                (int) svPage.getTotalElements(),
                svPage.isFirst(),
                svPage.isLast(),
                svPage.getNumber(),
                svPage.getSize()
        );
    }

    private Page<SinhVien> fetchSinhViens(Map<String, Object> params, Pageable pageable){
        // Lấy giá trị từ params
        String maSinhVien = ((String) params.get("maSinhVien") != null ? (String) params.get("maSinhVien") : "");
        String tenSinhVien = ((String) params.get("tenSinhVien") != null ? (String) params.get("tenSinhVien") : "");
        String tenLop = ((String) params.get("tenLop") != null ? (String) params.get("tenLop") : "");

        return svRepo.findByAllCondition(tenSinhVien, maSinhVien, tenLop, pageable);
    }
}

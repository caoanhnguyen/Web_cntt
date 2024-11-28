package com.kma.converter;

import com.kma.models.fileDTO;
import com.kma.models.sinhVienResponseDTO;
import com.kma.models.suKienDTO;
import com.kma.models.suKienResponseDTO;
import com.kma.repository.entities.DangKySuKien;
import com.kma.repository.entities.SuKien;
import com.kma.services.fileService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.List;

@Component
public class suKienDTOConverter {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    fileService fileServ;
    @Autowired
    sinhVienDTOConverter svDTOConverter;

    public suKienResponseDTO convertToSKResDTO(SuKien sk){
        return modelMapper.map(sk, suKienResponseDTO.class);
    }

    public suKienDTO convertToSKDTO(SuKien sk){
        suKienDTO dto = modelMapper.map(sk, suKienDTO.class);

        // Lấy danh sách tài nguyên liên quan
        List<fileDTO> fileDTOList = fileServ.getListFileDTO(sk.getTaiNguyenList());
        dto.setFileDTOList(fileDTOList);

        return dto;
    }

    public SuKien convertResDTOToSuKien(suKienResponseDTO skResDTO){
        SuKien event = modelMapper.map(skResDTO, SuKien.class);
        event.setCreateAt(new Date(System.currentTimeMillis()));

        return event;
    }
}

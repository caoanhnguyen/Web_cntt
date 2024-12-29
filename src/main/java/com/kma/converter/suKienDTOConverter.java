package com.kma.converter;

import com.kma.models.fileDTO;
import com.kma.models.suKienDTO;
import com.kma.models.suKienResponseDTO;
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

    public suKienResponseDTO convertToSKResDTO(SuKien sk){
        suKienResponseDTO dto = modelMapper.map(sk, suKienResponseDTO.class);

        // Lấy danh sách tài nguyên liên quan
        List<fileDTO> fileDTOList = fileServ.getListFileDTO(sk.getTaiNguyenList());
        dto.setFileDTOList(fileDTOList);

        return dto;
    }

    public suKienDTO convertToSKDTO(SuKien sk){
        suKienDTO dto = modelMapper.map(sk, suKienDTO.class);

        // Lấy danh sách tài nguyên liên quan
        List<fileDTO> fileDTOList = fileServ.getListFileDTO(sk.getTaiNguyenList());
        dto.setFileDTOList(fileDTOList);

        return dto;
    }

    public void convertResDTOToSuKien(suKienResponseDTO skResDTO, SuKien event){
        modelMapper.map(skResDTO, event);
        event.setCreateAt(new Date(System.currentTimeMillis()));
    }
}

package com.kma.converter;

import com.kma.enums.EventStatus;
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
        suKienResponseDTO dto = modelMapper.map(sk, suKienResponseDTO.class);
        dto.setStatus(sk.getStatus().getDisplayName());
        return dto;
    }

    public suKienDTO convertToSKDTO(SuKien sk){
        suKienDTO dto = modelMapper.map(sk, suKienDTO.class);
        dto.setStatus(sk.getStatus().getDisplayName());

        // Lấy danh sách tài nguyên liên quan
        List<fileDTO> fileDTOList = fileServ.getListFileDTO(sk.getTaiNguyenList());
        dto.setFileDTOList(fileDTOList);

        // Lấy danh sách sinh viên tham gia sự kiện này
        List<DangKySuKien> dkskList = sk.getDkskList();
        List<sinhVienResponseDTO> svResDTOList = dkskList.stream().map(i->(svDTOConverter.convertToSVResDTO(i.getSinhVien()))).toList();
        dto.setSinhVienThamGia(svResDTOList);

        return dto;
    }

    public SuKien convertResDTOToSuKien(suKienResponseDTO skResDTO){
        SuKien event = modelMapper.map(skResDTO, SuKien.class);
        event.setCreateAt(new Date(System.currentTimeMillis()));
        event.setStatus(EventStatus.fromDisplayName(skResDTO.getStatus()));

        return event;
    }
}

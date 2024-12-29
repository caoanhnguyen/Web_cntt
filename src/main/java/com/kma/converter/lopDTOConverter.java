package com.kma.converter;

import com.kma.models.lopDTO;
import com.kma.models.nhanVienResponseDTO;
import com.kma.repository.entities.Lop;
import com.kma.repository.entities.NhanVien;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class lopDTOConverter {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    nhanVienDTOConverter nvDTOConverter;

    public lopDTO convertToLopDTO(Lop lop){
        lopDTO dto = modelMapper.map(lop, lopDTO.class);
        NhanVien chuNhiem = lop.getChuNhiem();
        if(chuNhiem!=null){
            nhanVienResponseDTO chuNhiemDTO = nvDTOConverter.convertToNVResDTO(chuNhiem);
            dto.setChuNhiemDTO(chuNhiemDTO);
        }else{
            dto.setChuNhiemDTO(null);
        }
        return dto;
    }

    public void convertToLop(lopDTO dto, Lop lop){
        modelMapper.map(dto, lop);
        lop.setChuNhiem(null);
    }
}

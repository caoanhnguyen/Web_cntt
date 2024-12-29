package com.kma.converter;

import com.kma.models.phongBanResponseDTO;
import com.kma.repository.entities.PhongBan;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class phongBanDTOConverter {
    @Autowired
    ModelMapper modelMapper;

    public phongBanResponseDTO convertToPBResDTO(PhongBan fb){
        return modelMapper.map(fb, phongBanResponseDTO.class);
    }

    public void convertToPB(phongBanResponseDTO pbResDTO, PhongBan pb){
        modelMapper.map(pbResDTO, pb);
    }
}

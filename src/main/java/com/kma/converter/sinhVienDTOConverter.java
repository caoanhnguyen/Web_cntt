package com.kma.converter;

import com.kma.models.sinhVienResponseDTO;
import com.kma.repository.entities.SinhVien;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class sinhVienDTOConverter {
    @Autowired
    ModelMapper modelMapper;


    public sinhVienResponseDTO convertToSVResDTO(SinhVien sv){
        sinhVienResponseDTO svResDTO = modelMapper.map(sv, sinhVienResponseDTO.class);
        String gioiTinh = sv.getGioiTinh().getDisplayName();
        svResDTO.setGioiTinh(gioiTinh);
        String tenLop = sv.getLop().getTenLop();
        svResDTO.setTenLop(tenLop);

        return svResDTO;
    }
}

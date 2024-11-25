package com.kma.converter;

import com.kma.enums.GioiTinh;
import com.kma.models.sinhVienDTO;
import com.kma.models.sinhVienResponseDTO;
import com.kma.repository.entities.Lop;
import com.kma.repository.entities.SinhVien;
import com.kma.repository.lopRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class sinhVienDTOConverter {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    lopRepo lopRepo;


    public sinhVienResponseDTO convertToSVResDTO(SinhVien sv){
        sinhVienResponseDTO svResDTO = modelMapper.map(sv, sinhVienResponseDTO.class);
        String gioiTinh = sv.getGioiTinh().getDisplayName();
        svResDTO.setGioiTinh(gioiTinh);
        String tenLop = sv.getLop().getTenLop();
        svResDTO.setTenLop(tenLop);
        return svResDTO;
    }

    public sinhVienDTO convertToSVDTO(SinhVien sv){
        sinhVienDTO dto = modelMapper.map(sv, sinhVienDTO.class);
        String gioiTinh = sv.getGioiTinh().getDisplayName();
        dto.setGioiTinh(gioiTinh);
        String tenLop = sv.getLop().getTenLop();
        dto.setTenLop(tenLop);
        dto.setAvaDownloadUrl("/downloadProfile/"+sv.getAvaFileCode());
        return dto;
    }

    public SinhVien convertToSV(sinhVienDTO svDTO, String avaFileCode){
        SinhVien sv = modelMapper.map(svDTO, SinhVien.class);
        sv.setGioiTinh(GioiTinh.fromDisplayName(svDTO.getGioiTinh()));
        Lop lop = lopRepo.findByTenLop(svDTO.getTenLop());
        sv.setLop(lop);
        sv.setAvaFileCode(avaFileCode);
        sv.setDkskList(null);   // tạm thời để null

        return sv;
    }
}

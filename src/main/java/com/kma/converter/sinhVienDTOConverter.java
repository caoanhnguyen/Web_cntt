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
        Lop lop = sv.getLop();
        if(lop!=null){
            String tenLop = lop.getTenLop();
            svResDTO.setTenLop(tenLop);
        }else{
            svResDTO.setTenLop(null);
        }
        return svResDTO;
    }

    public sinhVienDTO convertToSVDTO(SinhVien sv){
        sinhVienDTO dto = modelMapper.map(sv, sinhVienDTO.class);
        String gioiTinh = sv.getGioiTinh().getDisplayName();
        dto.setGioiTinh(gioiTinh);
        Lop lop = sv.getLop();
        if(lop!=null){
            String tenLop = lop.getTenLop();
            dto.setTenLop(tenLop);
        }else{
            dto.setTenLop(null);
        }
        dto.setAvaDownloadUrl("/downloadProfile/"+sv.getAvaFileCode());
        return dto;
    }

    public void convertToSV(sinhVienDTO svDTO, SinhVien sv, String avaFileCode){
        modelMapper.map(svDTO, sv);
        if(svDTO.getGioiTinh()!= null){
            sv.setGioiTinh(GioiTinh.fromDisplayName(svDTO.getGioiTinh()));
        }
        if(svDTO.getTenLop()!=null){
            Lop lop = lopRepo.findByTenLop(svDTO.getTenLop());
            sv.setLop(lop);
        }else{
            sv.setLop(null);
        }
        sv.setAvaFileCode(avaFileCode);
    }
}

package com.kma.converter;

import com.kma.models.fileDTO;
import com.kma.models.monHocDTO;
import com.kma.models.monHocResponseDTO;
import com.kma.models.nhanVienResponseDTO;
import com.kma.repository.entities.MonHoc;
import com.kma.repository.entities.NhanVien;
import com.kma.repository.entities.TaiLieuMonHoc;
import com.kma.services.fileService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class monHocDTOConverter {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    fileService fileServ;

    public monHocDTO convertToMonHocDTO(MonHoc mh){
        monHocDTO mhDTO = modelMapper.map(mh, monHocDTO.class);

        List<TaiLieuMonHoc> taiLieuMonHocList = mh.getTaiLieuMHList();
        List<fileDTO> taiLieuMHList = fileServ.getListFileDTOByTL(taiLieuMonHocList);
        List<NhanVien> nhanVienList = mh.getNvList();
        List<nhanVienResponseDTO> nvList = new ArrayList<>();
        for(NhanVien nv: nhanVienList){
            nhanVienResponseDTO dto = modelMapper.map(nv, nhanVienResponseDTO.class);
            nvList.add(dto);
        }
        mhDTO.setNvList(nvList);
        mhDTO.setTaiLieuMHList(taiLieuMHList);

        return mhDTO;
    }

    public monHocResponseDTO convertToMonHocResDTO(MonHoc mh){
        monHocResponseDTO dto =  modelMapper.map(mh, monHocResponseDTO.class);
        dto.setDescription(mh.getMoTa());
        return dto;
    }
}

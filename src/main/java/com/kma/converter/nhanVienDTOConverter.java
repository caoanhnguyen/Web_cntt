package com.kma.converter;

import com.kma.models.monHocResponseDTO;
import com.kma.models.nhanVienDTO;
import com.kma.models.phongBanResponseDTO;
import com.kma.repository.entities.MonHoc;
import com.kma.repository.entities.NhanVien;
import com.kma.repository.monHocRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class nhanVienDTOConverter {

	@Autowired
	ModelMapper modelMapper;
	@Autowired
	monHocRepo mhRepo;

    public nhanVienDTO convertToNhanVienDTO(NhanVien nv) {
		nhanVienDTO dto =  modelMapper.map(nv, nhanVienDTO.class);
		// Set môn giảng dạy chính
		MonHoc monGiangDayChinh = mhRepo.findById(nv.getIdMonGiangDayChinh()).orElse(null);
		monHocResponseDTO monHocResDTO = new monHocResponseDTO();
		if(monGiangDayChinh!=null){
			monHocResDTO = modelMapper.map(monGiangDayChinh, monHocResponseDTO.class);
			dto.setMonGiangDayChinh(monHocResDTO);
		}else{
			dto.setMonGiangDayChinh(null);
		}
		// Set các môn liên quan
		List<monHocResponseDTO> monHocResponseDTOs = nv.getMonHocList().stream()
				.map(mh -> modelMapper.map(mh, monHocResponseDTO.class))
				.toList();
		dto.setCacMonLienQuan(monHocResponseDTOs);
		// Set phòng ban
		phongBanResponseDTO pbResDTO = modelMapper.map(nv.getPhongBan(), phongBanResponseDTO.class);
		dto.setPhongBan(pbResDTO);
		return dto;
	}
}

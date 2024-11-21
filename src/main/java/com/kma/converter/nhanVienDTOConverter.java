package com.kma.converter;

import com.kma.models.monHocResponseDTO;
import com.kma.models.nhanVienDTO;
import com.kma.repository.entities.NhanVien;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class nhanVienDTOConverter {

	@Autowired
	ModelMapper modelMapper;

    public nhanVienDTO convertToNhanVienDTO(NhanVien nv) {
		nhanVienDTO dto =  modelMapper.map(nv, nhanVienDTO.class);
		List<monHocResponseDTO> monHocResponseDTOs = nv.getMonHocList().stream()
				.map(mh -> modelMapper.map(mh, monHocResponseDTO.class))
				.toList();

		dto.setCacMonLienQuan(monHocResponseDTOs);
		return dto;
	}
}

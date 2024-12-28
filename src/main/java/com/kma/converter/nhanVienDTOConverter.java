package com.kma.converter;

import com.kma.enums.GioiTinh;
import com.kma.models.*;
import com.kma.repository.entities.*;
import com.kma.repository.monHocRepo;
import com.kma.repository.phongBanRepo;
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
	@Autowired
	phongBanRepo pbRepo;
	@Autowired
	monHocDTOConverter mhConverter;

    public nhanVienDTO convertToNhanVienDTO(NhanVien nv) {
		nhanVienDTO dto =  modelMapper.map(nv, nhanVienDTO.class);
		String gioiTinh = null;
		if(nv.getGioiTinh()!=null){
			gioiTinh = nv.getGioiTinh().getDisplayName();
		}
		dto.setGioiTinh(gioiTinh);
		// Set môn giảng dạy chính
		MonHoc monGiangDayChinh = null;
		if(nv.getIdMonGiangDayChinh()!=null){
			monGiangDayChinh = mhRepo.findById(nv.getIdMonGiangDayChinh()).orElse(null);
		}
		monHocResponseDTO monHocResDTO;
		if(monGiangDayChinh!=null){
			monHocResDTO = modelMapper.map(monGiangDayChinh, monHocResponseDTO.class);
			monHocResDTO.setDescription(monGiangDayChinh.getMoTa());
			dto.setMonGiangDayChinh(monHocResDTO);
		}else{
			dto.setMonGiangDayChinh(null);
		}
		// Set các môn liên quan
		List<monHocResponseDTO> monHocResponseDTOs = nv.getMonHocList().stream()
				.map(mhConverter::convertToMonHocResDTO)
				.toList();
		dto.setCacMonLienQuan(monHocResponseDTOs);
		// Set phòng ban
		if(nv.getPhongBan()!=null){
			phongBanResponseDTO pbResDTO = modelMapper.map(nv.getPhongBan(), phongBanResponseDTO.class);
			dto.setPhongBan(pbResDTO);
		}else{
			dto.setPhongBan(null);
		}

		dto.setAvaFileCode("/downloadProfile/"+nv.getAvaFileCode());
		return dto;
	}

	public nhanVienResponseDTO convertToNVResDTO(NhanVien nv){
		return modelMapper.map(nv, nhanVienResponseDTO.class);
	}

	public void convertNVReqToNV(nhanVienRequestDTO nvReqDTO, NhanVien nv, String avaFileCode){
		modelMapper.map(nvReqDTO, nv);
		if(nvReqDTO.getGioiTinh()!= null){
			nv.setGioiTinh(GioiTinh.fromDisplayName(nvReqDTO.getGioiTinh()));
		}
		String maPhongBan = nvReqDTO.getMaPhongBan();
		if(maPhongBan!=null && !maPhongBan.isEmpty()){
			PhongBan pb = pbRepo.findById(maPhongBan).orElse(null);
			nv.setPhongBan(pb);
		}else{
			nv.setPhongBan(null);
		}
		nv.setAvaFileCode(avaFileCode);
	}
}

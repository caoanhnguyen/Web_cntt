package com.kma.services;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.kma.models.nhanVienDTO;
import com.kma.models.nhanVienRequestDTO;
import com.kma.models.nhanVienResponseDTO;
import com.kma.models.paginationResponseDTO;
import org.springframework.web.multipart.MultipartFile;

public interface nhanVienService {

	nhanVienDTO getById(Integer idUser);

	paginationResponseDTO<nhanVienDTO> getAllNhanVien(Map<String, Object> params, int page, int size);

	paginationResponseDTO<nhanVienResponseDTO> getAllNhanVienSummary(Map<String, Object> params, int page, int size);

	void addNhanVien(MultipartFile file, nhanVienRequestDTO nvReqDTO, String userName) throws IOException;

	void updateNhanVien(Integer idUser, nhanVienRequestDTO nvReqDTO, MultipartFile file) throws IOException;

	void updateMGDC(Integer idUser, Integer idMGDC);

	void updateMonHocLienQuan(Integer idUser, List<Integer> idMonHocList);

	void deleteNhanVien(Integer idUser);

	boolean isOwner(Integer idUser, Integer nvId);
}

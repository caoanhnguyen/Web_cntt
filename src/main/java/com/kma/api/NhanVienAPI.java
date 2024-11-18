package com.kma.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kma.models.errorResponseDTO;
import com.kma.models.nhanVienDTO;
import com.kma.services.nhanVienService;

@RestController
public class NhanVienAPI {
	
	@Autowired
	nhanVienService nvServ;
	
	@GetMapping(value="api/nhanvien")
	public ResponseEntity<Object> getAllNhanVien(@RequestParam Map<String, Object> params){
		try {
			List<nhanVienDTO> DTO = nvServ.getAllNhanVien(params);
			return new ResponseEntity<>(DTO, HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			errorResponseDTO errorDTO = new errorResponseDTO();
			errorDTO.setError(e.getMessage());
			List<String> details = new ArrayList<>();
			details.add("An error occurred!");
			errorDTO.setDetails(details);
			
			return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}

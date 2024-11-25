package com.kma.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.kma.models.errorResponseDTO;
import com.kma.models.paginationResponseDTO;
import com.kma.models.sinhVienResponseDTO;
import com.kma.services.sinhVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class StudentAPI {
	@Autowired
	sinhVienService svServ;
	
	@GetMapping(value = "/api/students")
	public ResponseEntity<Object> getAllStudent(@RequestParam Map<String,Object> params,
											    @RequestParam(required = false, defaultValue = "0") int page,
											    @RequestParam(required = false, defaultValue = "10") int size) {
		try {
			paginationResponseDTO<sinhVienResponseDTO> DTO = svServ.getAllSinhVien(params, page, size);
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

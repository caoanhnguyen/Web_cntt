package com.kma.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.kma.models.postRequestDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kma.models.errorResponseDTO;
import com.kma.models.nhanVienDTO;
import com.kma.services.nhanVienService;

@RestController
public class NhanVienAPI {
	
	@Autowired
	nhanVienService nvServ;

	@GetMapping(value = "/api/nhanvien/{idUser}")
	public ResponseEntity<Object> getById(@PathVariable Integer idUser){
		try {
			nhanVienDTO DTO = nvServ.getById(idUser);
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
	
	@GetMapping(value="/api/nhanvien")
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

//	@PostMapping(value = "/api/nhanvien")
//	public 	ResponseEntity<Object> addNhanVien(@ModelAttribute postRequestDTO postRequestDTO){
//		try {
//
//			return ResponseEntity.ok("Add successful!");
//		} catch (Exception e) {
//			// TODO: handle exception
//			errorResponseDTO errorDTO = new errorResponseDTO();
//			errorDTO.setError(e.getMessage());
//			List<String> details = new ArrayList<>();
//			details.add("An error occurred!");
//			errorDTO.setDetails(details);
//
//			return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
//		}
//	}

	@DeleteMapping(value = "/api/nhanvien/{idUser}")
	public ResponseEntity<Object> deleteNhanVien(@PathVariable Integer idUser) {
		try {
			nvServ.deleteNhanVien(idUser);
			return ResponseEntity.ok("Delete successful!");
		} catch (EntityNotFoundException e) {
			// TODO: handle exception
			errorResponseDTO errorDTO = new errorResponseDTO();
			errorDTO.setError(e.getMessage());
			List<String> details = new ArrayList<>();
			details.add("User not found!");
			errorDTO.setDetails(details);

			return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
		}
	}
}

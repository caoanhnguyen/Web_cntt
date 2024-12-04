package com.kma.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.kma.models.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kma.services.nhanVienService;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class NhanVienAPI {
	
	@Autowired
	nhanVienService nvServ;

	@GetMapping(value = "/api/nhanvien/{idUser}")
	public ResponseEntity<Object> getById(@PathVariable Integer idUser){
		try {
			nhanVienDTO DTO = nvServ.getById(idUser);
			return new ResponseEntity<>(DTO, HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			// TODO: handle exception
			errorResponseDTO errorDTO = new errorResponseDTO();
			errorDTO.setError(e.getMessage());
			List<String> details = new ArrayList<>();
			details.add("Employee not found!");
			errorDTO.setDetails(details);

			return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
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
	public ResponseEntity<Object> getAllNhanVien(@RequestParam Map<String, Object> params,
												 @RequestParam(required = false, defaultValue = "0") int page,
												 @RequestParam(required = false, defaultValue = "10") int size){
		try {
			paginationResponseDTO<nhanVienDTO> DTO = nvServ.getAllNhanVien(params, page, size);
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

	@PostMapping(value = "/api/nhanvien")
	public 	ResponseEntity<Object> addNhanVien(@RequestParam(value = "file", required = false) MultipartFile file,
											   @ModelAttribute nhanVienRequestDTO nvReqDTO){
		try {
			nvServ.addNhanVien(file, nvReqDTO);
			return ResponseEntity.ok("Add successful!");
		} catch (Exception e) {
			// TODO: handle exception
			errorResponseDTO errorDTO = new errorResponseDTO();
			errorDTO.setError(e.getMessage());
			List<String> details = new ArrayList<>();
			details.add("An error occurred!");
			errorDTO.setDetails(details);

			return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping(value = "/api/nhanvien/{idUser}")
	public ResponseEntity<Object> updateNhanVien(@PathVariable Integer idUser,
												 @ModelAttribute nhanVienRequestDTO nvReqDTO,
												 @RequestParam(value = "file", required = false) MultipartFile file) {
		try {
			nvServ.updateNhanVien(idUser, nvReqDTO, file);
			return ResponseEntity.ok("Update successfully!");
		} catch (EntityNotFoundException e) {
			// TODO: handle exception
			errorResponseDTO errorDTO = new errorResponseDTO();
			errorDTO.setError(e.getMessage());
			List<String> details = new ArrayList<>();
			details.add("Employee not found!");
			errorDTO.setDetails(details);

			return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
		}
	}

	@DeleteMapping(value = "/api/nhanvien/{idUser}")
	public ResponseEntity<Object> deleteNhanVien(@PathVariable Integer idUser) {
		try {
			nvServ.deleteNhanVien(idUser);
			return ResponseEntity.ok("Delete successfully!");
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

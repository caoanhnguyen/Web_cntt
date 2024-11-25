package com.kma.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.kma.models.*;
import com.kma.services.sinhVienService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
public class SinhVienAPI {
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

	@GetMapping(value = "/api/students/{maSinhVien}")
	public ResponseEntity<Object> getById(@PathVariable String maSinhVien){
		try {
			sinhVienDTO DTO = svServ.findById(maSinhVien.toUpperCase());
			return new ResponseEntity<>(DTO, HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			// TODO: handle exception
			errorResponseDTO errorDTO = new errorResponseDTO();
			errorDTO.setError(e.getMessage());
			List<String> details = new ArrayList<>();
			details.add("Student not found!");
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

	@PostMapping(value = "/api/students")
	public ResponseEntity<Object> addSinhVien(@RequestParam(value = "file", required = false) MultipartFile file,
											  @ModelAttribute sinhVienDTO svDTO){
		try {
			svServ.addSinhVien(file, svDTO);
			return ResponseEntity.ok("Add successful!");
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

	@PutMapping(value = "/api/students/{maSinhVien}")
	public ResponseEntity<Object> updateSinhVien(@PathVariable String maSinhVien,
												 @ModelAttribute sinhVienDTO svDTO,
												 @RequestParam(value = "file", required = false) MultipartFile file) {
		try {
			svServ.updateSinhVien(maSinhVien.toUpperCase(), svDTO, file);
			return ResponseEntity.ok("Update successful!");
		} catch (EntityNotFoundException e) {
			// TODO: handle exception
			errorResponseDTO errorDTO = new errorResponseDTO();
			errorDTO.setError(e.getMessage());
			List<String> details = new ArrayList<>();
			details.add("Student not found!");
			errorDTO.setDetails(details);

			return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
		}
	}

	@DeleteMapping(value = "/api/students/{maSinhVien}")
	public ResponseEntity<Object> deleteSinhVien(@PathVariable String maSinhVien) {
		try {
			svServ.deleteSinhVien(maSinhVien);
			return ResponseEntity.ok("Delete successful!");
		} catch (EntityNotFoundException e) {
			// TODO: handle exception
			errorResponseDTO errorDTO = new errorResponseDTO();
			errorDTO.setError(e.getMessage());
			List<String> details = new ArrayList<>();
			details.add("Student not found!");
			errorDTO.setDetails(details);

			return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
		}
	}
}

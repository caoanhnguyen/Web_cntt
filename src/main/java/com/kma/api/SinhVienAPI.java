package com.kma.api;

import java.util.List;
import java.util.Map;

import com.kma.models.*;
import com.kma.services.sinhVienService;
import com.kma.utilities.buildErrorResUtil;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/students")
public class SinhVienAPI {
	@Autowired
	sinhVienService svServ;
	@Autowired
	buildErrorResUtil buildErrorResUtil;

	@GetMapping(value = "")
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_STUDENT')")
	public ResponseEntity<Object> getAllStudent(@RequestParam Map<String,Object> params,
											    @RequestParam(required = false, defaultValue = "0") int page,
											    @RequestParam(required = false, defaultValue = "10") int size) {
		try {
			paginationResponseDTO<sinhVienResponseDTO> DTO = svServ.getAllSinhVien(params, page, size);
			return new ResponseEntity<>(DTO, HttpStatus.OK);
		} catch (Exception e) {
			errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
			return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(value = "/{maSinhVien}")
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_STUDENT')")
	public ResponseEntity<Object> getById(@PathVariable String maSinhVien){
		try {
			sinhVienDTO DTO = svServ.findById(maSinhVien.toUpperCase());
			return new ResponseEntity<>(DTO, HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "Student not found!");
			return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
			return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(value = "/participated_events/{maSinhVien}")
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_STUDENT')")
	public ResponseEntity<Object> getAllParticipatedEvent(@PathVariable String maSinhVien){
		try {
			List<suKienResponseDTO> DTO = svServ.getAllParicipatedEvent(maSinhVien.toUpperCase());
			return new ResponseEntity<>(DTO, HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "Student not found!");
			return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
			return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(value = "")
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
	public ResponseEntity<Object> addSinhVien(@RequestParam(value = "file", required = false) MultipartFile file,
											  @ModelAttribute sinhVienDTO svDTO){
		try {
			svServ.addSinhVien(file, svDTO);
			return ResponseEntity.ok("Add successfully!");
		} catch (IllegalArgumentException | DataIntegrityViolationException e) {
			errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "Student id already exists!");
			return new ResponseEntity<>(errorDTO, HttpStatus.CONFLICT);
		} catch (Exception e) {
			errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
			return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping(value = "/{maSinhVien}")
	@PreAuthorize("hasRole('ROLE_ADMIN') OR @svServ.isOwner(#maSinhVien, principal.sinhVien.maSinhVien) OR hasRole('ROLE_EMPLOYEE')")
	public ResponseEntity<Object> updateSinhVien(@PathVariable String maSinhVien,
												 @ModelAttribute sinhVienDTO svDTO,
												 @RequestParam(value = "file", required = false) MultipartFile file) {
		try {
			svServ.updateSinhVien(maSinhVien.toUpperCase(), svDTO, file);
			return ResponseEntity.ok("Update successfully!");
		} catch (IllegalArgumentException | DataIntegrityViolationException e) {
			errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "Mã sinh viên hoặc CCCD đã tồn tại!");
			return new ResponseEntity<>(errorDTO, HttpStatus.CONFLICT);
		} catch (EntityNotFoundException e) {
			errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "Student not found!");
			return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
			return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping(value = "/{maSinhVien}")
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
	public ResponseEntity<Object> deleteSinhVien(@PathVariable String maSinhVien) {
		try {
			svServ.deleteSinhVien(maSinhVien);
			return ResponseEntity.ok("Delete successfully!");
		} catch (EntityNotFoundException e) {
			errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "Student not found!");
			return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
			return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}

package com.kma.api;

import java.util.List;
import java.util.Map;

import com.kma.models.*;
import com.kma.utilities.buildErrorResUtil;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.kma.services.nhanVienService;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/nhanvien")
public class NhanVienAPI {
	
	@Autowired
	nhanVienService nvServ;
	@Autowired
	buildErrorResUtil buildErrorResUtil;

	@GetMapping(value = "/{idUser}")
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_STUDENT')")
	public ResponseEntity<Object> getById(@PathVariable Integer idUser){
		try {
			nhanVienDTO DTO = nvServ.getById(idUser);
			return new ResponseEntity<>(DTO, HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "Employee not found!");
			return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
			return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(value="")
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
	public ResponseEntity<Object> getAllNhanVien(@RequestParam Map<String, Object> params,
												 @RequestParam(required = false, defaultValue = "0") int page,
												 @RequestParam(required = false, defaultValue = "10") int size){
		try {
			paginationResponseDTO<nhanVienDTO> DTO = nvServ.getAllNhanVien(params, page, size);
			return new ResponseEntity<>(DTO, HttpStatus.OK);
		} catch (Exception e) {
			errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
			return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(value = "")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public 	ResponseEntity<Object> addNhanVien(@RequestParam(value = "file", required = false) MultipartFile file,
											   @ModelAttribute nhanVienRequestDTO nvReqDTO,
											   @RequestParam(value = "userName") String userName){
		try {
			nvServ.addNhanVien(file, nvReqDTO, userName);
			return ResponseEntity.ok("Add successfully!");
		} catch (IllegalArgumentException | DataIntegrityViolationException e) {
				errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "Mã nhân viên hoặc tên đăng nhập đã tồn tại!");
				return new ResponseEntity<>(errorDTO, HttpStatus.CONFLICT);
		} catch (Exception e) {
			errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "Employee not found!");
			return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping(value = "/{idUser}")
	@PreAuthorize("@nvServ.isOwner(#idUser, principal.nhanVien.idUser) OR hasRole('ROLE_ADMIN')")
	public ResponseEntity<Object> updateNhanVien(@PathVariable Integer idUser,
												 @ModelAttribute nhanVienRequestDTO nvReqDTO,
												 @RequestParam(value = "file", required = false) MultipartFile file) {
		try {
			nvServ.updateNhanVien(idUser, nvReqDTO, file);
			return ResponseEntity.ok("Update successfully!");
		} catch (EntityNotFoundException e) {
			errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "Employee not found!");
			return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
			return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PatchMapping(value = "/main_subject/{idUser}")
	@PreAuthorize("@nvServ.isOwner(#idUser, principal.nhanVien.idUser) OR hasRole('ROLE_ADMIN')")
	public ResponseEntity<Object> updateMGDC(@PathVariable Integer idUser,
											 @RequestParam Integer idMGDC) {
		try {
			nvServ.updateMGDC(idUser, idMGDC);
			return ResponseEntity.ok("Update successfully!");
		} catch (EntityNotFoundException e) {
			errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "Employee not found!");
			return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
			return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PatchMapping(value = "/related_subject/{idUser}")
	@PreAuthorize("@nvServ.isOwner(#idUser, principal.nhanVien.idUser) OR hasRole('ROLE_ADMIN')")
	public ResponseEntity<Object> updateMonHocLienQuan(@PathVariable Integer idUser,
											 		   @RequestParam(value = "idMonHoc") List<Integer> idMonHocList) {
		try {
			nvServ.updateMonHocLienQuan(idUser, idMonHocList);
			return ResponseEntity.ok("Update successfully!");
		} catch (EntityNotFoundException e) {
			errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "Employee not found!");
			return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
			return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping(value = "/{idUser}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Object> deleteNhanVien(@PathVariable Integer idUser) {
		try {
			nvServ.deleteNhanVien(idUser);
			return ResponseEntity.ok("Delete successfully!");
		} catch (EntityNotFoundException e) {
			errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "Employee not found!");
			return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
			return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}

package com.kma.api;

import com.kma.enums.SubjectCategory;
import com.kma.models.errorResponseDTO;
import com.kma.models.monHocDTO;
import com.kma.models.monHocResponseDTO;
import com.kma.services.monHocService;
import com.kma.utilities.buildErrorResUtil;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/monhoc")
public class MonHocAPI {

    @Autowired
    monHocService monHocServ;
    @Autowired
    buildErrorResUtil buildErrorResUtil ;

    @GetMapping(value = "/grouped")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_STUDENT')")
    public ResponseEntity<?> getSubjectsOrderByCategory() {
        try {
            Map<SubjectCategory, List<monHocResponseDTO>> DTO = monHocServ.getGroupedSubjects();
            // Trả về danh sách môn học
            return new ResponseEntity<>(DTO, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "Invalid category name. Valid values: GENERAL, FOUNDATION, SPECIALIZED.");
            return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
            return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/{idUser}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_STUDENT')")
    public ResponseEntity<Object> getById(@PathVariable Integer idUser){
        try {
            monHocDTO DTO = monHocServ.getById(idUser);
            return new ResponseEntity<>(DTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "Subject not found!");
            return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
            return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_STUDENT')")
    public ResponseEntity<Object> getAllMonHoc(@RequestParam Map<Object,Object> params){
        try {
            List<monHocDTO> DTO = monHocServ.getAllMonHoc(params);
            return new ResponseEntity<>(DTO, HttpStatus.OK);
        } catch (NumberFormatException e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "Điền sai số tín chỉ!");
            return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
            return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<Object> addMonHoc(@RequestParam(value = "file", required = false) List<MultipartFile> files,
                                            @ModelAttribute monHocDTO mhDTO) {
        try {
            monHocServ.addMonHoc(files, mhDTO);
            return ResponseEntity.ok("Add successfully!");
        } catch (Exception e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
            return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/{idMonHoc}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<Object> updatePost(@PathVariable Integer idMonHoc,
                                             @ModelAttribute monHocDTO monHocDTO,
                                             @RequestParam(value = "file", required = false) List<MultipartFile> files,
                                             @RequestParam(value = "deleteFileIds", required = false) List<Integer> deleteFileIds) {

        try {
            monHocServ.updateMonHoc(idMonHoc,monHocDTO, files, deleteFileIds);
            return ResponseEntity.ok("Update successfully!");
        } catch (EntityNotFoundException e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "Subject not found!");
            return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
            return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/{idMonHoc}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<Object> deleteMonHoc(@PathVariable Integer idMonHoc) {
        try {
            monHocServ.deleteMonHoc(idMonHoc);
            return ResponseEntity.ok("Delete successfully!");
        } catch (EntityNotFoundException e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "Subject not found!");
            return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
            return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

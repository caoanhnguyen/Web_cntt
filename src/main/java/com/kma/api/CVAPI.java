package com.kma.api;

import com.kma.models.*;
import com.kma.services.CVService;
import com.kma.services.nhanVienService;
import com.kma.utilities.buildErrorResUtil;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class CVAPI {
    @Autowired
    buildErrorResUtil buildErrorResUtil;
    @Autowired
    CVService cvServ;
    @Autowired
    nhanVienService nvServ;

    @GetMapping(value="/public/nhanvien")
    public ResponseEntity<Object> getAllNhanVienSummary(@RequestParam Map<String, Object> params,
                                                        @RequestParam(required = false, defaultValue = "0") int page,
                                                        @RequestParam(required = false, defaultValue = "10") int size){
        try {
            paginationResponseDTO<nhanVienResponseDTO> DTO = nvServ.getAllNhanVienSummary(params, page, size);
            return new ResponseEntity<>(DTO, HttpStatus.OK);
        } catch (Exception e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
            return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/public/nhanvien/{idUser}/cv")
    public ResponseEntity<Object> getById(@PathVariable Integer idUser){
        try {
            CVDTO DTO = cvServ.getByUserId(idUser);
            return new ResponseEntity<>(DTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "Employee not found!");
            return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
            return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/nhanvien/{idUser}/cv")
    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<Object> addCV(@PathVariable Integer idUser,
                                         @ModelAttribute CVDTO cvDTO){
        try {
            cvServ.addCV(cvDTO, idUser);
            return ResponseEntity.ok("Add successfully!");
        } catch (Exception e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "Employee not found!");
            return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/nhanvien/cv/{CVId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') OR @cvServ.isOwner(#CVId, principal.nhanVien.idUser)")
    public ResponseEntity<Object> updateCV(@PathVariable Integer CVId,
                                            @ModelAttribute CVDTO cvDTO){
        try {
            cvServ.updateCV(CVId, cvDTO);
            return ResponseEntity.ok("Update successfully!");
        } catch (Exception e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "CV not found!");
            return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/nhanvien/cv/{CVId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') OR @cvServ.isOwner(#CVId, principal.nhanVien.idUser)")
    public ResponseEntity<Object> deleteCV(@PathVariable Integer CVId){
        try {
            cvServ.deleteCV(CVId);
            return ResponseEntity.ok("Delete successfully!");
        } catch (Exception e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "CV not found!");
            return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
        }
    }
}

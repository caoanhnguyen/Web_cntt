package com.kma.api;

import com.kma.models.CVDTO;
import com.kma.models.errorResponseDTO;
import com.kma.services.CVService;
import com.kma.utilities.buildErrorResUtil;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CVAPI {
    @Autowired
    buildErrorResUtil buildErrorResUtil;
    @Autowired
    CVService cvServ;

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
    public 	ResponseEntity<Object> addCV(@PathVariable Integer idUser,
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
    @PreAuthorize("@cvServ.isOwner(#CVId, principal.nhanVien.idUser) OR hasRole('ROLE_ADMIN')")
    public 	ResponseEntity<Object> updateCV(@PathVariable Integer CVId,
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
    @PreAuthorize("@cvServ.isOwner(#CVId, principal.nhanVien.idUser) OR hasRole('ROLE_ADMIN')")
    public 	ResponseEntity<Object> deleteCV(@PathVariable Integer CVId){
        try {
            cvServ.deleteCV(CVId);
            return ResponseEntity.ok("Delete successfully!");
        } catch (Exception e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "CV not found!");
            return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
        }
    }
}

package com.kma.api;

import com.kma.models.errorResponseDTO;
import com.kma.models.registrationDTO;
import com.kma.repository.entities.SuKien;
import com.kma.services.dangKySKService;
import com.kma.utilities.SlugUtils;
import com.kma.utilities.buildErrorResUtil;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class EventRegistrationAPI {

    @Autowired
    dangKySKService dkskServ;
    @Autowired
    buildErrorResUtil buildErrorResUtil;

    @PostMapping(value = "/api/registration")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public ResponseEntity<Object> registration(@ModelAttribute registrationDTO regisDTO){
        try {
            dkskServ.eventRegistration(regisDTO);
            return ResponseEntity.ok("Register successfully!");
        } catch (EntityNotFoundException e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "Student or event not found!");
            return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
            return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/api/{eventId}/export-students")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<byte[]> exportDanhSachSinhVien(@PathVariable Integer eventId) {
        try {
            SuKien event = dkskServ.getEventById(eventId);
            byte[] excelData = dkskServ.exportDanhSachSinhVien(eventId);

            // Tạo tên file dựa trên tên sự kiện
            String fileName = "danh_sach_sv_sk_" + SlugUtils.toSlug(event.getEventName()) + ".xlsx";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", fileName);

            return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

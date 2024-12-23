package com.kma.api;

import com.kma.models.errorResponseDTO;
import com.kma.models.registrationDTO;
import com.kma.services.dangKySKService;
import com.kma.utilities.buildErrorResUtil;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
}

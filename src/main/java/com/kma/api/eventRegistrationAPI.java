package com.kma.api;

import com.kma.models.errorResponseDTO;
import com.kma.models.registrationDTO;
import com.kma.models.sinhVienResponseDTO;
import com.kma.services.dangKySKService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class eventRegistrationAPI {
    @Autowired
    dangKySKService dkskServ;

    @PostMapping(value = "/api/registration")
    public ResponseEntity<Object> registration(@ModelAttribute registrationDTO regisDTO){
        try {
            dkskServ.eventRegistration(regisDTO);
            return ResponseEntity.ok("Register successfully!");
        } catch (EntityNotFoundException e) {
            // TODO: handle exception
            errorResponseDTO errorDTO = new errorResponseDTO();
            errorDTO.setError(e.getMessage());
            List<String> details = new ArrayList<>();
            details.add("Student or event not found!");
            errorDTO.setDetails(details);

            return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }
    }
}

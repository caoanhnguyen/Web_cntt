package com.kma.api;

import com.kma.models.*;
import com.kma.services.lopService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class lopAPI {

    @Autowired
    lopService lopServ;

    @GetMapping(value = "/api/class")
    public ResponseEntity<Object> getAllClass(@RequestParam Map<String,Object> params,
                                              @RequestParam(required = false, defaultValue = "0") int page,
                                              @RequestParam(required = false, defaultValue = "10") int size) {
        try {
            paginationResponseDTO<lopDTO> DTO = lopServ.getAllClass(params, page, size);
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

    @GetMapping(value = "/api/class/{idLop}")
    public ResponseEntity<Object> getById(@PathVariable Integer idLop){
        try {
            lopDTO DTO = lopServ.findById(idLop);
            return new ResponseEntity<>(DTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            // TODO: handle exception
            errorResponseDTO errorDTO = new errorResponseDTO();
            errorDTO.setError(e.getMessage());
            List<String> details = new ArrayList<>();
            details.add("Class not found!");
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

    @PostMapping(value = "/api/class")
    public ResponseEntity<Object> addLop(@ModelAttribute lopDTO DTO){
        try {
            lopServ.addLop(DTO);
            return ResponseEntity.ok("Add successfully!");
        } catch (EntityExistsException e) {
            // TODO: handle exception
            errorResponseDTO errorDTO = new errorResponseDTO();
            errorDTO.setError(e.getMessage());
            List<String> details = new ArrayList<>();
            details.add("Class name already exists!");
            errorDTO.setDetails(details);

            return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
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

    @PutMapping(value = "/api/class/{idLop}")
    public ResponseEntity<Object> updateSinhVien(@PathVariable Integer idLop,
                                                 @ModelAttribute lopRequestDTO DTO) {
        try {
            lopServ.updateLop(idLop, DTO);
            return ResponseEntity.ok("Update successfully!");
        } catch (EntityNotFoundException e) {
            // TODO: handle exception
            errorResponseDTO errorDTO = new errorResponseDTO();
            errorDTO.setError(e.getMessage());
            List<String> details = new ArrayList<>();
            details.add("Class not found!");
            errorDTO.setDetails(details);

            return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }
    }



    @DeleteMapping(value = "/api/class/{idLop}")
    public ResponseEntity<Object> deleteClass(@PathVariable Integer idLop) {
        try {
            lopServ.deleteLop(idLop);
            return ResponseEntity.ok("Delete successfully!");
        } catch (EntityNotFoundException e) {
            // TODO: handle exception
            errorResponseDTO errorDTO = new errorResponseDTO();
            errorDTO.setError(e.getMessage());
            List<String> details = new ArrayList<>();
            details.add("Class not found!");
            errorDTO.setDetails(details);

            return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }
    }
}

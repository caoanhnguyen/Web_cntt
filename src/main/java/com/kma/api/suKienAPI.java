package com.kma.api;

import com.kma.models.*;
import com.kma.services.suKienService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class suKienAPI {
    @Autowired
    suKienService skServ;

    @GetMapping(value = "/api/sukien")
    public ResponseEntity<Object> getAllSuKien(@RequestParam Map<String,Object> params,
                                               @RequestParam(required = false, defaultValue = "0") int page,
                                               @RequestParam(required = false, defaultValue = "10") int size){
        try {
            paginationResponseDTO<suKienResponseDTO> DTO =skServ.getAllEvent(params, page, size);
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

    @GetMapping(value = "/api/sukien/{eventId}")
    public ResponseEntity<Object> findById(@PathVariable Integer eventId){
        try {
            suKienDTO DTO = skServ.findById(eventId);
            return new ResponseEntity<>(DTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            // TODO: handle exception
            errorResponseDTO errorDTO = new errorResponseDTO();
            errorDTO.setError(e.getMessage());
            List<String> details = new ArrayList<>();
            details.add("Event not found!");
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

    @PostMapping(value = "/api/sukien")
    public ResponseEntity<Object> addEvent(@RequestParam(value = "file", required = false) List<MultipartFile> files,
                                          @ModelAttribute suKienResponseDTO skResDTO) {
        try {
            skServ.addEvent(files, skResDTO);
            return ResponseEntity.ok("Add successful!");
        } catch (IllegalArgumentException e) {
            // TODO: handle exception
            errorResponseDTO errorDTO = new errorResponseDTO();
            errorDTO.setError(e.getMessage());
            List<String> details = new ArrayList<>();
            details.add("Event not exist!");
            errorDTO.setDetails(details);

            return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }
    }

    @PutMapping(value = "/api/sukien/{eventId}")
    public ResponseEntity<Object> updatePost(@PathVariable Integer eventId,
                                             @ModelAttribute suKienResponseDTO skResDTO,
                                             @RequestParam(value = "file", required = false) List<MultipartFile> files,
                                             @RequestParam(value = "deleteFileIds", required = false) List<Integer> deleteFileIds) {

        try {
            skServ.updateEvent(eventId, skResDTO, files, deleteFileIds);
            return ResponseEntity.ok("Update successful!");
        } catch (EntityNotFoundException e) {
            // TODO: handle exception
            errorResponseDTO errorDTO = new errorResponseDTO();
            errorDTO.setError(e.getMessage());
            List<String> details = new ArrayList<>();
            details.add("Event not found!");
            errorDTO.setDetails(details);

            return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }
    }

    @DeleteMapping(value = "/api/sukien/{eventId}")
    public ResponseEntity<Object> deletePost(@PathVariable Integer eventId) {
        try {
            skServ.deleteEvent(eventId);
            return ResponseEntity.ok("Delete successful!");
        } catch (EntityNotFoundException e) {
            // TODO: handle exception
            errorResponseDTO errorDTO = new errorResponseDTO();
            errorDTO.setError(e.getMessage());
            List<String> details = new ArrayList<>();
            details.add("Event not found!");
            errorDTO.setDetails(details);

            return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }
    }
}

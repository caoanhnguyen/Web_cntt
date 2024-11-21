package com.kma.api;

import com.kma.models.errorResponseDTO;
import com.kma.models.monHocDTO;
import com.kma.models.postRequestDTO;
import com.kma.services.monHocService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class monHocAPI {

    @Autowired
    monHocService monHocServ;

    @GetMapping(value = "/api/monhoc")
    public ResponseEntity<Object> getAllMonHoc(@RequestParam Map<Object,Object> params){
        try {
            List<monHocDTO> DTO = monHocServ.getAllMonHoc(params);
            return new ResponseEntity<>(DTO, HttpStatus.OK);
        } catch (NumberFormatException e) {
            // TODO: handle exception
            errorResponseDTO errorDTO = new errorResponseDTO();
            errorDTO.setError(e.getMessage());
            List<String> details = new ArrayList<>();
            details.add("Điền sai số tín chỉ!");
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

    @PostMapping(value = "/api/monhoc")
    public ResponseEntity<Object> addMonHoc(@RequestParam(value = "file", required = false) List<MultipartFile> files,
                                          @ModelAttribute monHocDTO mhDTO) throws IOException {
        try {
            monHocServ.addMonHoc(files, mhDTO);
            return ResponseEntity.ok("Add successful!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }
    }

    @PutMapping(value = "/api/monhoc/{idMonHoc}")
    public ResponseEntity<Object> updatePost(@PathVariable Integer idMonHoc,
                                             @ModelAttribute monHocDTO monHocDTO,
                                             @RequestParam(value = "file", required = false) List<MultipartFile> files,
                                             @RequestParam(value = "deleteFileIds", required = false) List<Integer> deleteFileIds) {

        try {
            monHocServ.updateMonHoc(idMonHoc,monHocDTO, files, deleteFileIds);
            return ResponseEntity.ok("Update successful!");
        } catch (EntityNotFoundException e) {
            // TODO: handle exception
            errorResponseDTO errorDTO = new errorResponseDTO();
            errorDTO.setError(e.getMessage());
            List<String> details = new ArrayList<>();
            details.add("Subject not found!");
            errorDTO.setDetails(details);

            return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }
    }

    @DeleteMapping(value = "api/monhoc/{idMonHoc}")
    public ResponseEntity<Object> deleteMonHoc(@PathVariable Integer idMonHoc) {
        try {
            monHocServ.deleteMonHoc(idMonHoc);
            return ResponseEntity.ok("Delete successful!");
        } catch (EntityNotFoundException e) {
            // TODO: handle exception
            errorResponseDTO errorDTO = new errorResponseDTO();
            errorDTO.setError(e.getMessage());
            List<String> details = new ArrayList<>();
            details.add("Subject not found!");
            errorDTO.setDetails(details);

            return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }
    }

}

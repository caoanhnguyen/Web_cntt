package com.kma.api;

import com.kma.models.*;
import com.kma.services.suKienService;
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
@RequestMapping("/api")
public class SuKienAPI {
    @Autowired
    suKienService skServ;
    @Autowired
    com.kma.utilities.buildErrorResUtil buildErrorResUtil;

    @GetMapping(value = "/public/sukien")
    public ResponseEntity<Object> getAllSuKien(@RequestParam(defaultValue = "") Map<String,Object> params,
                                               @RequestParam(required = false, defaultValue = "0") int page,
                                               @RequestParam(required = false, defaultValue = "10") int size){
        try {
            paginationResponseDTO<suKienResponseDTO> DTO =skServ.getAllEvent(params, page, size);
            return new ResponseEntity<>(DTO, HttpStatus.OK);
        } catch (Exception e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
            return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/sukien/participation_list/{eventId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_STUDENT')")
    public ResponseEntity<Object> getAllSVInEvent(@PathVariable Integer eventId,
                                                  @RequestParam(value = "searchTerm", required = false, defaultValue = "") String searchTerm,
                                                  @RequestParam(required = false, defaultValue = "0") int page,
                                                  @RequestParam(required = false, defaultValue = "5") int size){
        try {
            paginationResponseDTO<sinhVienResponseDTO> DTO = skServ.getAllSVInEvent(eventId, searchTerm, page, size);
            return new ResponseEntity<>(DTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "Event not found!");
            return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
            return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping(value = "/public/sukien/{eventId}")
    public ResponseEntity<Object> getById(@PathVariable Integer eventId){
        try {
            suKienDTO DTO = skServ.findById(eventId);
            return new ResponseEntity<>(DTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "Event not found!");
            return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
            return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/sukien")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<Object> addEvent(@RequestParam(value = "file", required = false) List<MultipartFile> files,
                                          @ModelAttribute suKienResponseDTO skResDTO) {
        try {
            skServ.addEvent(files, skResDTO);
            return ResponseEntity.ok("Add successfully!");
        } catch (IllegalArgumentException e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "Event not found!");
            return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
            return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/sukien/{eventId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<Object> updatePost(@PathVariable Integer eventId,
                                             @ModelAttribute suKienResponseDTO skResDTO,
                                             @RequestParam(value = "file", required = false) List<MultipartFile> files,
                                             @RequestParam(value = "deleteFileIds", required = false) List<Integer> deleteFileIds) {

        try {
            skServ.updateEvent(eventId, skResDTO, files, deleteFileIds);
            return ResponseEntity.ok("Update successfully!");
        } catch (EntityNotFoundException e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "Event not found!");
            return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
            return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/sukien/{eventId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<Object> deletePost(@PathVariable Integer eventId) {
        try {
            skServ.deleteEvent(eventId);
            return ResponseEntity.ok("Delete successfully!");
        } catch (EntityNotFoundException e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "Event not found!");
            return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
            return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

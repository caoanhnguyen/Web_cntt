package com.kma.api;

import com.kma.models.*;
import com.kma.services.sliderService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SliderAPI {
    @Autowired
    com.kma.utilities.buildErrorResUtil buildErrorResUtil;
    @Autowired
    sliderService sliderServ;


    @GetMapping(value="/public/sliders")
    public ResponseEntity<Object> getAllSlider(@RequestParam(defaultValue = "date") String sort,
                                               @RequestParam(defaultValue = "desc") String order,
                                               @RequestParam(required = false, defaultValue = "0") int page,
                                               @RequestParam(required = false, defaultValue = "10") int size){
        try {
            paginationResponseDTO<sliderDTO> DTO = sliderServ.getAllSlider(page, size, sort, order);
            return new ResponseEntity<>(DTO, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
            return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
            return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value="/public/latest_sliders")
    public ResponseEntity<Object> getLatestSlider(){
        try {
            List<sliderDTO> DTO = sliderServ.getLatestSlider();
            return new ResponseEntity<>(DTO, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
            return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
            return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value="/public/sliders/{sliderId}")
    public ResponseEntity<Object> getById(@PathVariable Integer sliderId){
        try {
            sliderDTO DTO = sliderServ.getById(sliderId);
            return new ResponseEntity<>(DTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "Slider not found!");
            return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
            return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping(value = "/sliders")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> addSlider(@ModelAttribute sliderDTO DTO) {
        try {
            sliderServ.addSlider(DTO);
            return ResponseEntity.ok("Add successfully!");
        } catch (Exception e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
            return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/sliders/{sliderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> updateSlider(@PathVariable Integer sliderId,
                                                @ModelAttribute sliderDTO DTO) {

        try {
            sliderServ.updateSlider(sliderId, DTO);
            return ResponseEntity.ok("Update successfully!");
        } catch (EntityNotFoundException e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "Slider not found!");
            return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
            return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/sliders/{sliderId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> deleteSlider(@PathVariable Integer sliderId) {
        try {
            sliderServ.deleteSlider(sliderId);
            return ResponseEntity.ok("Delete successfully!");
        } catch (EntityNotFoundException e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "Slider not found!");
            return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
            return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

package com.kma.api;


import com.kma.models.*;
import com.kma.services.discussionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DiscussionAPI {

    @Autowired
    discussionService discussionServ;


    @GetMapping(value = "/discussion/{discussionId}")
    public ResponseEntity<Object> getById(@PathVariable Integer discussionId){
        try {
            discussionDTO DTO = discussionServ.getById(discussionId);
            return new ResponseEntity<>(DTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            // TODO: handle exception
            errorResponseDTO errorDTO = new errorResponseDTO();
            errorDTO.setError(e.getMessage());
            List<String> details = new ArrayList<>();
            details.add("Discussion not found!");
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

    @GetMapping(value="/discussion")
    public ResponseEntity<Object> getAllDiscussion(@RequestParam Map<String,Object> params,
                                                   @RequestParam(required = false, defaultValue = "0") int page,
                                                   @RequestParam(required = false, defaultValue = "10") int size){
        try {
            paginationResponseDTO<discussionResponseDTO> DTO =  discussionServ.getAllDiscussion(params, page, size);
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

    @GetMapping(value = "/discussion/latest")
    public ResponseEntity<Object> getLatestDiscussions() {
        try {
            List<discussionResponseDTO> DTO = discussionServ.getLatestDiscussions();
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

    @PostMapping(value = "/discussion")
    public ResponseEntity<Object> addPost(@ModelAttribute discussionRequestDTO discussReqDTO,
                                          @RequestParam(value = "tags", required = false) List<Integer> tagIdList,
                                          Principal principal) {
        try {
            discussionServ.addDiscussion(discussReqDTO, tagIdList, principal);
            return ResponseEntity.ok("Add successfully!");
        } catch (EntityNotFoundException e) {
            // TODO: handle exception
            errorResponseDTO errorDTO = new errorResponseDTO();
            errorDTO.setError(e.getMessage());
            List<String> details = new ArrayList<>();
            details.add("Author not exist!");
            errorDTO.setDetails(details);

            return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }
    }

    @PutMapping(value = "/discussion/{discussionId}")
    public ResponseEntity<Object> updatePost(@PathVariable Integer discussionId,
                                             @ModelAttribute discussionRequestDTO discussReqDTO,
                                             @RequestParam(value = "tags", required = false) List<Integer> tagIdList) {

        try {
            discussionServ.updateDiscussion(discussionId, discussReqDTO, tagIdList);
            return ResponseEntity.ok("Update successfully!");
        } catch (EntityNotFoundException e) {
            // TODO: handle exception
            errorResponseDTO errorDTO = new errorResponseDTO();
            errorDTO.setError(e.getMessage());
            List<String> details = new ArrayList<>();
            details.add("Discussion not found!");
            errorDTO.setDetails(details);

            return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }
    }

    @DeleteMapping(value = "/discussion/{discussionId}")
    public ResponseEntity<Object> deletePost(@PathVariable Integer discussionId) {
        try {
            discussionServ.deleteDiscussion(discussionId);
            return ResponseEntity.ok("Delete successfully!");
        } catch (EntityNotFoundException e) {
            // TODO: handle exception
            errorResponseDTO errorDTO = new errorResponseDTO();
            errorDTO.setError(e.getMessage());
            List<String> details = new ArrayList<>();
            details.add("Discussion not found!");
            errorDTO.setDetails(details);

            return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }
    }

}

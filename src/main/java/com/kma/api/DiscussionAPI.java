package com.kma.api;

import com.kma.models.*;
import com.kma.services.discussionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DiscussionAPI {

    @Autowired
    discussionService discussionServ;

    @GetMapping(value = "/discussions/{discussionId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_STUDENT')")
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

    @GetMapping(value="/discussions")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_STUDENT')")
    public ResponseEntity<Object> getAllDiscussion(@RequestParam Map<String,Object> params,
                                                   @RequestParam(defaultValue = "date") String sort,
                                                   @RequestParam(defaultValue = "desc") String order,
                                                   @RequestParam(required = false, defaultValue = "0") int page,
                                                   @RequestParam(required = false, defaultValue = "10") int size){
        try {
            paginationResponseDTO<discussionResponseDTO> DTO =  discussionServ.getAllDiscussion(params, page, size, sort, order);
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

    @GetMapping(value="/user/my_discussions")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_STUDENT')")
    public ResponseEntity<Object> getAllDiscussionOfUser(@RequestParam Map<String,Object> params,
                                                         @RequestParam(defaultValue = "date") String sort,
                                                         @RequestParam(defaultValue = "desc") String order,
                                                         @RequestParam(required = false, defaultValue = "0") int page,
                                                         @RequestParam(required = false, defaultValue = "10") int size){
        try {
            paginationResponseDTO<discussionResponseDTO> DTO =  discussionServ.getAllDiscussionOfUser(params, page, size, sort, order);
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

    @GetMapping(value="/discussions/pending")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> getAllPendingDiscuss(@RequestParam Map<String,Object> params,
                                                       @RequestParam(required = false, defaultValue = "0") int page,
                                                       @RequestParam(required = false, defaultValue = "10") int size){
        try {
            paginationResponseDTO<discussionResponseDTO> DTO = discussionServ.getAllPendingDiscuss(page, size);
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

    @PostMapping(value = "/discussions")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_STUDENT')")
    public ResponseEntity<Object> addDiscussion(@ModelAttribute discussionRequestDTO discussReqDTO,
                                                @RequestParam(value = "tags", required = false) List<Integer> tagIdList) {
        try {
            discussionServ.addDiscussion(discussReqDTO, tagIdList);
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

    @PutMapping(value = "/discussions/{discussionId}")
    @PreAuthorize("@discussionServ.isOwner(#discussionId, principal.userId) or hasRole('ADMIN')")
    public ResponseEntity<Object> updateDiscussion(@PathVariable Integer discussionId,
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

    @PatchMapping(value = "/discussions/{discussionId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> updateDiscussionStatus(@PathVariable Integer discussionId,
                                                         @RequestParam(value = "discussionStatus") String discussionStatus) {

        try {
            discussionServ.updateDiscussionStatus(discussionId, discussionStatus);
            return ResponseEntity.ok("Update successfully!");
        } catch (EntityNotFoundException | IllegalArgumentException e) {
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

    @DeleteMapping(value = "/discussions/{discussionId}")
    @PreAuthorize("@discussionServ.isOwner(#discussionId, principal.userId) or hasRole('ADMIN')")
    public ResponseEntity<Object> deleteDiscussion(@PathVariable Integer discussionId) {
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

package com.kma.api;

import com.kma.models.*;
import com.kma.services.discussionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DiscussionAPI {

    @Autowired
    discussionService discussionServ;
    @Autowired
    com.kma.utilities.buildErrorResUtil buildErrorResUtil;

    @GetMapping(value="/discussions/tags/{tagId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_STUDENT')")
    public ResponseEntity<Object> getAllDiscussionByTag(@PathVariable Integer tagId,
                                                        @RequestParam(defaultValue = "date") String sort,
                                                        @RequestParam(defaultValue = "desc") String order,
                                                        @RequestParam(required = false, defaultValue = "0") int page,
                                                        @RequestParam(required = false, defaultValue = "10") int size){
        try {
            paginationResponseDTO<discussionResponseDTO> DTO = discussionServ.getAllDiscussionByTag(tagId, page, size, sort, order);
            return new ResponseEntity<>(DTO, HttpStatus.OK);
        } catch (Exception e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
            return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/discussions/{discussionId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_STUDENT')")
    public ResponseEntity<Object> getById(@PathVariable Integer discussionId){
        try {
            discussionDTO DTO = discussionServ.getById(discussionId);
            return new ResponseEntity<>(DTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "Discussion not found!");
            return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
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
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
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
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
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
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
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
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "Author not exist!");
            return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
            return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
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
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "Discussion not found!");
            return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
            return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping(value = "/discussions/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> updateDiscussionStatus(@RequestParam(value = "discussionId") List<Integer> discussionIds,
                                                         @RequestParam(value = "discussionStatus") String discussionStatus) {

        try {
            discussionServ.updateDiscussionStatus(discussionIds, discussionStatus);
            return ResponseEntity.ok("Update successfully!");
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "Discussion not found!");
            return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
            return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/discussions/{discussionId}")
    @PreAuthorize("@discussionServ.isOwner(#discussionId, principal.userId) or hasRole('ADMIN')")
    public ResponseEntity<Object> deleteDiscussion(@PathVariable Integer discussionId) {
        try {
            discussionServ.deleteDiscussion(discussionId);
            return ResponseEntity.ok("Delete successfully!");
        } catch (EntityNotFoundException e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "Discussion not found!");
            return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
            return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

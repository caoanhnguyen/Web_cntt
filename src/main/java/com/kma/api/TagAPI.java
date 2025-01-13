package com.kma.api;

import com.kma.enums.TagCategory;
import com.kma.models.*;
import com.kma.services.tagService;
import com.kma.utilities.buildErrorResUtil;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tags")
public class TagAPI {
    @Autowired
    tagService tagServ;
    @Autowired
    buildErrorResUtil buildErrorResUtil;

    @GetMapping(value = "/grouped")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_STUDENT')")
    public ResponseEntity<?> getTagsOrderByCategory() {
        try {
            Map<TagCategory, List<tagDTO>> DTO = tagServ.getGroupedTags();

            // Trả về danh sách tag
            return new ResponseEntity<>(DTO, HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid category name. Valid values: GENERAL, FOUNDATION, SPECIALIZED.");
        } catch (Exception e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
            return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value="")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_STUDENT')")
    public ResponseEntity<Object> getAllTags(@RequestParam(required = false, defaultValue = "0") int page,
                                             @RequestParam(required = false, defaultValue = "10") int size){
        try {
            paginationResponseDTO<tagDTO> DTO =  tagServ.getAllTags(page, size);
            return new ResponseEntity<>(DTO, HttpStatus.OK);
        } catch (Exception e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
            return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value="/{tagId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_STUDENT')")
    public ResponseEntity<Object> getById(@PathVariable Integer tagId){
        try {
            tagDTO DTO = tagServ.getById(tagId);
            return new ResponseEntity<>(DTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "Tag not found!");
            return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
            return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> addTag(@ModelAttribute tagRequestDTO tagReqDTO){
        try {
            tagServ.addTag(tagReqDTO);
            return ResponseEntity.ok("Add successfully!");
        } catch (EntityExistsException e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "Tag name already exists!");
            return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
            return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/{tagId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> updateTag(@PathVariable Integer tagId,
                                            @ModelAttribute tagRequestDTO tagReqDTO) {
        try {
            tagServ.updateTag(tagId, tagReqDTO);
            return ResponseEntity.ok("Update successfully!");
        } catch (EntityNotFoundException e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "Tag not found!");
            return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
        } catch (EntityExistsException e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "Tag name already exists!");
            return new ResponseEntity<>(errorDTO, HttpStatus.CONFLICT);
        } catch (Exception e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
            return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @DeleteMapping(value = "/{tagId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> deleteTag(@PathVariable Integer tagId) {
        try {
            tagServ.deleteTag(tagId);
            return ResponseEntity.ok("Delete successfully!");
        } catch (EntityNotFoundException e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "Tag not found!");
            return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
            return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

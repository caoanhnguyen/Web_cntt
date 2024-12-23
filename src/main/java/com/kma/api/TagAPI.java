package com.kma.api;

import com.kma.enums.TagCategory;
import com.kma.models.errorResponseDTO;
import com.kma.models.paginationResponseDTO;
import com.kma.models.tagDTO;
import com.kma.services.tagService;
import com.kma.utilities.buildErrorResUtil;
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
}

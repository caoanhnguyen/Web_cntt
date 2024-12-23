package com.kma.api;

import com.kma.enums.TagCategory;
import com.kma.models.errorResponseDTO;
import com.kma.models.paginationResponseDTO;
import com.kma.models.tagDTO;
import com.kma.services.tagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TagAPI {
    @Autowired
    tagService tagServ;

    @GetMapping(value = "/tags/grouped")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_STUDENT')")
    public ResponseEntity<?> getTagsOrderByCategory() {
        try {
            Map<TagCategory, List<tagDTO>> DTO = tagServ.getGroupedTags();

            // Trả về danh sách tag
            return new ResponseEntity<>(DTO, HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid category name. Valid values: GENERAL, FOUNDATION, SPECIALIZED.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }
    }

    @GetMapping(value="/tags")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_STUDENT')")
    public ResponseEntity<Object> getAllTags(@RequestParam(required = false, defaultValue = "0") int page,
                                             @RequestParam(required = false, defaultValue = "10") int size){
        try {
            paginationResponseDTO<tagDTO> DTO =  tagServ.getAllTags(page, size);
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
}

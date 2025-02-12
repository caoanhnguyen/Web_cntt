package com.kma.api;

import com.kma.models.*;
import com.kma.services.articleService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ArticleAPI {

    @Autowired
    com.kma.utilities.buildErrorResUtil buildErrorResUtil;
    @Autowired
    articleService articleServ;


    @GetMapping(value="/public/{slug}/articles")
    public ResponseEntity<Object> getAllArticleBySlug(@PathVariable String slug,
                                                      @RequestParam(required = false, defaultValue = "0") int page,
                                                      @RequestParam(required = false, defaultValue = "10") int size){
        try {
            paginationResponseDTO<articleDTO> DTO = articleServ.getAllArticleOfMenuItemBySlug(slug, page, size);
            return new ResponseEntity<>(DTO, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred with slug!");
            return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
            return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping(value = "/articles")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> addArticle(@RequestParam(value = "file", required = false) List<MultipartFile> files,
                                             @ModelAttribute articleRequestDTO articleRequestDTO) {
        try {
            articleServ.addArticle(files, articleRequestDTO);
            return ResponseEntity.ok("Add successfully!");
        } catch (IllegalArgumentException e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "Menu Item not found!");
            return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
            return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/articles/{articleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> updateArticle(@PathVariable Integer articleId,
                                             @ModelAttribute articleRequestDTO articleRequestDTO,
                                             @RequestParam(value = "file", required = false) List<MultipartFile> files,
                                             @RequestParam(value = "deleteFileIds", required = false) List<Integer> deleteFileIds) {

        try {
            articleServ.updateArticle(articleId, articleRequestDTO, files, deleteFileIds);
            return ResponseEntity.ok("Update successfully!");
        } catch (EntityNotFoundException e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "Article not found!");
            return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
            return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/articles/{articleId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> deleteArticle(@PathVariable Integer articleId) {
        try {
            articleServ.deleteArticle(articleId);
            return ResponseEntity.ok("Delete successfully!");
        } catch (EntityNotFoundException e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "Article not found!");
            return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
            return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

package com.kma.api;

import com.kma.models.*;
import com.kma.services.menuItemService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MenuItemAPI {

    @Autowired
    menuItemService menuItemServ;
    @Autowired
    com.kma.utilities.buildErrorResUtil buildErrorResUtil;

    @GetMapping(value="/public/menu_items")
    public ResponseEntity<Object> getAllMenuItem(){
        try {
            List<navBarDTO> navBarDTOList = menuItemServ.getAllMenuItem();
            return new ResponseEntity<>(navBarDTOList, HttpStatus.OK);
        } catch (Exception e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
            return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/menu_items")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> addMenuItem(@ModelAttribute menuItemReqDTO menuItemReqDTO) {
        try {
            menuItemServ.addMenuItem(menuItemReqDTO);
            return ResponseEntity.ok("Add successfully!");
        } catch (Exception e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
            return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/menu_items/{menuItemId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> updateMenuItem(@PathVariable Integer menuItemId,
                                             @ModelAttribute menuItemResDTO menuItemResDTO) {

        try {
            menuItemServ.updateMenuItem(menuItemId, menuItemResDTO);
            return ResponseEntity.ok("Update successfully!");
        } catch (EntityNotFoundException e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "Menu Item not found!");
            return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
            return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/menu_items/{menuItemId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> softDeleteMenuItem(@PathVariable Integer menuItemId) {
        try {
            menuItemServ.softDeleteMenuItem(menuItemId);
            return ResponseEntity.ok("Delete successfully!");
        } catch (EntityNotFoundException e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "Menu Item not found!");
            return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
            return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping(value = "/menu_items/{menuItemId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object>restoreMenuItem(@PathVariable Integer menuItemId) {
        try {
            menuItemServ.restoreMenuItem(menuItemId);
            return ResponseEntity.ok("Restore successfully!");
        } catch (EntityNotFoundException e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "Menu Item not found!");
            return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
            return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

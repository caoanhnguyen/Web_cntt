package com.kma.api;


import com.kma.models.*;
import com.kma.security.JwtTokenUtil;
import com.kma.services.IUserService;
import com.kma.utilities.buildErrorResUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${user.prefix}")
public class UserController {

    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    IUserService userService;
    @Autowired
    buildErrorResUtil buildErrorResUtil;

    @GetMapping("/accounts/employees")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> getAllNhanVienAccount(@RequestParam(value = "searchTerm", required = false, defaultValue = "") String searchTerm,
                                                        @RequestParam(required = false, defaultValue = "0") int page,
                                                        @RequestParam(required = false, defaultValue = "10") int size) {
        // Kiểm tra thông tin đăng nhập và sinh token
        try {
            paginationResponseDTO<accountDTO> DTO = userService.getAllUser_NhanVienAccount(searchTerm, page, size);
            // Trả về token trong response
            return new ResponseEntity<>(DTO, HttpStatus.OK);
        } catch (ExpiredJwtException e){
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "Token expired. Please log in again.");
            return new ResponseEntity<>(errorDTO, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
            return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/accounts/students")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> getAllSinhVienAccount(@RequestParam(value = "searchTerm", required = false, defaultValue = "") String searchTerm,
                                                        @RequestParam(required = false, defaultValue = "0") int page,
                                                        @RequestParam(required = false, defaultValue = "10") int size) {
        // Kiểm tra thông tin đăng nhập và sinh token
        try {
            paginationResponseDTO<accountDTO> DTO = userService.getAllUser_SinhVienAccount(searchTerm, page, size);
            // Trả về token trong response
            return new ResponseEntity<>(DTO, HttpStatus.OK);
        } catch (ExpiredJwtException e){
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "Token expired. Please log in again.");
            return new ResponseEntity<>(errorDTO, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
            return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @ModelAttribute UserLoginDTO userLoginDTO) {
        // Kiểm tra thông tin đăng nhập và sinh token
        try {
            String token = userService.login(userLoginDTO.getUserName(), userLoginDTO.getPassword());
            // Trả về token trong response
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (ExpiredJwtException e){
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "Token expired. Please log in again.");
            return new ResponseEntity<>(errorDTO, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
            return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping("/{userId}/change_password")
    @PreAuthorize("@userService.isOwner(#userId, principal.userId)")
    public ResponseEntity<Object> changePassword(@PathVariable Integer userId,
                                                 @ModelAttribute changePasswordDTO changePasswordDTO) {
        // Kiểm tra thông tin đăng nhập và sinh token
        try {
            userService.changePassword(changePasswordDTO);
            return ResponseEntity.ok("Change successfully!");
        } catch (IllegalArgumentException e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "Token expired. Please log in again.");
            return new ResponseEntity<>(errorDTO, HttpStatus.UNAUTHORIZED);
        }  catch (Exception e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
            return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{userId}/username")
    @PreAuthorize("@userService.isOwner(#userId, principal.userId)")
    public ResponseEntity<Object> updateUserName(@PathVariable Integer userId,
                                                 @RequestParam(value = "username") String userName) {
        // Kiểm tra thông tin đăng nhập và sinh token
        try {
            userService.updateUserName(userId, userName);
            return ResponseEntity.ok("Change successfully!");
        } catch (IllegalArgumentException e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "Token expired. Please log in again.");
            return new ResponseEntity<>(errorDTO, HttpStatus.UNAUTHORIZED);
        }  catch (Exception e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
            return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{userId}/reset_password/")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> resetPassword(@PathVariable Integer userId) {
        // Kiểm tra thông tin đăng nhập và sinh token
        try {
            userService.resetPasswordForUser(userId);
            return ResponseEntity.ok("Reset successfully!");
        } catch (Exception e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
            return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/role/{accountId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> addRole(@PathVariable Integer accountId,
                                          @RequestParam Integer roleId) {
        // Kiểm tra thông tin đăng nhập và sinh token
        try {
            userService.addRole(accountId, roleId);

            return ResponseEntity.ok("Add role successfully!");
        } catch (IllegalArgumentException e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "Token expired. Please log in again.");
            return new ResponseEntity<>(errorDTO, HttpStatus.UNAUTHORIZED);
        }  catch (Exception e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
            return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/role/{accountId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> removeRole(@PathVariable Integer accountId,
                                             @RequestParam Integer roleId) {
        // Kiểm tra thông tin đăng nhập và sinh token
        try {
            userService.removeRole(accountId, roleId);

            return ResponseEntity.ok("Remove role successfully!");
        } catch (IllegalArgumentException e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "Token expired. Please log in again.");
            return new ResponseEntity<>(errorDTO, HttpStatus.UNAUTHORIZED);
        }  catch (Exception e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
            return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/logout")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_STUDENT')")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        try{
            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String jwt = authorizationHeader.substring(7);
                long expirationTime = jwtTokenUtil.getExpirationTime(jwt); // Tính thời gian hết hạn
                jwtTokenUtil.addTokenToBlacklist(jwt, expirationTime);
                return ResponseEntity.ok("Logout successful. Token blacklisted.");
            }
            return ResponseEntity.badRequest().body("Invalid Authorization header");
        } catch (Exception e) {
            errorResponseDTO errorDTO = buildErrorResUtil.buildErrorRes(e, "An error occurred!");
            return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("admin/{userId}/lock")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> lockAccount(@PathVariable Integer userId) {
        try {
            userService.updateAccountLockStatus(userId, true); // Lock tài khoản
            return ResponseEntity.ok("Account locked successfully!");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred!");
        }
    }

    @PatchMapping("admin/{userId}/unlock")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> unlockAccount(@PathVariable Integer userId) {
        try {
            userService.updateAccountLockStatus(userId, false); // Unlock tài khoản
            return ResponseEntity.ok("Account unlocked successfully!");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred!");
        }
    }
}

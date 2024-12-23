package com.kma.api;


import com.kma.models.UserLoginDTO;
import com.kma.models.changePasswordDTO;
import com.kma.models.errorResponseDTO;
import com.kma.security.JwtTokenUtil;
import com.kma.services.IUserService;
import com.kma.utilities.buildErrorResUtil;
import io.jsonwebtoken.ExpiredJwtException;
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


    @PutMapping("/change_password")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_STUDENT')")
    public ResponseEntity<Object> changePassword(@ModelAttribute changePasswordDTO changePasswordDTO) {
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

    @PutMapping("/admin/reset_password/{userId}")
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
}

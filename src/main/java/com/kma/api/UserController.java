package com.kma.api;


import com.kma.models.UserLoginDTO;
import com.kma.models.changePasswordDTO;
import com.kma.models.errorResponseDTO;
import com.kma.security.JwtTokenUtil;
import com.kma.services.IUserService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("${user.prefix}")
public class UserController {
    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    IUserService userService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @ModelAttribute UserLoginDTO userLoginDTO) {
        // Kiểm tra thông tin đăng nhập và sinh token
        try {
            String token = userService.login(userLoginDTO.getUserName(), userLoginDTO.getPassword());
            // Trả về token trong response
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (ExpiredJwtException e){
            // TODO: handle exception
            errorResponseDTO errorDTO = new errorResponseDTO();
            errorDTO.setError(e.getMessage());
            List<String> details = new ArrayList<>();
            details.add("Token expired. Please log in again.");
            errorDTO.setDetails(details);

            return new ResponseEntity<>(errorDTO, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            // TODO: handle exception
            errorResponseDTO errorDTO = new errorResponseDTO();
            errorDTO.setError(e.getMessage());
            List<String> details = new ArrayList<>();
            details.add("An error occurred!");
            errorDTO.setDetails(details);

            return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE', 'STUDENT')")
    @PutMapping("/change_password")
    public ResponseEntity<Object> changePassword(@RequestHeader("Authorization") String token,  // Lấy token từ header Authorization
                                                 @RequestBody changePasswordDTO changePasswordDTO) {
        // Kiểm tra thông tin đăng nhập và sinh token
        try {
            // Lấy username từ token
            String username = jwtTokenUtil.extractUserName(token.substring(7));  // Lấy username từ token (loại bỏ "Bearer ")

            userService.changePassword(username, changePasswordDTO);
            return ResponseEntity.ok("Change successfully!");
        } catch (IllegalArgumentException e) {
            // Nếu token hết hạn hoặc không hợp lệ, trả về 401 Unauthorized
            // TODO: handle exception
            errorResponseDTO errorDTO = new errorResponseDTO();
            errorDTO.setError(e.getMessage());
            List<String> details = new ArrayList<>();
            details.add("Token expired or Invalid. Please log in again.");
            errorDTO.setDetails(details);

            return new ResponseEntity<>(errorDTO, HttpStatus.UNAUTHORIZED);
        }  catch (Exception e) {
            // TODO: handle exception
            errorResponseDTO errorDTO = new errorResponseDTO();
            errorDTO.setError(e.getMessage());
            List<String> details = new ArrayList<>();
            details.add("An error occurred!");
            errorDTO.setDetails(details);

            return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/role/{username}")
    public ResponseEntity<Object> addRole(@PathVariable String username,
                                          @RequestParam Integer roleId) {
        // Kiểm tra thông tin đăng nhập và sinh token
        try {
            userService.addRole(username, roleId);

            return ResponseEntity.ok("Add role successfully!");
        } catch (IllegalArgumentException e) {
            // Nếu token hết hạn hoặc không hợp lệ, trả về 401 Unauthorized
            // TODO: handle exception
            errorResponseDTO errorDTO = new errorResponseDTO();
            errorDTO.setError(e.getMessage());
            List<String> details = new ArrayList<>();
            details.add("Token expired or Invalid. Please log in again.");
            errorDTO.setDetails(details);

            return new ResponseEntity<>(errorDTO, HttpStatus.UNAUTHORIZED);
        }  catch (Exception e) {
            // TODO: handle exception
            errorResponseDTO errorDTO = new errorResponseDTO();
            errorDTO.setError(e.getMessage());
            List<String> details = new ArrayList<>();
            details.add("An error occurred!");
            errorDTO.setDetails(details);

            return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/logout")
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
            // TODO: handle exception
            errorResponseDTO errorDTO = new errorResponseDTO();
            errorDTO.setError(e.getMessage());
            List<String> details = new ArrayList<>();
            details.add("An error occurred!");
            errorDTO.setDetails(details);

            return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
        }
    }
}

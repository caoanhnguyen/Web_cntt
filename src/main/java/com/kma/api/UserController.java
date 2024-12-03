package com.kma.api;


import com.kma.models.UserLoginDTO;
import com.kma.models.changePasswordDTO;
import com.kma.models.errorResponseDTO;
import com.kma.security.JwtTokenUtil;
import com.kma.services.IUserService;
import io.jsonwebtoken.ExpiredJwtException;
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
    private IUserService userService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody UserLoginDTO userLoginDTO) {
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

    @PreAuthorize("hasAnyRole('EMPLOYEE', 'STUDENT') and #username == authentication.name")
    @PutMapping("/change_password")
    public ResponseEntity<Object> changePassword(@RequestHeader("Authorization") String token,  // Lấy token từ header Authorization
                                                 @RequestBody changePasswordDTO changePasswordDTO) {
        // Kiểm tra thông tin đăng nhập và sinh token
        try {
            // Lấy username từ token
            String username = jwtTokenUtil.extractUserName(token.substring(7));  // Lấy username từ token (loại bỏ "Bearer ")

            userService.changePassword(username, changePasswordDTO);
            return ResponseEntity.ok("Change successfully!");
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

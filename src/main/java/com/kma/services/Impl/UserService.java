package com.kma.services.Impl;

import com.kma.models.changePasswordDTO;
import com.kma.repository.entities.User;
import com.kma.repository.userRepo;
import com.kma.security.JwtTokenUtil;
import com.kma.services.IUserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService implements IUserService {
    private final userRepo userRepo;  // final
    private final PasswordEncoder passwordEncoder;  // final
    private final JwtTokenUtil jwtTokenUtil;  // final
    private final AuthenticationManager authenticationManager;  // final

    @Override
    public String login(String userName, String password) throws Exception {
        Optional<User> optionalUser = userRepo.findByUserName(userName);
        if(optionalUser.isEmpty()) {
            throw new Exception("Invalid user name / password");
        }
        //return optionalUser.get();//muốn trả JWT token ?
        User existingUser = optionalUser.get();
        //check password
//        !passwordEncoder.matches(password, existingUser.getPassword())
        if(!passwordEncoder.matches(password, existingUser.getPassword())) {
            throw new BadCredentialsException("Wrong username or password");
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userName, password,
                existingUser.getAuthorities()
        );

        //authenticate with Java Spring security
        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUtil.generateToken(existingUser);
    }

    @Override
    public void changePassword(String username, changePasswordDTO changePasswordDTO) throws Exception {
        // Lấy người dùng từ cơ sở dữ liệu
        Optional<User> optionalUser = userRepo.findByUserName(username);
        // Kiểm tra xem người dùng có tồn tại không
        User user = optionalUser.orElseThrow(() -> new Exception("User not found"));

        // Kiểm tra mật khẩu cũ có khớp không
        if (!passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPassword())) {
            throw new Exception("Old password is incorrect");
        }

        // Kiểm tra mật khẩu mới và xác nhận mật khẩu mới có khớp không
        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())) {
            throw new Exception("New password and confirm password do not match");
        }

        // Mã hóa mật khẩu mới
        String encodedNewPassword = passwordEncoder.encode(changePasswordDTO.getNewPassword());

        // Cập nhật mật khẩu mới vào cơ sở dữ liệu
        user.setPassword(encodedNewPassword);

        userRepo.save(user);
    }
}

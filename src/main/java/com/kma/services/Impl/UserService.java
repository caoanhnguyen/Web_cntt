package com.kma.services.Impl;

import com.kma.models.changePasswordDTO;
import com.kma.repository.entities.Role;
import com.kma.repository.entities.User;
import com.kma.repository.roleRepo;
import com.kma.repository.userRepo;
import com.kma.security.JwtTokenUtil;
import com.kma.services.IUserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService implements IUserService {

    private final roleRepo rolerepo;  // final
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
        //muốn trả JWT token ?
        User existingUser = optionalUser.get();
        //check password
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
    public void changePassword(changePasswordDTO changePasswordDTO) throws Exception {
        // Lấy người dùng
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

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

    @Override
    public void resetPasswordForUser(Integer userId) throws Exception {
        // Lấy người dùng từ cơ sở dữ liệu
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));

        // Mã hóa mật khẩu mới
        String encodedNewPassword = passwordEncoder.encode("12345");

        // Cập nhật mật khẩu mới vào cơ sở dữ liệu
        user.setPassword(encodedNewPassword);

        userRepo.save(user);
    }

    @Override
    public void addRole(Integer accountId, Integer roleId) throws Exception {
        // Lấy người dùng từ cơ sở dữ liệu
        User user = userRepo.findById(accountId)
                .orElseThrow(() -> new Exception("User not found"));

        // Kiểm tra xem Role có tồn tại không
        Role newRole = rolerepo.findById(roleId).orElse(null);
        if (newRole == null) {
            throw new Exception("Role not found");
        }

        // Tránh thêm trùng Role cho User
        if (user.getRoleList().contains(newRole)) {
            throw new Exception("User already has this role");
        }

        // Thêm Role cho User và User vào Role
        user.getRoleList().add(newRole);
        newRole.getUserList().add(user);

        // Lưu lại thông tin vào cơ sở dữ liệu
        userRepo.save(user);
        rolerepo.save(newRole);
    }

}

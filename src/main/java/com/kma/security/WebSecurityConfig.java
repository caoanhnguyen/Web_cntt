package com.kma.security;

import com.kma.repository.entities.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@EnableWebMvc
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;

    @Value("${api.prefix}")
    private String apiPrefix;

    @Value("${user.prefix}")
    private String userPrefix;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)  throws Exception{
        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(
                                String.format("%s/login/**", userPrefix),
                                String.format("%s/class/**", apiPrefix),
                                String.format("%s/nhanvien/**", apiPrefix)
                        )
                        .permitAll()

                        // PhongBan Authorization
                        .requestMatchers(GET,
                                String.format("%s/phong_ban/**", apiPrefix)).hasAnyRole(Role.ADMIN, Role.STUDENT, Role.EMPLOYEE)
                        .requestMatchers(POST,
                                String.format("%s/phong_ban/**", apiPrefix)).hasAnyRole(Role.ADMIN)
                        .requestMatchers(PUT,
                                String.format("%s/phong_ban/**", apiPrefix)).hasAnyRole(Role.ADMIN)
                        .requestMatchers(DELETE,
                                String.format("%s/phong_ban/**", apiPrefix)).hasAnyRole(Role.ADMIN)


                        // POST Authorization
                        .requestMatchers(GET,
                                String.format("%s/posts/**", apiPrefix)).hasAnyRole(Role.ADMIN, Role.STUDENT, Role.EMPLOYEE)
                        .requestMatchers(POST,
                                String.format("%s/posts/**", apiPrefix)).hasAnyRole(Role.ADMIN, Role.EMPLOYEE)
                        .requestMatchers(PUT,
                                String.format("%s/posts/**", apiPrefix)).hasAnyRole(Role.ADMIN, Role.EMPLOYEE)
                        .requestMatchers(DELETE,
                                String.format("%s/posts/**", apiPrefix)).hasAnyRole(Role.ADMIN, Role.EMPLOYEE)

                        // Event Registration Authorization
                        .requestMatchers(POST,
                                String.format("%s/registration/**", apiPrefix)).hasAnyRole(Role.STUDENT) // Đăng kí sự kiện

                        // Event Authorization
                        .requestMatchers(GET,
                                String.format("%s/sukien/**", apiPrefix)).hasAnyRole(Role.ADMIN, Role.STUDENT, Role.EMPLOYEE)
                        .requestMatchers(GET,
                                String.format("%s/sukien/participation_list/**", apiPrefix)).hasAnyRole(Role.ADMIN, Role.STUDENT, Role.EMPLOYEE)
                        .requestMatchers(POST,
                                String.format("%s/sukien/**", apiPrefix)).hasAnyRole(Role.ADMIN, Role.EMPLOYEE)
                        .requestMatchers(PUT,
                                String.format("%s/sukien/**", apiPrefix)).hasAnyRole(Role.ADMIN, Role.EMPLOYEE)
                        .requestMatchers(DELETE,
                                String.format("%s/sukien/**", apiPrefix)).hasAnyRole(Role.ADMIN, Role.EMPLOYEE)

                        // Student Authorization
                        .requestMatchers(GET,
                                String.format("%s/students/**", apiPrefix)).hasAnyRole(Role.ADMIN, Role.STUDENT, Role.EMPLOYEE)
                        .requestMatchers(GET,
                                String.format("%s/students/participated_events/**", apiPrefix)).hasAnyRole(Role.ADMIN, Role.STUDENT, Role.EMPLOYEE)
                        .requestMatchers(POST,
                                String.format("%s/students/**", apiPrefix)).hasAnyRole(Role.ADMIN)
                        .requestMatchers(PUT,
                                String.format("%s/students/**", apiPrefix)).hasAnyRole(Role.ADMIN, Role.STUDENT)
                        .requestMatchers(DELETE,
                                String.format("%s/students/**", apiPrefix)).hasAnyRole(Role.ADMIN)

                        // Employee Authorization
                        .requestMatchers(GET,
                                String.format("%s/nhanvien/**", apiPrefix)).hasAnyRole(Role.ADMIN, Role.STUDENT, Role.EMPLOYEE)
//                        .requestMatchers(POST,
//                                String.format("%s/nhanvien/**", apiPrefix)).hasAnyRole(Role.ADMIN)
                        .requestMatchers(PUT,
                                String.format("%s/nhanvien/**", apiPrefix)).hasAnyRole(Role.ADMIN, Role.EMPLOYEE)
                        .requestMatchers(DELETE,
                                String.format("%s/nhanvien/**", apiPrefix)).hasAnyRole(Role.ADMIN)

                        // FILE Authorization
                        .requestMatchers(GET,
                               ("/downloadProfile/**")).hasAnyRole(Role.ADMIN, Role.STUDENT, Role.EMPLOYEE)
                        .requestMatchers(GET,
                                ("/downloadDocs/**")).hasAnyRole(Role.ADMIN, Role.STUDENT, Role.EMPLOYEE)
                        .requestMatchers(GET,
                                ("/downloadFile/**")).hasAnyRole(Role.ADMIN, Role.STUDENT, Role.EMPLOYEE)

                        // Change password Authorization
                        .requestMatchers(PUT,
                                ("/change_password/**")).hasAnyRole(Role.STUDENT, Role.EMPLOYEE)

                        .anyRequest().authenticated());
        return http.build();
    }
}

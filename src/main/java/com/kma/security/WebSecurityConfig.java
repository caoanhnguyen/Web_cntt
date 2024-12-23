package com.kma.security;

import com.kma.repository.entities.Role;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@EnableWebMvc
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;

    @Value("${api.prefix}")
    String apiPrefix;

    @Value("${user.prefix}")
    String userPrefix;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)  throws Exception{
        http
                .csrf(AbstractHttpConfigurer::disable)
//                .cors(AbstractHttpConfigurer::disable)
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(
                                String.format("%s/login/**", userPrefix)
                        )
                        .permitAll()
                        .requestMatchers(POST, "/notifications/**").permitAll()
                        .requestMatchers(POST, "/api/store-fcm-token").permitAll()
                        .requestMatchers(GET, "/downloadFile/**").permitAll()
                        .requestMatchers(GET, "/downloadProfile/**").permitAll()
                        .requestMatchers(GET, "/downloadDocs/**").permitAll()
                        .requestMatchers(POST, "/uploadImg").permitAll()
                        .requestMatchers(GET,
                                String.format("%s/posts/**", apiPrefix)).permitAll()
                        .requestMatchers(GET,
                                String.format("%s/sukien/**", apiPrefix)).permitAll()

                        // Tag Authorization
                        .requestMatchers(GET,
                                String.format("%s/tags", apiPrefix)).hasAnyRole(Role.ADMIN, Role.STUDENT, Role.EMPLOYEE)

                        // Vote Authorization
                        .requestMatchers(POST,
                                String.format("%s/discussions/*/votes", apiPrefix)).hasAnyRole(Role.ADMIN, Role.STUDENT, Role.EMPLOYEE)
                        .requestMatchers(POST,
                                String.format("%s/answers/*/votes", apiPrefix)).hasAnyRole(Role.ADMIN, Role.STUDENT, Role.EMPLOYEE)
                        .requestMatchers(GET,
                                String.format("%s/votes/check/**", apiPrefix)).hasAnyRole(Role.ADMIN, Role.STUDENT, Role.EMPLOYEE)


                        // Discussion Authorization
                        .requestMatchers(GET,
                                String.format("%s/discussions/**", apiPrefix)).hasAnyRole(Role.ADMIN, Role.STUDENT, Role.EMPLOYEE)
                        .requestMatchers(GET,
                                String.format("%s/user/my_discussions/**", apiPrefix)).hasAnyRole(Role.ADMIN, Role.STUDENT, Role.EMPLOYEE)
                        .requestMatchers(POST,
                                String.format("%s/discussions/**", apiPrefix)).hasAnyRole(Role.ADMIN, Role.STUDENT, Role.EMPLOYEE)
                        .requestMatchers(PUT,
                                String.format("%s/discussions/**", apiPrefix)).hasAnyRole(Role.ADMIN, Role.STUDENT, Role.EMPLOYEE)
                        .requestMatchers(PATCH,
                                String.format("%s/discussions/*/status", apiPrefix)).hasAnyRole(Role.ADMIN, Role.STUDENT, Role.EMPLOYEE)
                        .requestMatchers(DELETE,
                                String.format("%s/discussions/**", apiPrefix)).hasAnyRole(Role.ADMIN, Role.STUDENT, Role.EMPLOYEE)

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
                                String.format("%s/user/my_posts/", apiPrefix)).hasAnyRole(Role.ADMIN, Role.STUDENT, Role.EMPLOYEE)
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
//                        .requestMatchers(GET,
//                                String.format("%s/sukien/**", apiPrefix)).hasAnyRole(Role.ADMIN, Role.STUDENT, Role.EMPLOYEE)
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
                                String.format("%s/students/**", apiPrefix)).hasAnyRole(Role.ADMIN, Role.EMPLOYEE)
                        .requestMatchers(PUT,
                                String.format("%s/students/**", apiPrefix)).hasAnyRole(Role.ADMIN, Role.EMPLOYEE)
                        .requestMatchers(DELETE,
                                String.format("%s/students/**", apiPrefix)).hasAnyRole(Role.ADMIN, Role.EMPLOYEE)

                        // Subject Authorization
                        .requestMatchers(GET,
                                String.format("%s/monhoc/**", apiPrefix)).hasAnyRole(Role.ADMIN, Role.STUDENT, Role.EMPLOYEE)
                        .requestMatchers(POST,
                                String.format("%s/monhoc/**", apiPrefix)).hasAnyRole(Role.ADMIN)
                        .requestMatchers(PUT,
                                String.format("%s/monhoc/**", apiPrefix)).hasAnyRole(Role.ADMIN, Role.EMPLOYEE)
                        .requestMatchers(DELETE,
                                String.format("%s/monhoc/**", apiPrefix)).hasAnyRole(Role.ADMIN)

                        // Class Authorization
                        .requestMatchers(GET,
                                String.format("%s/class/**", apiPrefix)).hasAnyRole(Role.ADMIN, Role.STUDENT, Role.EMPLOYEE)
                        .requestMatchers(POST,
                                String.format("%s/class/**", apiPrefix)).hasAnyRole(Role.ADMIN)
                        .requestMatchers(PUT,
                                String.format("%s/class/**", apiPrefix)).hasAnyRole(Role.ADMIN, Role.EMPLOYEE)
                        .requestMatchers(DELETE,
                                String.format("%s/class/**", apiPrefix)).hasAnyRole(Role.ADMIN)

                        // Employee Authorization
                        .requestMatchers(GET,
                                String.format("%s/nhanvien/**", apiPrefix)).hasAnyRole(Role.ADMIN, Role.STUDENT, Role.EMPLOYEE)
                        .requestMatchers(POST,
                                String.format("%s/nhanvien/**", apiPrefix)).hasAnyRole(Role.ADMIN)
                        .requestMatchers(PUT,
                                String.format("%s/nhanvien/**", apiPrefix)).hasAnyRole(Role.ADMIN, Role.EMPLOYEE)
                        .requestMatchers(PATCH,
                                String.format("%s/nhanvien/main_subject/**", apiPrefix)).hasAnyRole(Role.ADMIN)
                        .requestMatchers(PATCH,
                                String.format("%s/nhanvien/related_subject/**", apiPrefix)).hasAnyRole(Role.ADMIN)
                        .requestMatchers(DELETE,
                                String.format("%s/nhanvien/**", apiPrefix)).hasAnyRole(Role.ADMIN)

                        // FILE Authorization
                        .requestMatchers(GET,
                               ("/downloadProfile/**")).hasAnyRole(Role.ADMIN, Role.STUDENT, Role.EMPLOYEE)
                        .requestMatchers(GET,
                                ("/downloadDocs/**")).hasAnyRole(Role.ADMIN, Role.STUDENT, Role.EMPLOYEE)
                        .requestMatchers(GET,
                                ("/downloadFile/**")).hasAnyRole(Role.ADMIN, Role.STUDENT, Role.EMPLOYEE)

                        // Special Authorization
                        .requestMatchers(POST,
                                String.format("%s/change_password/**", userPrefix)).hasAnyRole(Role.STUDENT, Role.EMPLOYEE)
                        .requestMatchers(PATCH,
                                String.format("%s/role/**", userPrefix)).hasRole(Role.ADMIN)

                        .anyRequest().authenticated()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        // Xử lý 403 Forbidden
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\": \"Access denied: " + accessDeniedException.getMessage() + "\"}");
                        })
                        // Xử lý 401 Unauthorized
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\": \"Authentication required\"}");
                        })
                );


        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:8083", "https://fond-frank-goose.ngrok-free.app"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}

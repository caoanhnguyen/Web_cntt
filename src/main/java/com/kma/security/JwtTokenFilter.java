package com.kma.security;

import com.kma.repository.entities.User;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    @Value("${api.prefix}")
    String apiPrefix;

    @Value("${user.prefix}")
    String userPrefix;


    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            if(isBypassToken(request)) {
                filterChain.doFilter(request, response); //enable bypass
                return;
            }
            final String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return;
            }
            final String token = authHeader.substring(7);
            // Kiểm tra token hết hạn
            if (jwtTokenUtil.isTokenExpired(token)) {
                // Nếu token đã hết hạn, trả về 401 Unauthorized
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired. Please log in again.");
                return;
            }
            // Kiểm tra JWT trong Blacklist
            if (jwtTokenUtil.isTokenBlacklisted(token)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return;
            }
            final String userName = jwtTokenUtil.extractUserName(token);
            if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                User userDetails = (User) userDetailsService.loadUserByUsername(userName);
                if(jwtTokenUtil.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null,
                                    userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }else {
                    // Token không hợp lệ hoặc đã hết hạn, trả về 401
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized - Token is invalid or expired");
                    return;
                }
            }
            filterChain.doFilter(request, response); //enable bypass
        } catch (IllegalArgumentException e) {
            // Nếu token hết hạn hoặc không hợp lệ, trả về 401 Unauthorized
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"message\": \"Token expired or Invalid. Please log in again.\"}");
        } catch (RedisConnectionFailureException e) {
            // Nếu lỗi kết nối redis
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"message\": \"Failed to connect to Redis.\"}");
        } catch (Exception e) {
            // Các lỗi không mong muốn khác, trả về 401 Unauthorized
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"message\": \"Unauthorized - Invalid token\"}");
        }
    }

    private boolean isBypassToken(@NonNull  HttpServletRequest request) {
        final List<Pair<String, String>> bypassTokens = Arrays.asList(
                Pair.of(String.format("%s/login", userPrefix), "POST"),
                Pair.of("/notifications", "POST"),
                Pair.of("/api/store-fcm-token", "POST"),
                Pair.of(String.format("%s/home", userPrefix), "GET"),
                Pair.of("/uploadImg", "POST"),
                Pair.of("/downloadFile", "GET"),
                Pair.of("/downloadProfile", "GET"),
                Pair.of("/downloadDocs", "GET"),
                Pair.of(String.format("%s/posts", apiPrefix), "GET"),
                Pair.of(String.format("%s/sukien", apiPrefix), "GET")

        );
        for(Pair<String, String> bypassToken: bypassTokens) {
            if (request.getServletPath().contains(bypassToken.getFirst()) &&
                    request.getMethod().equals(bypassToken.getSecond())) {
                return true;
            }
        }
        return false;
    }
}

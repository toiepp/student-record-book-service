package com.lidiia.studentrecordbookservice.config;

import com.lidiia.studentrecordbookservice.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {




    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException, java.io.IOException {
        if (request.getRequestURI().equals("/api/auth/validate")) {
            // Пропустить валидацию для /api/auth/validate
            filterChain.doFilter(request, response);
            return;
        }
        try {
            String token = extractToken(request);

            if (token != null) {
                System.out.println("Token received: " + token);

                // Вывод информации о правилах авторизации
                System.out.println("Authorization rules for the request: " + request.getRequestURI());

                // Проверка токена
                jwtService.validateToken(token);

                // Получение информации об аутентификации
                Authentication authentication = jwtService.getAuthentication(token);
                System.out.println("User '" + authentication.getName() + "' authenticated with roles: " + authentication.getAuthorities());

                // Установка контекста безопасности
                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("Security context set for user: " + SecurityContextHolder.getContext().getAuthentication().getName());
            } else {
                System.out.println("No token found in the request");
            }

            // Выведем принципала (Principal)
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("Current principal: " + authentication);

        } catch (ExpiredJwtException eje) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token has expired");
            System.out.println("Token has expired: " + eje.getMessage());
            return;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized access to the application");
            System.out.println("Unauthorized access to the application: " + e.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }

        private String extractToken(HttpServletRequest request) {
            String header = request.getHeader(HttpHeaders.AUTHORIZATION);

            if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
                return header.substring("Bearer ".length());
            }

            return null;
        }
    }

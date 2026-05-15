package com.jobportal.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        // 🔹 Get role
        String role = authentication.getAuthorities()
                .stream()
                .map(r -> r.getAuthority())
                .findFirst()
                .orElse("UNKNOWN");

        DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String time = LocalDateTime.now().format(formater);
        // ✅ REQUIRED OUTPUT
        System.out.println("===== LOGIN SUCCESS =====");
        System.out.println("User: " + username);
        System.out.println("Role: " + role);
        System.out.println("looged in time: " + time);
        System.out.println("=========================");

        // existing logic
        boolean hasJobSeeker = authentication.getAuthorities()
                .stream()
                .anyMatch(r -> r.getAuthority().equals("Job Seeker"));

        boolean hasRecruiter = authentication.getAuthorities()
                .stream()
                .anyMatch(r -> r.getAuthority().equals("Recruiter"));

        if (hasJobSeeker || hasRecruiter) {
            response.sendRedirect("/dashboard/");
        } else {
            response.sendRedirect("/login?error");
        }
    }
}

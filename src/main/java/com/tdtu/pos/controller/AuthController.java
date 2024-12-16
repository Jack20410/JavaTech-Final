package com.tdtu.pos.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login() {
        return "login"; // Serve login.html
    }

    @GetMapping("/auth/redirect")
    public String redirectBasedOnRole(Authentication authentication) {
        if (authentication == null) {
            return "redirect:/login";
        }

        // Check user role and redirect accordingly
        if (authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_MANAGER"))) {
            return "redirect:/manager/index";
        } else if (authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_SALESPERSON"))) {
            return "redirect:/salesperson/index";
        }

        // Default fallback if no role matches
        return "redirect:/login?error";
    }
}

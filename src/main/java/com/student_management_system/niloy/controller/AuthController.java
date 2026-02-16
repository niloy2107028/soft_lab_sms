package com.student_management_system.niloy.controller;

import com.student_management_system.niloy.model.User;
import com.student_management_system.niloy.model.Role;
import com.student_management_system.niloy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        User user = userService.getUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("username", username);
        model.addAttribute("role", user.getRole());

        if (user.getRole() == Role.STUDENT) {
            return "redirect:/student/dashboard";
        } else if (user.getRole() == Role.TEACHER) {
            return "redirect:/teacher/dashboard";
        }

        return "redirect:/login";
    }
}

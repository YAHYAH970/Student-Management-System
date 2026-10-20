package com.college.sms.controller;

import com.college.sms.repository.AdminRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AdminRepository admins;
    private final PasswordEncoder encoder;
    public AuthController(AdminRepository admins, PasswordEncoder encoder) {
        this.admins = admins; this.encoder = encoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body, HttpSession session) {
        String username = body.getOrDefault("username", "");
        String password = body.getOrDefault("password", "");
        var admin = admins.findByUsername(username);
        if (admin.isPresent() && encoder.matches(password, admin.get().getPasswordHash())) {
            session.setAttribute("adminUser", username);
            return ResponseEntity.ok(Map.of("message", "Login successful", "username", username));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid username or password"));
    }

    @PostMapping("/logout")
    public Map<String, String> logout(HttpSession session) {
        session.invalidate();
        return Map.of("message", "Logged out");
    }
}

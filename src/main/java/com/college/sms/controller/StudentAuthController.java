package com.college.sms.controller;

import com.college.sms.repository.StudentRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/student-auth")
public class StudentAuthController {
    private final StudentRepository students;
    public StudentAuthController(StudentRepository students) { this.students = students; }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String,String> body, HttpSession session) {
        String email = body.getOrDefault("email", "").trim();
        String password = body.getOrDefault("password", "").trim();
        var student = students.findAll().stream()
            .filter(s -> s.getEmail().equalsIgnoreCase(email) && s.getRollNumber().equalsIgnoreCase(password))
            .findFirst();
        if (student.isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(Map.of("message", "Email ya temporary password galat hai."));
        session.setAttribute("studentId", student.get().getId());
        session.setAttribute("studentEmail", student.get().getEmail());
        return ResponseEntity.ok(Map.of("message", "Login successful", "name", student.get().getFullName()));
    }

    @PostMapping("/logout")
    public Map<String,String> logout(HttpSession session) {
        session.removeAttribute("studentId");
        session.removeAttribute("studentEmail");
        return Map.of("message", "Logged out");
    }
}

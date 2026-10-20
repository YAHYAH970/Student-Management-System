package com.college.sms.controller;

import com.college.sms.repository.StudentRepository;
import com.college.sms.repository.ResultRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/student-portal")
public class StudentPortalController {
    private final StudentRepository students;
    private final ResultRepository results;
    public StudentPortalController(StudentRepository students, ResultRepository results) { this.students = students; this.results = results; }

    @GetMapping("/me")
    public ResponseEntity<?> me(HttpSession session) {
        Long id = (Long) session.getAttribute("studentId");
        if (id == null) return ResponseEntity.status(401).body(Map.of("message", "Please log in."));
        return students.findById(id).<ResponseEntity<?>>map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.status(404).body(Map.of("message", "Student not found.")));
    }

    @GetMapping("/results")
    public ResponseEntity<?> myResults(HttpSession session) {
        Long id = (Long) session.getAttribute("studentId");
        if (id == null) return ResponseEntity.status(401).body(Map.of("message", "Please log in."));
        return ResponseEntity.ok(results.findByStudentId(id));
    }
}

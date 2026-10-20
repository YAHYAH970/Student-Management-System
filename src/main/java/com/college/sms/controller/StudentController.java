package com.college.sms.controller;

import com.college.sms.model.Student;
import com.college.sms.repository.StudentRepository;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    private final StudentRepository students;
    public StudentController(StudentRepository students) { this.students = students; }

    @GetMapping
    public List<Student> all(@RequestParam(required = false) String q) {
        return (q == null || q.isBlank()) ? students.findAll() : students.search(q.trim());
    }
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Student student) {
        try { student.setRollNumber(student.getRollNumber().trim()); return ResponseEntity.status(201).body(students.save(student)); }
        catch (DataIntegrityViolationException ex) { return ResponseEntity.badRequest().body(Map.of("message", "Roll number or email already exists.")); }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody Student input) {
        return students.findById(id).map(s -> {
            s.setRollNumber(input.getRollNumber().trim()); s.setFullName(input.getFullName().trim());
            s.setEmail(input.getEmail().trim()); s.setPhone(input.getPhone());
            s.setDepartment(input.getDepartment().trim()); s.setYearLevel(input.getYearLevel());
            try { return ResponseEntity.ok(students.save(s)); }
            catch (DataIntegrityViolationException ex) { return ResponseEntity.badRequest().body(Map.of("message", "Roll number or email already exists.")); }
        }).orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (!students.existsById(id)) return ResponseEntity.notFound().build();
        students.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

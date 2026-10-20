package com.college.sms.controller;

import com.college.sms.model.Result;
import com.college.sms.repository.*;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/results")
public class ResultController {
    private final ResultRepository results;
    private final StudentRepository students;
    private final CourseRepository courses;
    public ResultController(ResultRepository results, StudentRepository students, CourseRepository courses) {
        this.results = results; this.students = students; this.courses = courses;
    }
    @GetMapping public List<Result> all(@RequestParam(required = false) Long studentId) {
        return studentId == null ? results.findAll() : results.findByStudentId(studentId);
    }
    @PostMapping public ResponseEntity<?> create(@Valid @RequestBody ResultInput input) {
        var student = students.findById(input.studentId());
        var course = courses.findById(input.courseId());
        if (student.isEmpty() || course.isEmpty())
            return ResponseEntity.badRequest().body(Map.of("message", "Choose an existing student and course."));
        Result result = new Result();
        result.setStudent(student.get()); result.setCourse(course.get());
        result.setMarks(input.marks()); result.setSemester(input.semester());
        return ResponseEntity.status(201).body(results.save(result));
    }
    public record ResultInput(Long studentId, Long courseId,
                              @jakarta.validation.constraints.DecimalMin("0.0") @jakarta.validation.constraints.DecimalMax("100.0") Double marks,
                              @jakarta.validation.constraints.Min(1) @jakarta.validation.constraints.Max(8) Integer semester) {}
}

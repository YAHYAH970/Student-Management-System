package com.college.sms.controller;

import com.college.sms.model.Course;
import com.college.sms.repository.CourseRepository;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseRepository courses;
    public CourseController(CourseRepository courses) { this.courses = courses; }
    @GetMapping public List<Course> all() { return courses.findAll(); }
    @PostMapping public Course create(@Valid @RequestBody Course course) { return courses.save(course); }
    @DeleteMapping("/{id}") public void delete(@PathVariable Long id) { courses.deleteById(id); }
}

package com.college.sms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "results", uniqueConstraints = @UniqueConstraint(
        name = "uq_student_course_semester", columnNames = {"student_id", "course_id", "semester"}))
public class Result {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false) @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    @ManyToOne(optional = false) @JoinColumn(name = "course_id", nullable = false)
    private Course course;
    @DecimalMin("0.0") @DecimalMax("100.0") @Column(nullable = false)
private Double marks;
    @Min(1) @Max(8) @Column(nullable = false)
    private Integer semester;

    public Long getId() { return id; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
    public Double getMarks() { return marks; }
    public void setMarks(Double marks) { this.marks = marks; }
    public Integer getSemester() { return semester; }
    public void setSemester(Integer semester) { this.semester = semester; }
}

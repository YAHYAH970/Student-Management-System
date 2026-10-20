package com.college.sms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "students", indexes = {
    @Index(name = "idx_students_name", columnList = "full_name"),
    @Index(name = "idx_students_department", columnList = "department")
})
public class Student {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank @Column(name = "roll_number", nullable = false, unique = true, length = 30)
    private String rollNumber;
    @NotBlank @Column(name = "full_name", nullable = false, length = 120)
    private String fullName;
    @NotBlank @Email @Column(nullable = false, unique = true, length = 150)
    private String email;
    @Column(length = 20)
    private String phone;
    @NotBlank @Column(nullable = false, length = 80)
    private String department;
    @Min(1) @Max(4) @Column(name = "year_level", nullable = false)
    private Integer yearLevel;

    public Long getId() { return id; }
    public String getRollNumber() { return rollNumber; }
    public void setRollNumber(String rollNumber) { this.rollNumber = rollNumber; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public Integer getYearLevel() { return yearLevel; }
    public void setYearLevel(Integer yearLevel) { this.yearLevel = yearLevel; }
}

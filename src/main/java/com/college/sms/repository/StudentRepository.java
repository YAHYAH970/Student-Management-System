package com.college.sms.repository;
import com.college.sms.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query("SELECT s FROM Student s WHERE LOWER(s.fullName) LIKE LOWER(CONCAT('%', :q, '%')) OR LOWER(s.rollNumber) LIKE LOWER(CONCAT('%', :q, '%')) OR LOWER(s.department) LIKE LOWER(CONCAT('%', :q, '%'))")
    List<Student> search(@Param("q") String q);
}

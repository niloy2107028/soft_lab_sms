package com.student_management_system.niloy.repository;

import com.student_management_system.niloy.model.Student;
import com.student_management_system.niloy.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByStudentId(String studentId);
    Optional<Student> findByUserId(Long userId);
    List<Student> findByDepartment(Department department);
    List<Student> findByDepartmentId(Long departmentId);
    boolean existsByStudentId(String studentId);
}

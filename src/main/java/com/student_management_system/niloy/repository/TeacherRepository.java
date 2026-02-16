package com.student_management_system.niloy.repository;

import com.student_management_system.niloy.model.Teacher;
import com.student_management_system.niloy.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findByEmployeeId(String employeeId);
    Optional<Teacher> findByUserId(Long userId);
    List<Teacher> findByDepartment(Department department);
    List<Teacher> findByDepartmentId(Long departmentId);
    boolean existsByEmployeeId(String employeeId);
}

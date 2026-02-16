package com.student_management_system.niloy.repository;

import com.student_management_system.niloy.model.Course;
import com.student_management_system.niloy.model.Department;
import com.student_management_system.niloy.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByCourseCode(String courseCode);
    List<Course> findByDepartment(Department department);
    List<Course> findByDepartmentId(Long departmentId);
    List<Course> findByTeacher(Teacher teacher);
    List<Course> findByTeacherId(Long teacherId);
    boolean existsByCourseCode(String courseCode);
}

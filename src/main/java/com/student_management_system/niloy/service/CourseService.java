package com.student_management_system.niloy.service;

import com.student_management_system.niloy.model.Course;
import com.student_management_system.niloy.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Transactional
    public Course createCourse(Course course) {
        if (courseRepository.existsByCourseCode(course.getCourseCode())) {
            throw new RuntimeException("Course code already exists");
        }
        return courseRepository.save(course);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    public Optional<Course> getCourseByCourseCode(String courseCode) {
        return courseRepository.findByCourseCode(courseCode);
    }

    public List<Course> getCoursesByDepartmentId(Long departmentId) {
        return courseRepository.findByDepartmentId(departmentId);
    }

    public List<Course> getCoursesByTeacherId(Long teacherId) {
        return courseRepository.findByTeacherId(teacherId);
    }

    @Transactional
    public Course updateCourse(Long id, Course courseDetails) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        course.setCourseName(courseDetails.getCourseName());
        course.setDescription(courseDetails.getDescription());
        course.setCredits(courseDetails.getCredits());
        course.setDepartment(courseDetails.getDepartment());
        course.setTeacher(courseDetails.getTeacher());

        return courseRepository.save(course);
    }

    @Transactional
    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }
}

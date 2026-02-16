package com.student_management_system.niloy.service;

import com.student_management_system.niloy.model.Student;
import com.student_management_system.niloy.model.Course;
import com.student_management_system.niloy.repository.StudentRepository;
import com.student_management_system.niloy.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Transactional
    public Student createStudent(Student student) {
        if (student.getStudentId() != null && studentRepository.existsByStudentId(student.getStudentId())) {
            throw new RuntimeException("Student ID already exists");
        }
        return studentRepository.save(student);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    public Optional<Student> getStudentByUserId(Long userId) {
        return studentRepository.findByUserId(userId);
    }

    public Optional<Student> getStudentByStudentId(String studentId) {
        return studentRepository.findByStudentId(studentId);
    }

    public List<Student> getStudentsByDepartmentId(Long departmentId) {
        return studentRepository.findByDepartmentId(departmentId);
    }

    @Transactional
    public Student updateStudent(Long id, Student studentDetails) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        student.setFirstName(studentDetails.getFirstName());
        student.setLastName(studentDetails.getLastName());
        student.setPhone(studentDetails.getPhone());
        student.setAddress(studentDetails.getAddress());
        student.setDepartment(studentDetails.getDepartment());

        return studentRepository.save(student);
    }

    @Transactional
    public Student enrollInCourse(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (!student.getCourses().contains(course)) {
            student.getCourses().add(course);
            studentRepository.save(student);
        }

        return student;
    }

    @Transactional
    public Student unenrollFromCourse(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        student.getCourses().remove(course);
        return studentRepository.save(student);
    }

    @Transactional
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }
}

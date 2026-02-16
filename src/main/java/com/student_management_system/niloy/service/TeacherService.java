package com.student_management_system.niloy.service;

import com.student_management_system.niloy.model.Teacher;
import com.student_management_system.niloy.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    @Transactional
    public Teacher createTeacher(Teacher teacher) {
        if (teacher.getEmployeeId() != null && teacherRepository.existsByEmployeeId(teacher.getEmployeeId())) {
            throw new RuntimeException("Employee ID already exists");
        }
        return teacherRepository.save(teacher);
    }

    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    public Optional<Teacher> getTeacherById(Long id) {
        return teacherRepository.findById(id);
    }

    public Optional<Teacher> getTeacherByUserId(Long userId) {
        return teacherRepository.findByUserId(userId);
    }

    public Optional<Teacher> getTeacherByEmployeeId(String employeeId) {
        return teacherRepository.findByEmployeeId(employeeId);
    }

    public List<Teacher> getTeachersByDepartmentId(Long departmentId) {
        return teacherRepository.findByDepartmentId(departmentId);
    }

    @Transactional
    public Teacher updateTeacher(Long id, Teacher teacherDetails) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        teacher.setFirstName(teacherDetails.getFirstName());
        teacher.setLastName(teacherDetails.getLastName());
        teacher.setPhone(teacherDetails.getPhone());
        teacher.setAddress(teacherDetails.getAddress());
        teacher.setSpecialization(teacherDetails.getSpecialization());
        teacher.setDepartment(teacherDetails.getDepartment());

        return teacherRepository.save(teacher);
    }

    @Transactional
    public void deleteTeacher(Long id) {
        teacherRepository.deleteById(id);
    }
}

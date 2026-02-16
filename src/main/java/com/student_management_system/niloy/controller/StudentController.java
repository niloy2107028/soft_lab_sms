package com.student_management_system.niloy.controller;

import com.student_management_system.niloy.model.Student;
import com.student_management_system.niloy.model.User;
import com.student_management_system.niloy.model.Course;
import com.student_management_system.niloy.model.Department;
import com.student_management_system.niloy.service.StudentService;
import com.student_management_system.niloy.service.UserService;
import com.student_management_system.niloy.service.CourseService;
import com.student_management_system.niloy.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('STUDENT')")
    public String studentDashboard(Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username).orElseThrow();
        Student student = studentService.getStudentByUserId(user.getId()).orElseThrow();

        model.addAttribute("student", student);
        model.addAttribute("courses", student.getCourses());
        model.addAttribute("allCourses", courseService.getAllCourses());
        
        return "student-dashboard";
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('STUDENT')")
    public String studentProfile(Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username).orElseThrow();
        Student student = studentService.getStudentByUserId(user.getId()).orElseThrow();

        model.addAttribute("student", student);
        model.addAttribute("departments", departmentService.getAllDepartments());
        
        return "student-profile";
    }

    @PostMapping("/profile/update")
    @PreAuthorize("hasRole('STUDENT')")
    public String updateProfile(@ModelAttribute Student studentDetails, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username).orElseThrow();
        Student student = studentService.getStudentByUserId(user.getId()).orElseThrow();

        studentService.updateStudent(student.getId(), studentDetails);
        
        return "redirect:/student/profile?success";
    }

    @PostMapping("/enroll/{courseId}")
    @PreAuthorize("hasRole('STUDENT')")
    public String enrollInCourse(@PathVariable Long courseId, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username).orElseThrow();
        Student student = studentService.getStudentByUserId(user.getId()).orElseThrow();

        studentService.enrollInCourse(student.getId(), courseId);
        
        return "redirect:/student/dashboard?enrolled";
    }

    @PostMapping("/unenroll/{courseId}")
    @PreAuthorize("hasRole('STUDENT')")
    public String unenrollFromCourse(@PathVariable Long courseId, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username).orElseThrow();
        Student student = studentService.getStudentByUserId(user.getId()).orElseThrow();

        studentService.unenrollFromCourse(student.getId(), courseId);
        
        return "redirect:/student/dashboard?unenrolled";
    }
}

package com.student_management_system.niloy.controller;

import com.student_management_system.niloy.model.*;
import com.student_management_system.niloy.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('TEACHER')")
    public String teacherDashboard(Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username).orElseThrow();
        Teacher teacher = teacherService.getTeacherByUserId(user.getId()).orElseThrow();

        model.addAttribute("teacher", teacher);
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("teachers", teacherService.getAllTeachers());
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("departments", departmentService.getAllDepartments());
        
        return "teacher-dashboard";
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('TEACHER')")
    public String teacherProfile(Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username).orElseThrow();
        Teacher teacher = teacherService.getTeacherByUserId(user.getId()).orElseThrow();

        model.addAttribute("teacher", teacher);
        model.addAttribute("departments", departmentService.getAllDepartments());
        
        return "teacher-profile";
    }

    @PostMapping("/profile/update")
    @PreAuthorize("hasRole('TEACHER')")
    public String updateProfile(@ModelAttribute Teacher teacherDetails, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username).orElseThrow();
        Teacher teacher = teacherService.getTeacherByUserId(user.getId()).orElseThrow();

        teacherService.updateTeacher(teacher.getId(), teacherDetails);
        
        return "redirect:/teacher/profile?success";
    }

    // Student Management - Only accessible by teachers
    @GetMapping("/students")
    @PreAuthorize("hasRole('TEACHER')")
    public String manageStudents(Model model) {
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "manage-students";
    }

    @PostMapping("/students/create")
    @PreAuthorize("hasRole('TEACHER')")
    public String createStudent(@RequestParam String username,
                                @RequestParam String password,
                                @RequestParam String email,
                                @RequestParam String firstName,
                                @RequestParam String lastName,
                                @RequestParam String studentId,
                                @RequestParam Long departmentId) {
        try {
            User user = userService.createUser(username, password, email, Role.STUDENT);
            
            Department department = departmentService.getDepartmentById(departmentId).orElseThrow();
            
            Student student = new Student();
            student.setUser(user);
            student.setFirstName(firstName);
            student.setLastName(lastName);
            student.setStudentId(studentId);
            student.setDepartment(department);
            
            studentService.createStudent(student);
            
            return "redirect:/teacher/students?success";
        } catch (Exception e) {
            return "redirect:/teacher/students?error=" + e.getMessage();
        }
    }

    @PostMapping("/students/update/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public String updateStudent(@PathVariable Long id, @ModelAttribute Student studentDetails) {
        studentService.updateStudent(id, studentDetails);
        return "redirect:/teacher/students?updated";
    }

    @PostMapping("/students/delete/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public String deleteStudent(@PathVariable Long id) {
        Student student = studentService.getStudentById(id).orElseThrow();
        userService.deleteUser(student.getUser().getId());
        studentService.deleteStudent(id);
        return "redirect:/teacher/students?deleted";
    }

    // Teacher Management
    @GetMapping("/teachers")
    @PreAuthorize("hasRole('TEACHER')")
    public String manageTeachers(Model model) {
        model.addAttribute("teachers", teacherService.getAllTeachers());
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "manage-teachers";
    }

    @PostMapping("/teachers/create")
    @PreAuthorize("hasRole('TEACHER')")
    public String createTeacher(@RequestParam String username,
                                @RequestParam String password,
                                @RequestParam String email,
                                @RequestParam String firstName,
                                @RequestParam String lastName,
                                @RequestParam String employeeId,
                                @RequestParam Long departmentId) {
        try {
            User user = userService.createUser(username, password, email, Role.TEACHER);
            
            Department department = departmentService.getDepartmentById(departmentId).orElseThrow();
            
            Teacher teacher = new Teacher();
            teacher.setUser(user);
            teacher.setFirstName(firstName);
            teacher.setLastName(lastName);
            teacher.setEmployeeId(employeeId);
            teacher.setDepartment(department);
            
            teacherService.createTeacher(teacher);
            
            return "redirect:/teacher/teachers?success";
        } catch (Exception e) {
            return "redirect:/teacher/teachers?error=" + e.getMessage();
        }
    }

    @PostMapping("/teachers/update/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public String updateTeacher(@PathVariable Long id, @ModelAttribute Teacher teacherDetails) {
        teacherService.updateTeacher(id, teacherDetails);
        return "redirect:/teacher/teachers?updated";
    }

    @PostMapping("/teachers/delete/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public String deleteTeacher(@PathVariable Long id) {
        Teacher teacher = teacherService.getTeacherById(id).orElseThrow();
        userService.deleteUser(teacher.getUser().getId());
        teacherService.deleteTeacher(id);
        return "redirect:/teacher/teachers?deleted";
    }

    // Course Management
    @GetMapping("/courses")
    @PreAuthorize("hasRole('TEACHER')")
    public String manageCourses(Model model) {
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("teachers", teacherService.getAllTeachers());
        return "manage-courses";
    }

    @PostMapping("/courses/create")
    @PreAuthorize("hasRole('TEACHER')")
    public String createCourse(@ModelAttribute Course course) {
        courseService.createCourse(course);
        return "redirect:/teacher/courses?success";
    }

    @PostMapping("/courses/update/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public String updateCourse(@PathVariable Long id, @ModelAttribute Course courseDetails) {
        courseService.updateCourse(id, courseDetails);
        return "redirect:/teacher/courses?updated";
    }

    @PostMapping("/courses/delete/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public String deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return "redirect:/teacher/courses?deleted";
    }

    // Department Management
    @GetMapping("/departments")
    @PreAuthorize("hasRole('TEACHER')")
    public String manageDepartments(Model model) {
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "manage-departments";
    }

    @PostMapping("/departments/create")
    @PreAuthorize("hasRole('TEACHER')")
    public String createDepartment(@ModelAttribute Department department) {
        departmentService.createDepartment(department);
        return "redirect:/teacher/departments?success";
    }

    @PostMapping("/departments/update/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public String updateDepartment(@PathVariable Long id, @ModelAttribute Department departmentDetails) {
        departmentService.updateDepartment(id, departmentDetails);
        return "redirect:/teacher/departments?updated";
    }

    @PostMapping("/departments/delete/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public String deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return "redirect:/teacher/departments?deleted";
    }
}

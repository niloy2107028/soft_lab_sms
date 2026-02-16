package com.student_management_system.niloy.config;

import com.student_management_system.niloy.model.*;
import com.student_management_system.niloy.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private CourseService courseService;

    @Override
    public void run(String... args) throws Exception {
        // Check if data already exists
        if (departmentService.getAllDepartments().isEmpty()) {
            // Create Departments
            Department cse = new Department("Computer Science", "Department of Computer Science and Engineering");
            Department eee = new Department("Electrical Engineering", "Department of Electrical and Electronic Engineering");
            Department civil = new Department("Civil Engineering", "Department of Civil Engineering");
            
            cse = departmentService.createDepartment(cse);
            eee = departmentService.createDepartment(eee);
            civil = departmentService.createDepartment(civil);

            // Create Teacher User and Profile
            User teacherUser = userService.createUser("teacher1", "password123", "teacher@example.com", Role.TEACHER);
            Teacher teacher = new Teacher(teacherUser, "John", "Doe", "T001", cse);
            teacher.setPhone("123-456-7890");
            teacher.setSpecialization("Software Engineering");
            teacherService.createTeacher(teacher);

            // Create another Teacher
            User teacherUser2 = userService.createUser("teacher2", "password123", "teacher2@example.com", Role.TEACHER);
            Teacher teacher2 = new Teacher(teacherUser2, "Jane", "Smith", "T002", eee);
            teacher2.setPhone("123-456-7891");
            teacher2.setSpecialization("Power Systems");
            teacherService.createTeacher(teacher2);

            // Create Student User and Profile
            User studentUser = userService.createUser("student1", "password123", "student@example.com", Role.STUDENT);
            Student student = new Student(studentUser, "Alice", "Johnson", "S001", cse);
            student.setPhone("987-654-3210");
            student.setAddress("123 Main St");
            studentService.createStudent(student);

            // Create another Student
            User studentUser2 = userService.createUser("student2", "password123", "student2@example.com", Role.STUDENT);
            Student student2 = new Student(studentUser2, "Bob", "Williams", "S002", eee);
            student2.setPhone("987-654-3211");
            student2.setAddress("456 Oak Ave");
            studentService.createStudent(student2);

            // Create Courses
            Course course1 = new Course("CSE101", "Introduction to Programming", "Basic programming concepts using Java", 3);
            course1.setDepartment(cse);
            course1.setTeacher(teacher);
            courseService.createCourse(course1);

            Course course2 = new Course("CSE201", "Data Structures", "Data structures and algorithms", 4);
            course2.setDepartment(cse);
            course2.setTeacher(teacher);
            courseService.createCourse(course2);

            Course course3 = new Course("EEE101", "Circuit Analysis", "Fundamentals of circuit theory", 3);
            course3.setDepartment(eee);
            course3.setTeacher(teacher2);
            courseService.createCourse(course3);

            System.out.println("==============================================");
            System.out.println("Initial data loaded successfully!");
            System.out.println("==============================================");
            System.out.println("Teacher Login:");
            System.out.println("  Username: teacher1");
            System.out.println("  Password: password123");
            System.out.println("Student Login:");
            System.out.println("  Username: student1");
            System.out.println("  Password: password123");
            System.out.println("==============================================");
        }
    }
}

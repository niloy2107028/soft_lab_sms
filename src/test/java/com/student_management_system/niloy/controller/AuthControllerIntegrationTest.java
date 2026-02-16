package com.student_management_system.niloy.controller;

import com.student_management_system.niloy.model.Role;
import com.student_management_system.niloy.model.User;
import com.student_management_system.niloy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * INTEGRATION TEST Example
 * 
 * This is an INTEGRATION TEST for AuthController.
 * Integration tests check how multiple components work TOGETHER.
 * 
 * Key Concepts:
 * - @SpringBootTest: Loads the FULL Spring application context (like running the real app)
 * - @AutoConfigureMockMvc: Provides MockMvc to simulate HTTP requests
 * - Uses REAL database (H2 in-memory for tests)
 * - Tests the COMPLETE flow: Controller -> Service -> Repository -> Database
 * - Slower than unit tests but more realistic
 * - @Transactional: Each test rolls back database changes (clean state for each test)
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
@Transactional
@DisplayName("AuthController Integration Tests")
public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User testStudent;
    private User testTeacher;

    @BeforeEach
    void setUp() {
        // Clean database before each test
        userRepository.deleteAll();
        
        // Create a test student user
        testStudent = new User();
        testStudent.setUsername("studenttest");
        testStudent.setPassword(passwordEncoder.encode("password123"));
        testStudent.setEmail("student@test.com");
        testStudent.setRole(Role.STUDENT);
        testStudent.setEnabled(true);
        userRepository.save(testStudent);

        // Create a test teacher user
        testTeacher = new User();
        testTeacher.setUsername("teachertest");
        testTeacher.setPassword(passwordEncoder.encode("password123"));
        testTeacher.setEmail("teacher@test.com");
        testTeacher.setRole(Role.TEACHER);
        testTeacher.setEnabled(true);
        userRepository.save(testTeacher);
    }

    @Test
    @DisplayName("Should display login page for unauthenticated users")
    void testLoginPage_Unauthenticated() throws Exception {
        // Simulate GET request to /login
        mockMvc.perform(get("/login"))
            .andExpect(status().isOk())  // Expect HTTP 200
            .andExpect(view().name("login"))  // Expect login template
            .andExpect(content().string(org.hamcrest.Matchers.containsString("Student Management System")));
    }

    @Test
    @DisplayName("Should successfully login with valid student credentials")
    void testLogin_ValidStudentCredentials() throws Exception {
        // Simulate form login with valid credentials
        mockMvc.perform(formLogin("/login")
                .user("studenttest")
                .password("password123"))
            .andExpect(authenticated().withUsername("studenttest"))
            .andExpect(authenticated().withRoles("STUDENT"))
            .andExpect(status().is3xxRedirection())  // Expect redirect after login
            .andExpect(redirectedUrl("/dashboard"));
    }

    @Test
    @DisplayName("Should successfully login with valid teacher credentials")
    void testLogin_ValidTeacherCredentials() throws Exception {
        mockMvc.perform(formLogin("/login")
                .user("teachertest")
                .password("password123"))
            .andExpect(authenticated().withUsername("teachertest"))
            .andExpect(authenticated().withRoles("TEACHER"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/dashboard"));
    }

    @Test
    @DisplayName("Should fail login with invalid credentials")
    void testLogin_InvalidCredentials() throws Exception {
        mockMvc.perform(formLogin("/login")
                .user("studenttest")
                .password("wrongpassword"))
            .andExpect(unauthenticated())
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/login?error"));
    }

    @Test
    @DisplayName("Should fail login with non-existent user")
    void testLogin_NonExistentUser() throws Exception {
        mockMvc.perform(formLogin("/login")
                .user("nonexistent")
                .password("password123"))
            .andExpect(unauthenticated())
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/login?error"));
    }

    @Test
    @DisplayName("Should redirect root path to login for unauthenticated users")
    void testRootPath_RedirectsToLogin() throws Exception {
        mockMvc.perform(get("/"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/login"));
    }

    @Test
    @DisplayName("Should deny access to dashboard without authentication")
    void testDashboard_Unauthenticated() throws Exception {
        mockMvc.perform(get("/dashboard"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrlPattern("**/login"));
    }
}

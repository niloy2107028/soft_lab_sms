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
 * ============================================
 * 🔗 SIMPLE INTEGRATION TEST EXAMPLE
 * ============================================
 * 
 * What is this?
 * This tests how MULTIPLE parts work TOGETHER.
 * Think of it like test-driving a whole car - not just the engine.
 * 
 * Why "Integration" Test?
 * - Tests Controller + Service + Database together
 * - Uses a REAL (but temporary) database (H2)
 * - Starts the whole Spring application
 * - SLOWER than unit tests (takes seconds, not milliseconds)
 * - More realistic - tests actual user scenarios
 * 
 * When to use Integration Tests?
 * - Testing login/authentication flows
 * - Testing complete user journeys
 * - Making sure components work together
 * - Before deploying to production
 */
@SpringBootTest  // Starts the whole application
@AutoConfigureMockMvc  // Allows us to simulate HTTP requests
@TestPropertySource(properties = {
    // Use H2 in-memory database for testing (temporary, deleted after tests)
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
@Transactional  // Rolls back database changes after each test
@DisplayName("Simple Integration Test Example - Login")
public class AuthControllerIntegrationTest {

    // ========================================
    // Step 1: Set Up Test Tools
    // ========================================
    
    // MockMvc simulates HTTP requests (like a browser)
    @Autowired
    private MockMvc mockMvc;

    // Real database repository (using H2 in-memory)
    @Autowired
    private UserRepository userRepository;

    // Real password encoder
    @Autowired
    private PasswordEncoder passwordEncoder;

    private User testStudent;

    // This runs BEFORE each test
    @BeforeEach
    void setUp() {
        // Clean database
        userRepository.deleteAll();
        
        // Create a test user in the database
        testStudent = new User();
        testStudent.setUsername("student123");
        testStudent.setPassword(passwordEncoder.encode("password123"));
        testStudent.setEmail("student@test.com");
        testStudent.setRole(Role.STUDENT);
        testStudent.setEnabled(true);
        
        // Save to database
        userRepository.save(testStudent);
    }

    // ========================================
    // Test 1: View Login Page
    // ========================================
    @Test
    @DisplayName("Test 1: Anyone can see the login page")
    void shouldDisplayLoginPage() throws Exception {
        
        // ARRANGE: Nothing needed
        
        // ACT: Simulate GET request to /login
        // This is like typing "localhost:8081/login" in browser
        mockMvc.perform(get("/login"))
        
            // ASSERT: Check the response
            .andExpect(status().isOk())  // Should return HTTP 200 (success)
            .andExpect(view().name("login"))  // Should show login.html template
            .andExpect(content().string(
                org.hamcrest.Matchers.containsString("Student Management System")
            ));  // Page should contain this text
    }

    // ========================================
    // Test 2: Login with CORRECT Password
    // ========================================
    @Test
    @DisplayName("Test 2: Should login successfully with correct password")
    void shouldLoginWithCorrectCredentials() throws Exception {
        
        // ACT: Simulate submitting login form
        // Like typing username & password and clicking "Login" button
        mockMvc.perform(formLogin("/login")
                .user("student123")       // Type username
                .password("password123")) // Type password
        
            // ASSERT: Check what happened
            .andExpect(authenticated()  // User should be logged in
                .withUsername("student123"))  // Logged in as this user
            .andExpect(authenticated()
                .withRoles("STUDENT"))  // Should have STUDENT role
            .andExpect(status().is3xxRedirection())  // Should redirect (HTTP 302)
            .andExpect(redirectedUrl("/dashboard"));  // Redirect to dashboard
    }

    // ========================================
    // Test 3: Login with WRONG Password
    // ========================================
    @Test
    @DisplayName("Test 3: Should fail with wrong password")
    void shouldFailWithWrongPassword() throws Exception {
        
        // ACT: Try to login with wrong password
        mockMvc.perform(formLogin("/login")
                .user("student123")
                .password("wrongpassword"))  // Wrong password!
        
            // ASSERT: Login should fail
            .andExpect(unauthenticated())  // User is NOT logged in
            .andExpect(status().is3xxRedirection())  // Redirects back
            .andExpect(redirectedUrl("/login?error"));  // Shows error message
    }

    // ========================================
    // Test 4: Login with User That Doesn't Exist
    // ========================================
    @Test
    @DisplayName("Test 4: Should fail with non-existent user")
    void shouldFailWithNonExistentUser() throws Exception {
        
        // ACT: Try to login with username that doesn't exist
        mockMvc.perform(formLogin("/login")
                .user("nobody")  // This user doesn't exist
                .password("password123"))
        
            // ASSERT: Login should fail
            .andExpect(unauthenticated())
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/login?error"));
    }

    // ========================================
    // Test 5: Access Dashboard WITHOUT Login
    // ========================================
    @Test
    @DisplayName("Test 5: Cannot access dashboard without login")
    void shouldBlockDashboardWithoutLogin() throws Exception {
        
        // ACT: Try to access dashboard without logging in
        mockMvc.perform(get("/dashboard"))
        
            // ASSERT: Should be redirected to login page
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrlPattern("**/login"));
    }
}

/**
 * ============================================
 * 📚 KEY CONCEPTS EXPLAINED
 * ============================================
 * 
 * 1. @SpringBootTest
 *    - Starts the ENTIRE application
 *    - Like running your app normally, but for testing
 * 
 * 2. @AutoConfigureMockMvc
 *    - Gives us MockMvc tool
 *    - MockMvc simulates HTTP requests (like a browser)
 * 
 * 3. mockMvc.perform(get("/url"))
 *    - Simulates visiting a URL
 *    - Like typing URL in browser
 * 
 * 4. formLogin()
 *    - Simulates submitting a login form
 *    - Tests username + password login
 * 
 * 5. .andExpect()
 *    - Checks what the response is
 *    - Examples:
 *      - status().isOk() = HTTP 200 (success)
 *      - authenticated() = User is logged in
 *      - redirectedUrl() = Redirected to this page
 * 
 * 6. H2 Database
 *    - Temporary in-memory database
 *    - Created when test starts
 *    - Deleted when test ends
 *    - Perfect for testing
 * 
 * 7. @Transactional
 *    - Rolls back database changes after each test
 *    - Keeps tests independent
 * 
 * ============================================
 * 🏃 HOW TO RUN THIS TEST
 * ============================================
 * 
 * Option 1: In VS Code
 * - Right-click on this file
 * - Click "Run Tests"
 * 
 * Option 2: In Docker
 * docker-compose run app ./mvnw test -Dtest=AuthControllerIntegrationTest
 * 
 * Option 3: What to expect
 * - First time: Takes 10-20 seconds (starts Spring application)
 * - Next times: Faster (Spring context is cached)
 * - Green ✅ = All tests passed
 * - Red ❌ = Some tests failed (check which one)
 * 
 * ============================================
 * 🔍 DIFFERENCE FROM UNIT TEST
 * ============================================
 * 
 * Unit Test (UserServiceTest):
 * ✓ Tests ONE thing
 * ✓ Uses fake objects (@Mock)
 * ✓ No database
 * ✓ Very fast (milliseconds)
 * ✗ Doesn't test real scenarios
 * 
 * Integration Test (This file):
 * ✓ Tests MULTIPLE things together
 * ✓ Uses real Spring application
 * ✓ Uses real (H2) database
 * ✓ Tests realistic user scenarios
 * ✗ Slower (seconds)
 * 
 * Both are important! Use both types.
 * ============================================
 */

package com.student_management_system.niloy.controller;

import com.student_management_system.niloy.model.Role;
import com.student_management_system.niloy.model.User;
import com.student_management_system.niloy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * ============================================
 * 🔗 BEGINNER-FRIENDLY INTEGRATION TEST
 * ============================================
 * 
 * 📚 WHAT IS THIS FILE?
 * ---------------------
 * This tests if MULTIPLE parts of your app work TOGETHER correctly.
 * 
 * 🎯 INTEGRATION TEST vs UNIT TEST:
 * ----------------------------------
 * Unit Test (UserServiceTest) = Testing ONE piece alone (like testing just the engine)
 * Integration Test (THIS file) = Testing MANY pieces together (like driving the whole car)
 * 
 * 🔍 WHAT DOES THIS TEST?
 * ------------------------
 * 1. Can users see the login page?
 * 2. Are protected pages blocked from non-logged-in users?
 * 
 * ⚙️ HOW IT WORKS:
 * ----------------
 * - Starts your WHOLE Spring Boot application (just for testing)
 * - Creates a TEMPORARY database (H2) that disappears after tests
 * - Simulates a web browser visiting your pages
 * - Checks if everything works correctly
 * 
 * ⏱️ SPEED:
 * ----------
 * Slower than unit tests (takes 5-10 seconds) because it starts the whole app
 */

@SpringBootTest  // 👉 This starts your entire Spring Boot application
@TestPropertySource(properties = {
    // 👉 Use temporary H2 database (in memory, deleted after tests)
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
@Transactional  // 👉 Automatically cleans up database after each test
public class AuthControllerIntegrationTest {

    // ========================================
    // TOOLS WE NEED FOR TESTING
    // ========================================
    
    // MockMvc = Pretends to be a web browser
    private MockMvc mockMvc;

    // Get Spring application context (all your app's components)
    @Autowired
    private WebApplicationContext webApplicationContext;

    // Real database access
    @Autowired
    private UserRepository userRepository;

    // Real password encoder
    @Autowired
    private PasswordEncoder passwordEncoder;

    // ========================================
    // SETUP: Runs BEFORE Each Test
    // ========================================
    @BeforeEach
    void setUp() {
        // Set up our "fake browser" (MockMvc)
        this.mockMvc = MockMvcBuilders
            .webAppContextSetup(webApplicationContext)
            .apply(org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity())
            .build();
        
        // Create a test user in the temporary database
        User testUser = new User();
        testUser.setUsername("student123");
        testUser.setPassword(passwordEncoder.encode("password123"));
        testUser.setEmail("student@test.com");
        testUser.setRole(Role.STUDENT);
        testUser.setEnabled(true);
        
        userRepository.save(testUser);  // Save to H2 database
    }

    // ========================================
    // ✅ TEST 1: Can Anyone See Login Page?
    // ========================================
    @Test
    void test1_AnyoneCanViewLoginPage() throws Exception {
        
        // 📖 WHAT ARE WE TESTING?
        // We're checking if the login page shows up when someone visits it.
        // This is like opening a webpage in your browser.
        
        // 🎬 ACT: Visit the /login page (like clicking a link)
        mockMvc.perform(get("/login"))
        
            // ✅ ASSERT: Check what happened
            .andExpect(status().isOk())  // HTTP 200 = Page loaded successfully
            .andExpect(view().name("login"));  // It shows the login.html page
        
        // 💡 WHY THIS TEST MATTERS:
        // If this fails, users can't even see your login page!
    }

    // ========================================
    // ✅ TEST 2: Are Protected Pages Blocked?
    // ========================================
    @Test
    void test2_DashboardIsBlockedWithoutLogin() throws Exception {
        
        // 📖 WHAT ARE WE TESTING?
        // We're checking if the dashboard is protected.
        // Users who aren't logged in should NOT be able to see it.
        
        // 🎬 ACT: Try to visit /dashboard WITHOUT logging in first
        mockMvc.perform(get("/dashboard"))
        
            // ✅ ASSERT: Should redirect us to login page
            .andExpect(status().is3xxRedirection());  // HTTP 302 = Redirected
        
        // 💡 WHY THIS TEST MATTERS:
        // This protects your app! If this test fails, anyone can see
        // private student data without logging in. That's bad!
    }
}

/**
 * ============================================
 * 📚 WHAT DID WE JUST TEST?
 * ============================================
 * 
 * Test 1: Login Page Works ✅
 * - Users can see the login page
 * - No errors when visiting /login
 * 
 * Test 2: Security Works ✅
 * - Dashboard is protected
 * - Non-logged-in users are redirected to login
 * 
 * ============================================
 * 🎓 KEY CONCEPTS FOR BEGINNERS
 * ============================================
 * 
 * 1️⃣ @SpringBootTest
 *    Starts your entire app (like pressing "Run" in VS Code)
 * 
 * 2️⃣ MockMvc
 *    Pretends to be a web browser. It can:
 *    - Visit pages (GET requests)
 *    - Submit forms (POST requests)
 *    - Check what the response is
 * 
 * 3️⃣ mockMvc.perform(get("/url"))
 *    Visits a URL (like typing "localhost:8081/login" in browser)
 * 
 * 4️⃣ .andExpect(...)
 *    Checks if the result is what we expect
 *    Examples:
 *    - status().isOk() = Page loaded fine (HTTP 200)
 *    - status().is3xxRedirection() = Got redirected (HTTP 302)
 *    - view().name("login") = Shows the login.html page
 * 
 * 5️⃣ H2 Database
 *    A temporary database that exists only during testing.
 *    Created when test starts → Deleted when test ends
 *    Your real PostgreSQL database is NOT affected!
 * 
 * ============================================
 * 🔍 INTEGRATION vs UNIT TEST
 * ============================================
 * 
 * UNIT TEST (UserServiceTest.java):
 * ✓ Tests ONE class alone
 * ✓ Uses fake objects (@Mock)
 * ✓ No database needed
 * ✓ Very fast (milliseconds)
 * ✓ Example: Does getUserByUsername() work?
 * 
 * INTEGRATION TEST (THIS file):
 * ✓ Tests MANY parts together
 * ✓ Uses real Spring application
 * ✓ Uses real (temporary) database
 * ✓ Slower (5-10 seconds)
 * ✓ Example: Can users visit protected pages?
 * 
 * BOTH ARE IMPORTANT! Use both types of tests.
 * 
 * ============================================
 * 🏃 HOW TO RUN THIS TEST
 * ============================================
 * 
 * In Terminal (PowerShell):
 *   $env:JAVA_HOME = "C:\Program Files\Java\jdk-25"
 *   ./mvnw.cmd test -Dtest=AuthControllerIntegrationTest
 * 
 * Or run ALL tests:
 *   ./mvnw.cmd test
 * 
 * In VS Code:
 *   Click the ▶️ button above the class name
 * 
 * What to expect:
 *   ✅ Tests run: 2, Failures: 0, Errors: 0
 *   ✅ BUILD SUCCESS
 *   Takes 5-10 seconds (slower than unit tests)
 * 
 * ============================================
 */

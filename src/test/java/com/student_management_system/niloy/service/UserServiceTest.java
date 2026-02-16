package com.student_management_system.niloy.service;

import com.student_management_system.niloy.model.Role;
import com.student_management_system.niloy.model.User;
import com.student_management_system.niloy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * ============================================
 * 🎯 SIMPLE UNIT TEST EXAMPLE
 * ============================================
 * 
 * What is this?
 * This file tests the UserService class in ISOLATION.
 * Think of it like testing just the engine of a car - not the whole car.
 * 
 * Why "Unit" Test?
 * - Tests ONE small piece (unit) of code
 * - Uses FAKE objects (mocks) instead of real database
 * - FAST - runs in milliseconds
 * - No need to start the whole application
 * 
 * When to use Unit Tests?
 * - Testing business logic in Service classes
 * - Testing calculations or data transformations
 * - When you want fast feedback
 */
@DisplayName("Simple Unit Test Example - UserService")
public class UserServiceTest {

    // ========================================
    // Step 1: Set Up Test Tools
    // ========================================
    
    // @Mock creates a FAKE UserRepository
    // It doesn't connect to real database
    @Mock
    private UserRepository userRepository;

    // @InjectMocks creates a REAL UserService
    // But injects the FAKE repository into it
    @InjectMocks
    private UserService userService;

    private User testUser;

    // This runs BEFORE each test
    @BeforeEach
    void setUp() {
        // Initialize the mocks
        MockitoAnnotations.openMocks(this);
        
        // Create a sample user for testing
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("student123");
        testUser.setEmail("student@test.com");
        testUser.setRole(Role.STUDENT);
    }

    // ========================================
    // Test 1: Find User That EXISTS
    // ========================================
    @Test
    @DisplayName("Test 1: Should find user when username exists")
    void shouldFindUserWhenUsernameExists() {
        
        // STEP 1: ARRANGE (Prepare)
        // Tell the fake repository: "When someone asks for 'student123', return testUser"
        when(userRepository.findByUsername("student123"))
            .thenReturn(Optional.of(testUser));

        // STEP 2: ACT (Execute)
        // Call the real method we want to test
        Optional<User> result = userService.getUserByUsername("student123");

        // STEP 3: ASSERT (Check Result)
        // Check if we got the user back
        assertTrue(result.isPresent(), "User should be found");
        assertEquals("student123", result.get().getUsername(), "Username should match");
        assertEquals("student@test.com", result.get().getEmail(), "Email should match");
        
        // Verify the repository method was called exactly once
        verify(userRepository, times(1)).findByUsername("student123");
    }

    // ========================================
    // Test 2: Find User That DOESN'T EXIST
    // ========================================
    @Test
    @DisplayName("Test 2: Should return empty when user not found")
    void shouldReturnEmptyWhenUserNotFound() {
        
        // ARRANGE: Tell fake repository to return nothing
        when(userRepository.findByUsername("unknown"))
            .thenReturn(Optional.empty());

        // ACT: Try to find non-existent user
        Optional<User> result = userService.getUserByUsername("unknown");

        // ASSERT: Should get empty result
        assertFalse(result.isPresent(), "User should NOT be found");
        
        // Verify the method was called
        verify(userRepository, times(1)).findByUsername("unknown");
    }

    // ========================================
    // Test 3: Check Username Exists
    // ========================================
    @Test
    @DisplayName("Test 3: Should check if username exists")
    void shouldCheckIfUsernameExists() {
        
        // ARRANGE: Tell repository username exists
        when(userRepository.existsByUsername("student123"))
            .thenReturn(true);

        // ACT: This just demonstrates checking - userService doesn't have this method,
        // but this shows how you would test it
        boolean exists = userRepository.existsByUsername("student123");

        // ASSERT: Should return true
        assertTrue(exists, "Username should exist");
    }
}

/**
 * ============================================
 * 📚 KEY CONCEPTS EXPLAINED
 * ============================================
 * 
 * 1. @Mock
 *    - Creates a FAKE object
 *    - Doesn't connect to real database
 *    - You control what it returns
 * 
 * 2. @InjectMocks
 *    - Creates the REAL object you're testing
 *    - Automatically uses the @Mock objects
 * 
 * 3. when().thenReturn()
 *    - "When this method is called, return this value"
 *    - Example: when(repo.find("bob")).thenReturn(bobUser);
 * 
 * 4. verify()
 *    - Checks if a method was called
 *    - Example: verify(repo, times(1)).find("bob");
 *    - Makes sure the method was used correctly
 * 
 * 5. assertEquals(expected, actual)
 *    - Checks if two values are equal
 *    - If not equal, test fails
 * 
 * 6. assertTrue() / assertFalse()
 *    - Checks if something is true/false
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
 * docker-compose run app ./mvnw test -Dtest=UserServiceTest
 * 
 * Option 3: See if tests pass/fail
 * - Green ✅ = Test passed
 * - Red ❌ = Test failed
 * 
 * ============================================
 */

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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * UNIT TEST Example
 * 
 * This is a UNIT TEST for UserService class.
 * Unit tests focus on testing a SINGLE unit/component in isolation.
 * 
 * Key Concepts:
 * - @Mock: Creates a fake/mock object (doesn't use real database)
 * - @InjectMocks: Injects the mocked dependencies into the class being tested
 * - We test ONE service at a time without depending on other components
 * - Fast execution (no database, no Spring context)
 */
@DisplayName("UserService Unit Tests")
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        // Initialize mocks before each test
        MockitoAnnotations.openMocks(this);
        
        // Create a test user
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("encodedPassword123");
        testUser.setEmail("test@example.com");
        testUser.setRole(Role.STUDENT);
        testUser.setEnabled(true);
    }

    @Test
    @DisplayName("Should successfully load user by username")
    void testLoadUserByUsername_Success() {
        // ARRANGE: Set up test data and mock behavior
        when(userRepository.findByUsername("testuser"))
            .thenReturn(Optional.of(testUser));

        // ACT: Execute the method being tested
        UserDetails userDetails = userService.loadUserByUsername("testuser");

        // ASSERT: Verify the results
        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertEquals("encodedPassword123", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
            .anyMatch(auth -> auth.getAuthority().equals("ROLE_STUDENT")));
        
        // Verify that the repository method was called exactly once
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    @DisplayName("Should throw exception when user not found")
    void testLoadUserByUsername_UserNotFound() {
        // ARRANGE: Mock repository to return empty
        when(userRepository.findByUsername("nonexistent"))
            .thenReturn(Optional.empty());

        // ACT & ASSERT: Verify exception is thrown
        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername("nonexistent");
        });
        
        verify(userRepository, times(1)).findByUsername("nonexistent");
    }

    @Test
    @DisplayName("Should create new user successfully")
    void testCreateUser_Success() {
        // ARRANGE
        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // ACT
        User createdUser = userService.createUser("newuser", "password123", 
                                                   "new@example.com", Role.STUDENT);

        // ASSERT
        assertNotNull(createdUser);
        assertEquals(1L, createdUser.getId());
        verify(userRepository, times(1)).existsByUsername("newuser");
        verify(userRepository, times(1)).existsByEmail("new@example.com");
        verify(passwordEncoder, times(1)).encode("password123");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when username already exists")
    void testCreateUser_UsernameExists() {
        // ARRANGE
        when(userRepository.existsByUsername("existinguser")).thenReturn(true);

        // ACT & ASSERT
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.createUser("existinguser", "password123", 
                                   "test@example.com", Role.STUDENT);
        });
        
        assertEquals("Username already exists", exception.getMessage());
        verify(userRepository, times(1)).existsByUsername("existinguser");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when email already exists")
    void testCreateUser_EmailExists() {
        // ARRANGE
        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        // ACT & ASSERT
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.createUser("newuser", "password123", 
                                   "existing@example.com", Role.STUDENT);
        });
        
        assertEquals("Email already exists", exception.getMessage());
        verify(userRepository, times(1)).existsByEmail("existing@example.com");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should get user by username")
    void testGetUserByUsername_Success() {
        // ARRANGE
        when(userRepository.findByUsername("testuser"))
            .thenReturn(Optional.of(testUser));

        // ACT
        Optional<User> foundUser = userService.getUserByUsername("testuser");

        // ASSERT
        assertTrue(foundUser.isPresent());
        assertEquals("testuser", foundUser.get().getUsername());
        assertEquals("test@example.com", foundUser.get().getEmail());
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    @DisplayName("Should return empty when username not found")
    void testGetUserByUsername_NotFound() {
        // ARRANGE
        when(userRepository.findByUsername("nonexistent"))
            .thenReturn(Optional.empty());

        // ACT
        Optional<User> foundUser = userService.getUserByUsername("nonexistent");

        // ASSERT
        assertFalse(foundUser.isPresent());
        verify(userRepository, times(1)).findByUsername("nonexistent");
    }
}

# Testing Guide - Student Management System

## 📚 What is Testing?

Testing verifies that your code works correctly. Imagine you're building a car - you wouldn't sell it without testing the brakes, right? Same with software!

## 🎯 Types of Tests We Added

### 1. **Unit Tests** (UserServiceTest.java)

Located: `src/test/java/.../service/UserServiceTest.java`

**What it does:**

- Tests ONE component in isolation (just the UserService)
- Uses **mocks** (fake objects) instead of real database
- Very FAST (runs in milliseconds)

**Analogy:** Like testing just the car engine on a test bench - not the whole car.

**Key Concepts:**

```java
@Mock  // Creates a fake/dummy object
private UserRepository userRepository;

@InjectMocks  // Injects the mocks into the class being tested
private UserService userService;
```

**The 3 A's Pattern:**

1. **ARRANGE**: Set up test data
2. **ACT**: Run the method you're testing
3. **ASSERT**: Check if the result is correct

**Example from our code:**

```java
@Test
void testLoadUserByUsername_Success() {
    // ARRANGE: Mock the repository to return a user
    when(userRepository.findByUsername("testuser"))
        .thenReturn(Optional.of(testUser));

    // ACT: Call the method
    UserDetails userDetails = userService.loadUserByUsername("testuser");

    // ASSERT: Verify it worked
    assertNotNull(userDetails);
    assertEquals("testuser", userDetails.getUsername());
}
```

### 2. **Integration Tests** (AuthControllerIntegrationTest.java)

Located: `src/test/java/.../controller/AuthControllerIntegrationTest.java`

**What it does:**

- Tests how MULTIPLE components work TOGETHER
- Uses real Spring context (loads the whole application)
- Uses H2 in-memory database (temporary database just for tests)
- SLOWER than unit tests but more realistic

**Analogy:** Like test-driving the whole car on a track - testing everything together.

**Key Concepts:**

```java
@SpringBootTest  // Starts the entire Spring application
@AutoConfigureMockMvc  // Allows simulating HTTP requests
@Transactional  // Rolls back database changes after each test
```

**Example from our code:**

```java
@Test
void testLogin_ValidStudentCredentials() throws Exception {
    // Simulate a real login form submission
    mockMvc.perform(formLogin("/login")
            .user("studenttest")
            .password("password123"))
        .andExpect(authenticated())  // Check user is authenticated
        .andExpect(redirectedUrl("/dashboard"));  // Check redirect
}
```

## 🔄 Running the Tests

### Option 1: Using Maven Command

```bash
# Run all tests
mvn test

# Run only unit tests
mvn test -Dtest=UserServiceTest

# Run only integration tests
mvn test -Dtest=AuthControllerIntegrationTest
```

### Option 2: Using IDE

- Right-click on the test file → "Run Tests"
- Or click the green play button next to test methods

### Option 3: In Docker

```bash
docker-compose run app mvn test
```

## 📊 Understanding Test Results

When tests run, you'll see output like:

```
[INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0
```

- **Tests run**: Total tests executed
- **Failures**: Tests that failed (assertions didn't pass)
- **Errors**: Tests that crashed
- **Skipped**: Tests marked as @Disabled

## ✅ What We Test

### UserServiceTest (Unit Tests)

1. ✓ Loading user by username (success case)
2. ✓ Loading user by username (user not found)
3. ✓ Creating a new user (success)
4. ✓ Creating user with existing username (error)
5. ✓ Creating user with existing email (error)
6. ✓ Getting user by username
7. ✓ Getting user when not found

### AuthControllerIntegrationTest (Integration Tests)

1. ✓ Displaying login page
2. ✓ Login with valid student credentials
3. ✓ Login with valid teacher credentials
4. ✓ Login with wrong password
5. ✓ Login with non-existent user
6. ✓ Redirecting root path to login
7. ✓ Denying unauthenticated access to dashboard

## 🎓 Key Testing Terms

| Term                                   | Meaning                            |
| -------------------------------------- | ---------------------------------- |
| **@Test**                              | Marks a method as a test           |
| **@BeforeEach**                        | Runs before each test (setup)      |
| **assertEquals(expected, actual)**     | Checks if two values are equal     |
| **assertTrue(condition)**              | Checks if something is true        |
| **assertThrows(Exception.class, ...)** | Checks if code throws an exception |
| **verify(mock, times(1))**             | Checks if a mock method was called |
| **when(...).thenReturn(...)**          | Defines mock behavior              |

## 🚀 Best Practices

1. **Test Names Should Be Descriptive**
   - Good: `testLoadUserByUsername_Success()`
   - Bad: `test1()`

2. **One Test, One Thing**
   - Each test should verify ONE specific behavior

3. **Tests Should Be Independent**
   - Tests shouldn't depend on each other
   - Each test should work alone

4. **Keep Tests Fast**
   - Unit tests should run in milliseconds
   - Integration tests in seconds

## 🐛 Common Issues

### "Connection refused" error

→ Integration tests need H2 database dependency (already added in pom.xml)

### "Bean not found" error

→ Make sure @SpringBootTest is present for integration tests

### Tests pass locally but fail in CI

→ Check database setup and environment variables

## 📁 Test Structure

```
src/test/java/com/student_management_system/niloy/
├── NiloyApplicationTests.java          # Basic app test
├── service/
│   └── UserServiceTest.java            # Unit tests
└── controller/
    └── AuthControllerIntegrationTest.java  # Integration tests
```

## 🎯 Next Steps for Learning

1. Try breaking a test (change an assertion) to see it fail
2. Add more tests for other services (StudentService, TeacherService)
3. Learn about test coverage tools (JaCoCo)
4. Explore Behavior-Driven Development (BDD) with Cucumber

## 💡 Why Testing Matters

- **Confidence**: Know your code works before deploying
- **Documentation**: Tests show how to use your code
- **Refactoring**: Change code safely without breaking things
- **Bug Prevention**: Catch bugs early before users do
- **Professional Development**: Required skill in industry

---

Happy Testing! 🎉

# 🧪 SIMPLE TESTING GUIDE - Quick Reference

## 📍 WHERE ARE THE TEST FILES?

```
Your Project Folder (niloy)
│
└── 📁 src
    └── 📁 test
        └── 📁 java
            └── 📁 com
                └── 📁 student_management_system
                    └── 📁 niloy
                        │
                        ├── 📁 service/
                        │   └── 📄 UserServiceTest.java  ⬅️ UNIT TEST (Simple!)
                        │
                        └── 📁 controller/
                            └── 📄 AuthControllerIntegrationTest.java  ⬅️ INTEGRATION TEST (Simple!)
```

---

## 🎯 TWO TYPES OF TESTS

### 1️⃣ UNIT TEST - UserServiceTest.java

**Location:** `src/test/java/.../service/UserServiceTest.java`

**What it does:**

```
Tests ONLY UserService
[UserService] ✅ <--- We test just this
```

**Like:** Testing just the car engine on a workbench

**Speed:** ⚡ Very fast (milliseconds)

**Uses:** Fake objects (mocks) - no real database

**Tests in this file:**

1. ✅ Find a user that exists
2. ✅ Try to find a user that doesn't exist
3. ✅ Check if a username exists

---

### 2️⃣ INTEGRATION TEST - AuthControllerIntegrationTest.java

**Location:** `src/test/java/.../controller/AuthControllerIntegrationTest.java`

**What it does:**

```
Tests Controller + Service + Database together
[Controller] --> [Service] --> [Database] ✅ All working together
```

**Like:** Test-driving a complete car

**Speed:** 🐢 Slower (a few seconds)

**Uses:** Real Spring application + Real H2 database (temporary)

**Tests in this file:**

1. ✅ Can anyone view the login page?
2. ✅ Login with correct username and password
3. ✅ Login fails with wrong password
4. ✅ Login fails with non-existent user
5. ✅ Cannot access dashboard without login

---

## 🏃 HOW TO RUN TESTS

### Option 1: Using Docker (Easiest)

```bash
docker-compose run app ./mvnw test
```

### Option 2: In VS Code

1. Open the test file
2. Right-click anywhere in the file
3. Click **"Run Tests"**
4. See results at bottom of screen

### Option 3: Run specific test

```bash
# Run only unit tests
docker-compose run app ./mvnw test -Dtest=UserServiceTest

# Run only integration tests
docker-compose run app ./mvnw test -Dtest=AuthControllerIntegrationTest
```

---

## 📖 UNDERSTANDING THE TEST CODE

### Every test follows this pattern:

```java
@Test  // This marks it as a test
@DisplayName("Human-readable description")
void testSomething() {

    // STEP 1: ARRANGE (Prepare)
    // Set up test data

    // STEP 2: ACT (Execute)
    // Run the code you want to test

    // STEP 3: ASSERT (Check)
    // Verify the result is correct
}
```

### Example from UserServiceTest:

```java
@Test
@DisplayName("Test 1: Should find user when username exists")
void shouldFindUserWhenUsernameExists() {

    // ARRANGE: Tell fake repository what to return
    when(userRepository.findByUsername("student123"))
        .thenReturn(Optional.of(testUser));

    // ACT: Call the method we're testing
    Optional<User> result = userService.getUserByUsername("student123");

    // ASSERT: Check if we got the right answer
    assertTrue(result.isPresent());  // User should be found
    assertEquals("student123", result.get().getUsername());
}
```

---

## ✅ WHAT TO LOOK FOR WHEN READING TESTS

### In UNIT tests (UserServiceTest.java):

- `@Mock` = Fake object (not real)
- `when().thenReturn()` = "When this happens, return this"
- `verify()` = "Check if this method was called"
- `assertEquals()` = "These should be equal"
- `assertTrue()` / `assertFalse()` = "This should be true/false"

### In INTEGRATION tests (AuthControllerIntegrationTest.java):

- `mockMvc.perform()` = Simulate HTTP request (like browser)
- `get("/login")` = Visit this URL
- `formLogin()` = Submit login form
- `.andExpect()` = Check the response
- `authenticated()` = User is logged in
- `status().isOk()` = Got HTTP 200 (success)

---

## 🎨 VISUAL: What Each Test Actually Does

### UserServiceTest (Unit Test)

```
Test 1: Find existing user
┌─────────────────────┐
│  Call:              │
│  getUserByUsername  │ --> Fake Repository returns user --> ✅ Found!
│  ("student123")     │
└─────────────────────┘

Test 2: Find non-existent user
┌─────────────────────┐
│  Call:              │
│  getUserByUsername  │ --> Fake Repository returns nothing --> ✅ Not found!
│  ("nobody")         │
└─────────────────────┘
```

### AuthControllerIntegrationTest (Integration Test)

```
Test 1: View login page
Browser → GET /login → Controller → ✅ Shows login.html

Test 2: Login with correct password
Browser → POST /login (student123, password123)
        → Controller → Service → Database
        → ✅ User authenticated → Redirect to /dashboard

Test 3: Login with wrong password
Browser → POST /login (student123, wrongpass)
        → Controller → Service → Database
        → ❌ Authentication failed → Redirect to /login?error
```

---

## 🔍 HOW TO KNOW IF TESTS PASSED

### Green ✅ = All Good!

```
Tests run: 8, Failures: 0, Errors: 0, Skipped: 0
✓ All tests passed!
```

### Red ❌ = Something Failed

```
Tests run: 8, Failures: 1, Errors: 0, Skipped: 0
✗ Test failed: shouldLoginWithCorrectCredentials
  Expected: authenticated
  But was: unauthenticated
```

This means test #1 expected user to login but they didn't.

---

## 🎓 LEARNING PATH

### Step 1: Read the Comments

Both test files have LOTS of comments explaining every line

### Step 2: Run the Tests

```bash
docker-compose run app ./mvnw test
```

### Step 3: Make a Test Fail (On Purpose!)

1. Open `UserServiceTest.java`
2. Find this line:
   ```java
   assertTrue(result.isPresent(), "User should be found");
   ```
3. Change `assertTrue` to `assertFalse`
4. Run tests - it should FAIL!
5. Change it back to `assertTrue` - should PASS again!

This teaches you how tests catch bugs! 🐛

### Step 4: Read More

- [TESTING_GUIDE.md](TESTING_GUIDE.md) - Detailed testing concepts
- [BRANCH_PROTECTION_GUIDE.md](BRANCH_PROTECTION_GUIDE.md) - How to protect your code

---

## 🆘 COMMON QUESTIONS

**Q: Why are tests important?**  
A: They catch bugs BEFORE users find them. Professional developers always write tests.

**Q: Which test is more important?**  
A: Both! Unit tests are fast for quick feedback. Integration tests ensure everything works together.

**Q: Do I need to write tests for everything?**  
A: Start with critical features (login, user creation, etc.). Add more tests as you learn.

**Q: What if tests fail in CI/CD?**  
A: Fix the code until tests pass. Never merge failing tests to `main` branch!

**Q: How do I add more tests?**  
A: Copy an existing test, rename it, change what it tests. Follow the ARRANGE-ACT-ASSERT pattern.

---

## 📚 FILES TO READ IN ORDER

1. **This file** - You are here! ✅
2. **UserServiceTest.java** - Simple unit test with lots of comments
3. **AuthControllerIntegrationTest.java** - Simple integration test with comments
4. **TESTING_GUIDE.md** - More detailed explanations

---

## 🎯 QUICK TIPS

✅ **DO:**

- Read the comments in test files
- Run tests often
- Add tests for new features
- Make sure tests pass before committing

❌ **DON'T:**

- Skip reading comments (they explain everything!)
- Ignore failing tests
- Delete tests because they fail (fix the code or test instead)
- Commit code that breaks tests

---

**Need help? Check the comments in the test files - every line is explained!** 💡

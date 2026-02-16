# 🤖 How GitHub Actions (Cloud Testing) Works

## 📚 What is GitHub Actions?

GitHub Actions is like having a **robot** that automatically tests your code every time you push to GitHub. You don't need to run tests manually - GitHub does it for you in the cloud!

---

## 🎯 The Big Picture

```
You Write Code → Push to GitHub → Robot Runs Tests → You Get Results
     (Local)         (Upload)        (Cloud/GitHub)      (See ✅ or ❌)
```

---

## 📂 Where is the Logic? 

### The Configuration File:
```
.github/workflows/ci.yml
```

This file tells GitHub **WHAT** to do, **WHEN** to do it, and **HOW** to do it.

---

## 🔍 How It Works: Step-by-Step

### 1️⃣ TRIGGER: When Does It Run?

```yaml
on:
  push:
    branches: [main, develop]  # When you push code to main or develop branch
  pull_request:
    branches: [main, develop]  # When someone creates a pull request
```

**In Simple Words:**
- You run: `git push` 
- GitHub detects: "Hey, new code arrived!"
- GitHub starts: The robot wakes up and starts testing

---

### 2️⃣ ENVIRONMENT: Where Does It Run?

```yaml
runs-on: ubuntu-latest
```

**What This Means:**
- GitHub creates a **brand new virtual computer** (Ubuntu Linux)
- This computer exists in GitHub's cloud servers
- It's like renting a computer for 5-10 minutes
- After tests finish, the computer is deleted

**Think of it like:**
- You go to a library computer lab
- You use a computer for your work
- You leave, and they reset it for the next person

---

### 3️⃣ STEPS: What Does the Robot Do?

#### Step 1: Get Your Code
```yaml
- name: Checkout code
  uses: actions/checkout@v4
```
**What happens:** Downloads your code from GitHub to the virtual computer

---

#### Step 2: Install Java
```yaml
- name: Set up JDK 21
  uses: actions/setup-java@v4
  with:
    java-version: "21"
```
**What happens:** Installs Java 21 on the virtual computer (just like you installed Java on your PC)

---

#### Step 3: Build Your App
```yaml
- name: Build with Maven
  run: mvn clean compile -DskipTests
```
**What happens:** Compiles your Java code (turns .java files into .class files)

**Equivalent to running locally:**
```powershell
./mvnw.cmd clean compile -DskipTests
```

---

#### Step 4: Run Unit Tests
```yaml
- name: Run Unit Tests
  run: mvn test -Dtest=*Test
```
**What happens:** Runs all files ending with "Test" (like UserServiceTest.java)

**Equivalent to running locally:**
```powershell
./mvnw.cmd test -Dtest=UserServiceTest
```

---

#### Step 5: Run Integration Tests
```yaml
- name: Run Integration Tests
  run: mvn test -Dtest=*IntegrationTest
```
**What happens:** Runs all files ending with "IntegrationTest" (like AuthControllerIntegrationTest.java)

**Equivalent to running locally:**
```powershell
./mvnw.cmd test -Dtest=AuthControllerIntegrationTest
```

---

#### Step 6: Package the App
```yaml
- name: Package Application
  run: mvn package -DskipTests
```
**What happens:** Creates a .jar file (your application as a single file)

---

#### Step 7 & 8: Save Test Results
```yaml
- name: Upload Test Results
  uses: actions/upload-artifact@v4
  with:
    name: test-results
    path: target/surefire-reports/
```
**What happens:** Saves test reports so you can download and view them

---

## 🎬 Real-World Example

### Scenario: You Fix a Bug

**Step 1: You work locally**
```powershell
# You're on your Windows PC
cd 'E:\3.2\SEPM Lab\SPRINGBOOT_PROJECTS\niloy'

# You edit a file: src/main/java/.../.../UserService.java
# You save the file

# You test it locally (optional)
./mvnw.cmd test
```

**Step 2: You push to GitHub**
```powershell
git add .
git commit -m "Fixed user login bug"
git push origin main
```

**Step 3: GitHub Actions Starts Automatically**
```
🤖 GitHub Robot:
[✓] Received your code
[✓] Created Ubuntu virtual computer
[✓] Installed Java 21
[✓] Downloaded your code
[✓] Compiled your code
[✓] Running unit tests...
    ✅ UserServiceTest - PASSED
[✓] Running integration tests...
    ✅ AuthControllerIntegrationTest - PASSED
[✓] All tests passed! ✅
```

**Step 4: You see the results**
- Go to: https://github.com/niloy2107028/soft_lab_sms/actions
- You see: ✅ Green checkmark = Success!
- Or: ❌ Red X = Tests failed

---

## 🌐 Where to See Results

### Method 1: GitHub Website
1. Go to your repository: https://github.com/niloy2107028/soft_lab_sms
2. Click the **"Actions"** tab at the top
3. You'll see a list of all test runs
4. Click on any run to see details

### Method 2: On Commits
- Each commit shows a ✅ or ❌ icon
- ✅ = Tests passed in the cloud
- ❌ = Tests failed in the cloud

---

## 💰 Cost

**FREE for public repositories!**
- GitHub gives you **2,000 minutes per month** for free
- Your tests take ~1-2 minutes per run
- That's ~1,000 test runs per month for free!

---

## 🔥 Why This is Powerful

### Without GitHub Actions:
```
❌ You push broken code
❌ Your team pulls it
❌ Everyone's code breaks
❌ You find out hours later
```

### With GitHub Actions:
```
✅ You push code
✅ Robot tests it in 2 minutes
✅ Robot says: "Tests failed! ❌"
✅ You fix it BEFORE your team pulls
✅ Everyone is happy 😊
```

---

## 🎓 Key Concepts Explained

### 1. Virtual Environment
- **What:** A temporary computer that GitHub creates
- **Where:** In GitHub's data centers (the cloud)
- **Duration:** Only exists while tests run (5-10 minutes)
- **Why:** So your tests don't depend on your local computer

### 2. Workflow File (ci.yml)
- **What:** Instructions for the robot
- **Format:** YAML (a simple configuration language)
- **Location:** `.github/workflows/ci.yml`
- **Purpose:** Tells GitHub what to do automatically

### 3. Steps
- **What:** Individual tasks the robot performs
- **Order:** Runs top to bottom (Step 1, then Step 2, then Step 3...)
- **Failure:** If any step fails, the whole workflow fails

### 4. Triggers (on: push)
- **What:** Events that start the workflow
- **Examples:** push, pull_request, schedule, manual
- **Your setup:** Runs on push to main/develop branches

---

## 🔄 The Complete Flow Diagram

```
┌─────────────────────────────────────────────────────────────┐
│  YOUR COMPUTER (Windows)                                    │
│  ┌──────────────────────────────────────────────────────┐  │
│  │ 1. You write code                                    │  │
│  │ 2. You run: git push                                 │  │
│  └──────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                          │
                          │ (Internet)
                          ▼
┌─────────────────────────────────────────────────────────────┐
│  GITHUB (Cloud)                                             │
│  ┌──────────────────────────────────────────────────────┐  │
│  │ 3. GitHub receives your code                         │  │
│  │ 4. Reads .github/workflows/ci.yml                    │  │
│  │ 5. "Aha! I should run tests!"                        │  │
│  └──────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                          │
                          ▼
┌─────────────────────────────────────────────────────────────┐
│  VIRTUAL COMPUTER (Ubuntu Linux in Cloud)                  │
│  ┌──────────────────────────────────────────────────────┐  │
│  │ 6. Install Java                                      │  │
│  │ 7. Download your code                                │  │
│  │ 8. Compile code                                      │  │
│  │ 9. Run unit tests ✅                                 │  │
│  │ 10. Run integration tests ✅                         │  │
│  │ 11. Package application                              │  │
│  └──────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                          │
                          ▼
┌─────────────────────────────────────────────────────────────┐
│  RESULTS (Visible on GitHub)                                │
│  ┌──────────────────────────────────────────────────────┐  │
│  │ ✅ All tests passed!                                 │  │
│  │ 📊 5 tests run, 0 failures                          │  │
│  │ ⏱️ Completed in 1 minute 47 seconds                 │  │
│  └──────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                          │
                          ▼
                  You see: ✅ Green checkmark
              (Your code is safe to merge!)
```

---

## 🆚 Local vs Cloud Testing

| Feature | Local Testing | Cloud Testing (GitHub Actions) |
|---------|---------------|-------------------------------|
| **Where** | Your PC | GitHub's servers |
| **When** | When YOU run commands | Automatically on push |
| **Dependencies** | Needs Java on your PC | GitHub installs everything |
| **Visibility** | Only you see results | Whole team sees results |
| **Cost** | Free (uses your PC) | Free (2000 min/month) |
| **Use Case** | While developing | Before merging code |

---

## 💡 Pro Tips

### Tip 1: Test Locally First
```powershell
# Always test on your PC before pushing
./mvnw.cmd test

# If tests pass locally, then push
git push
```

### Tip 2: Watch the Actions Tab
- After pushing, go to: https://github.com/niloy2107028/soft_lab_sms/actions
- Watch your tests run in real-time
- See exactly where it fails (if it does)

### Tip 3: Fix Failures Quickly
- If tests fail in GitHub Actions ❌
- Pull the logs to see the error
- Fix locally, push again

---

## 🎯 Summary

**GitHub Actions is:**
- 🤖 An automatic robot that tests your code
- ☁️ Runs in the cloud (not on your PC)
- 🚀 Triggered when you push code
- ✅ Shows results on GitHub website
- 🆓 Free for public repositories
- 🛡️ Protects your code from bugs

**The workflow file (ci.yml):**
- 📝 Contains instructions for the robot
- 📂 Located in `.github/workflows/ci.yml`
- 🔧 Defines WHEN, WHERE, and HOW to test

**It's like having a teammate who:**
- Never sleeps 😴
- Tests every change 🔍
- Reports back in minutes ⏱️
- Never gets tired 💪

---

## 🚀 Next Steps

1. ✅ Push some code to GitHub
2. ✅ Go to Actions tab to watch it run
3. ✅ See the green checkmark ✅
4. ✅ Celebrate! You're using professional CI/CD! 🎉

---

**You now understand how GitHub Actions works! 🎓**

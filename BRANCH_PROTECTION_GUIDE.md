# 🔒 Branch Protection Rules Setup Guide

## What Are Branch Protection Rules?

Branch protection rules prevent team members from pushing broken code to important branches (like `main`). They enforce best practices like:

- ✅ Requiring code reviews before merging
- ✅ Ensuring tests pass before merging
- ✅ Preventing direct pushes to main branch
- ✅ Requiring pull requests (PRs) for all changes

## 🎯 Why Use Branch Protection?

**Without protection:**
❌ Anyone can push broken code to `main`
❌ No code review = more bugs
❌ Tests might be skipped
❌ Production breaks happen easily

**With protection:**
✅ All changes go through pull requests
✅ Tests must pass (CI checks)
✅ Code gets reviewed
✅ Main branch stays stable

---

## 📋 Step-by-Step Setup Guide

### Step 1: Go to Your Repository Settings

1. Open your browser and go to:

   ```
   https://github.com/niloy2107028/soft_lab_sms
   ```

2. Click on **"Settings"** tab (at the top of the page)
   - If you don't see Settings, make sure you're logged in to GitHub

### Step 2: Navigate to Branch Protection

1. In the left sidebar, click on **"Branches"**
2. You'll see a section called **"Branch protection rules"**

3. Click **"Add branch protection rule"** button

### Step 3: Configure the Rule

#### A. Branch Name Pattern

- In the **"Branch name pattern"** field, type:
  ```
  main
  ```
  This will apply the rule to your `main` branch

#### B. Enable Required Settings

Check these boxes (recommended for learning):

**1. ✅ Require a pull request before merging**

- This means you can't push directly to `main`
- All changes must go through a pull request

Sub-options to enable:

- ✅ **"Require approvals"**: Set to `1` approval
  - Someone must review and approve your code
- ✅ **"Dismiss stale pull request approvals when new commits are pushed"**
  - If you change code after approval, it needs re-approval

**2. ✅ Require status checks to pass before merging**

- Tests must pass before merging

Click **"Add status check"** and search for:

- `build-and-test` (this is your CI workflow job name)

Also check:

- ✅ **"Require branches to be up to date before merging"**
  - Ensures your branch has latest changes from main

**3. ✅ Require conversation resolution before merging**

- All code review comments must be resolved

**4. ✅ Do not allow bypassing the above settings**

- Even repository admins must follow rules (good practice!)

#### C. (Optional) Additional Settings

These are optional but useful:

- ✅ **"Require linear history"**: Keeps git history clean
- ✅ **"Include administrators"**: Apply rules to everyone

### Step 4: Save the Rule

1. Scroll to the bottom
2. Click **"Create"** button

🎉 **Done! Your main branch is now protected!**

---

## 🔄 How to Work with Branch Protection

### The New Workflow

**Old Way (Before Protection):**

```bash
git add .
git commit -m "changes"
git push origin main  # ❌ This will now be BLOCKED!
```

**New Way (With Protection):**

1. **Create a new branch:**

   ```bash
   git checkout -b feature/my-new-feature
   ```

2. **Make your changes and commit:**

   ```bash
   git add .
   git commit -m "Add new feature"
   ```

3. **Push to your branch:**

   ```bash
   git push origin feature/my-new-feature
   ```

4. **Create a Pull Request on GitHub:**
   - Go to: https://github.com/niloy2107028/soft_lab_sms
   - You'll see a banner: **"Compare & pull request"**
   - Click it and fill in:
     - Title: Brief description
     - Description: What changes you made
   - Click **"Create pull request"**

5. **Wait for CI to run:**
   - GitHub Actions will automatically run your tests
   - You'll see: ✅ "All checks have passed" or ❌ "Some checks failed"

6. **Get code review:**
   - Ask someone to review your code
   - They'll approve or request changes

7. **Merge the PR:**
   - Once approved and tests pass
   - Click **"Merge pull request"**
   - Click **"Confirm merge"**

---

## 🧪 Testing Your Branch Protection

Let's verify it works:

### Test 1: Try Direct Push (Should Fail)

```bash
cd "E:\3.2\SEPM Lab\SPRINGBOOT_PROJECTS\niloy"

# Make a small change
echo "Test" >> README.md

git add README.md
git commit -m "Test direct push"
git push origin main
```

**Expected Result:** ❌ Push rejected with error:

```
! [remote rejected] main -> main (protected branch hook declined)
```

### Test 2: Use Pull Request (Should Work)

```bash
# Create new branch
git checkout -b test-branch-protection

# Make a small change
echo "# Testing branch protection" >> README.md

git add README.md
git commit -m "Test: verify branch protection works"
git push origin test-branch-protection
```

Then:

1. Go to GitHub
2. Create a Pull Request
3. Watch CI run
4. Merge after approval ✅

---

## 📊 Monitoring CI Status

### Where to See CI Results

**On GitHub:**

1. Go to repository: https://github.com/niloy2107028/soft_lab_sms
2. Click **"Actions"** tab
3. You'll see all workflow runs

**What You'll See:**

- ✅ Green check = Tests passed
- ❌ Red X = Tests failed
- 🟡 Yellow dot = Tests running

### Understanding CI Workflow

Your `.github/workflows/ci.yml` does:

1. **Checkout code** - Downloads your code
2. **Setup Java 21** - Installs Java environment
3. **Build** - Compiles your code
4. **Run Unit Tests** - Tests individual components
5. **Run Integration Tests** - Tests components together
6. **Package** - Creates JAR file
7. **Upload Results** - Saves test reports

### Viewing Test Results

After CI runs:

1. Click on the workflow run
2. Click **"build-and-test"** job
3. Expand each step to see details
4. Download **"test-results"** artifact for detailed reports

---

## 🚨 Common Issues and Solutions

### Issue 1: "Branch protection rule prevents push"

**Solution:** This is correct! Use pull requests instead.

### Issue 2: CI tests failing

**Solution:**

```bash
# Run tests locally first
mvn test

# Fix any failing tests
# Then commit and push
```

### Issue 3: "No status checks found"

**Solution:**

- Make sure CI workflow ran at least once
- Check workflow file name matches
- Wait a few minutes after first push

### Issue 4: Can't merge PR

**Checklist:**

- ✅ All CI checks passed?
- ✅ Got required approval?
- ✅ All comments resolved?
- ✅ Branch up to date with main?

---

## 📚 Additional Commands

### Create and Switch to New Branch

```bash
git checkout -b feature/add-new-feature
```

### Update Your Branch with Latest Main

```bash
git checkout main
git pull origin main
git checkout your-branch
git merge main
```

### Delete Branch After Merging

```bash
git branch -d feature/old-feature
git push origin --delete feature/old-feature
```

---

## 🎓 Learning Resources

- [GitHub Branch Protection Docs](https://docs.github.com/en/repositories/configuring-branches-and-merges-in-your-repository/managing-protected-branches/about-protected-branches)
- [GitHub Actions Docs](https://docs.github.com/en/actions)
- [Pull Request Tutorial](https://docs.github.com/en/pull-requests)

---

## ✅ Quick Reference

### Branch Protection Checklist

- [ ] Branch protection rule created for `main`
- [ ] Require pull requests enabled
- [ ] Require status checks enabled
- [ ] CI workflow configured
- [ ] Tested with a pull request
- [ ] Team members understand new workflow

### Workflow Reminder

```
1. Create branch → 2. Make changes → 3. Commit →
4. Push → 5. Create PR → 6. CI runs →
7. Get review → 8. Merge
```

---

**💡 Pro Tip:** Start with lenient rules and make them stricter as your team grows comfortable with the process!

**Need Help?** Check the GitHub repository Issues tab or ask your instructor.

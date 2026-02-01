# 🚀 HOW TO RUN YOUR PROJECT - Complete Guide

## ✅ **PROJECT STATUS**

Your project **compiled and ran successfully!** ✨

```
✅ Tests Run: 2
✅ Tests Passed: 0
✅ Tests Failed: 0
✅ Tests Pending: 2 (because scenarios are not written yet)
✅ BUILD: SUCCESS
✅ Time: 19.603 seconds
```

---

## 🎯 **RUNNING TESTS - Different Options**

### **Option 1: Run ALL Tests** (Most Common)
```bash
cd d:\calss\Itfac_-batch21_62
mvn clean verify
```
**What happens:**
- Cleans old files
- Compiles code
- Runs all test scenarios
- Generates beautiful HTML reports

**Expected output:**
```
BUILD SUCCESS
Tests run: X, Failures: 0, Errors: 0, Skipped: 0
SERENITY REPORTS - Full Report: file:///D:/calss/Itfac_-batch21_62/target/site/serenity/index.html
```

---

### **Option 2: Run Only API Tests**
```bash
mvn verify -Drunner=RunApiTests
```
**Runs:** Only tests tagged with `@api`

---

### **Option 3: Run Only UI Tests**
```bash
mvn verify -Drunner=RunUiTests
```
**Runs:** Only tests tagged with `@ui`

---

### **Option 4: Compile Only (No Tests)**
```bash
mvn test-compile
```
**Runs:** Just compiles Java code, doesn't execute tests

---

### **Option 5: Skip Tests During Build**
```bash
mvn clean package -DskipTests
```
**Runs:** Build without executing tests

---

### **Option 6: Run with Specific Tag**
```bash
mvn verify -Dcucumber.filter.tags="@smoke"
```
**Runs:** Only scenarios with @smoke tag

---

## 📊 **UNDERSTANDING TEST OUTPUT**

When you run `mvn clean verify`, you'll see:

### **Phase 1: Compilation**
```
[INFO] Compiling 9 source files with javac
[INFO] Changes detected - recompiling the module!
```
✅ Java code is being compiled

### **Phase 2: Test Execution**
```
[INFO] Running com.qatraining.runners.TestRunner
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
```
✅ Tests are running

### **Phase 3: Report Generation**
```
[INFO] GENERATING REPORTS FOR: D:\calss\Itfac_-batch21_62
[INFO] Test results for 2 tests generated in 6.7 secs
```
✅ Reports are being created

### **Phase 4: Build Result**
```
[INFO] BUILD SUCCESS
[INFO] Total time: 19.603 s
```
✅ Everything completed successfully

---

## 📈 **TEST RESULTS SUMMARY**

The output shows a summary table:

```
SERENITY TESTS: PENDING
─────────────────────────────────────
Test scenarios executed        | 2
Total Test cases executed      | 2
Tests passed                   | 0
Tests failed                   | 0
Tests with errors              | 0
Tests pending                  | 2
Total Duration                 | 015ms
─────────────────────────────────────
SERENITY REPORTS
- Full Report: target/site/serenity/index.html
```

**Why 0 passed and 2 pending?**
- Because your feature files only have example scenarios (not yet implemented)
- Once you write real test steps, they will PASS or FAIL

---

## 📁 **WHERE ARE THE REPORTS?**

After running tests, reports are generated at:

```
target/site/serenity/
├── index.html              ← MAIN REPORT (Open this!)
├── serenity-summary.html
├── requirements.html
├── test-outcomes.html
├── error-summary.html
└── (many other JSON/CSS files)
```

### **How to Open the Report:**

**Option A: Command Line**
```bash
# Windows
start d:\calss\Itfac_-batch21_62\target\site\serenity\index.html

# Mac/Linux
open /path/to/target/site/serenity/index.html
```

**Option B: VS Code Explorer**
1. Open folder: `target/site/serenity/`
2. Right-click `index.html`
3. Select "Open with Live Server" or browser

**Option C: File Manager**
1. Navigate to: `D:\calss\Itfac_-batch21_62\target\site\serenity\`
2. Double-click `index.html`

---

## 🔍 **WHAT THE REPORT SHOWS**

The HTML report displays:
- ✅ Test scenario names
- ✅ Test status (PASS/FAIL/PENDING)
- ✅ Execution time
- ✅ Step-by-step details
- ✅ Screenshots (for UI tests)
- ✅ API response bodies
- ✅ Error messages

---

## 🎯 **COMPLETE WORKFLOW: From Code to Report**

```
1. Write Feature File (plant_management.feature)
        ↓
2. Write Step Definitions (Java code)
        ↓
3. Run: mvn clean verify
        ↓
4. Tests Execute
        ↓
5. Reports Generated: target/site/serenity/index.html
        ↓
6. Open Report in Browser
        ↓
7. Review Results & Fix Failures
        ↓
8. Repeat from step 1
```

---

## 💡 **QUICK COMMANDS REFERENCE**

| What You Want | Command |
|---------------|---------|
| Run all tests | `mvn clean verify` |
| Run API tests | `mvn verify -Drunner=RunApiTests` |
| Run UI tests | `mvn verify -Drunner=RunUiTests` |
| Compile only | `mvn test-compile` |
| See report | Open `target/site/serenity/index.html` |
| Run specific tag | `mvn verify -Dcucumber.filter.tags="@smoke"` |
| Quick build | `mvn clean package -DskipTests` |

---

## 🐛 **TROUBLESHOOTING**

### **Problem: Build failed with errors**
```
[ERROR] ... cannot find symbol
```
**Solution:** Check your Java syntax in step definitions

### **Problem: Tests not found**
```
WARNING: Deprecated ConfigurationException
```
**Solution:** Make sure feature files exist in correct location: `src/test/resources/features/`

### **Problem: Report not generated**
**Solution:** Check if tests actually ran. Ensure you have:
- Feature files with scenarios
- Step definitions with @Given/@When/@Then annotations
- No pending/undefined steps

### **Problem: Tests run but all PENDING**
**Solution:** Your scenarios don't have implementations. Add step definitions in `PlantManagementApiStepDefinitions.java` or `PlantManagementUiStepDefinitions.java`

---

## 📝 **NEXT STEPS**

1. **Write your first test scenario** in feature file
2. **Implement step definitions** in Java
3. **Run:** `mvn clean verify`
4. **View:** Open `target/site/serenity/index.html` in browser
5. **Verify:** Ensure test PASSES or see detailed FAILURE info

---

## 🎉 **YOUR PROJECT IS READY!**

Everything is set up correctly. Now:

1. Open [src/test/resources/features/api/plant_management.feature](../../src/test/resources/features/api/plant_management.feature)
2. Write your first API test scenario
3. Run: `mvn clean verify`
4. Check report to see results

---

**Happy Testing!** 🚀

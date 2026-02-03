# 🚀 QUICK START: Run UI-PM-1 Test Case

## One-Command Setup & Execution

```bash
# Navigate to project
cd d:\calss\Itfac_-batch21_62

# Run the UI test
mvn clean verify -Drunner=RunUiTests
```

---

## ✅ Pre-Flight Checklist

Before running, verify:

- [ ] **Backend Server Running** → `http://localhost:8080` (must be accessible)
- [ ] **Admin User Exists** → Username: `admin`, Password: `admin123`
- [ ] **Database Has Plants** → At least 1 plant record exists
- [ ] **Chrome Browser** → Installed and updated
- [ ] **Maven** → Installed and in PATH

---

## 📋 What the Test Does

1. Opens Login page
2. Logs in as admin (admin/admin123)
3. Navigates to Plants page
4. Verifies URL is `/ui/plants`
5. Checks "Plants" header visible
6. Confirms plant table visible
7. Verifies plants display in table

---

## 📊 Expected Results

```
✅ Tests run: 1
✅ Tests passed: 1
✅ Tests failed: 0
✅ Tests with errors: 0
✅ BUILD SUCCESS
```

---

## 📁 Files Created/Modified

```
✅ src/test/resources/features/ui/plant_management.feature
   └─ Added: UI-PM-1 scenario with 8 steps

✅ src/test/java/com/qatraining/stepdefinitions/ui/PlantManagementUiStepDefinitions.java
   └─ Added: 8 step implementation methods

✅ src/test/java/com/qatraining/pages/PlantsPage.java
   └─ Added: 7 new helper methods
   └─ Updated: DefaultUrl to "/ui/plants"
```

---

## 🎯 Step Details

| # | Step | Implementation |
|---|------|-----------------|
| 1 | Navigate to login page | `adminNavigatesToLoginPage()` |
| 2 | Enter credentials | `adminEntersCredentials("admin", "admin123")` |
| 3 | Click login | `adminClicksLoginButton()` |
| 4 | Wait for page load | `adminWaitsForPlantsPageToLoad()` |
| 5 | Verify URL | `adminShouldBeOnPlantsPage("/ui/plants")` |
| 6 | Check header | `headerShouldBeVisible("Plants")` |
| 7 | Check table | `plantTableShouldBeVisible()` |
| 8 | Check plants | `plantsShoulBDisplayedInTable()` |

---

## 🔍 View Test Reports

After running `mvn clean verify`:

```
Open: D:\calss\Itfac_-batch21_62\target\site\serenity\index.html
```

---

## 🛠️ Troubleshooting

| Error | Solution |
|-------|----------|
| `Connection refused` | Start backend server on port 8080 |
| `Login fails` | Verify admin credentials exist in database |
| `No plants shown` | Insert plant records in database |
| `Timeout` | Increase timeout in `test-config.properties` |

---

## 🎓 Run Other Tests

```bash
# Run ALL tests
mvn clean verify

# Run only UI tests
mvn verify -Drunner=RunUiTests

# Run only API tests
mvn verify -Drunner=RunApiTests

# Run with specific tag
mvn verify -Dcucumber.filter.tags="@ui and @plant-management"
```

---

**Test Case:** UI-PM-1 - Admin Access to Plant Management Dashboard  
**Status:** ✅ Ready to Execute  
**Created:** 2026-02-02


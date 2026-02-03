# UI Test Setup Guide

## Login URL Configuration

✅ **Login URL is correctly configured:**
- **Full URL:** `http://localhost:8080/ui/login`
- **Base URL:** `http://localhost:8080` (from serenity.conf)
- **Login Path:** `/ui/login` (from LoginPage.java @DefaultUrl annotation)

## Prerequisites for UI Tests to Pass

### 1. **Start the Application Server**
The UI tests require the web application to be running on `http://localhost:8080`. You must start your application server before running UI tests.

```bash
# Example: If using Java/Spring Boot
java -jar application.jar

# Or if using Node.js/Express
npm start

# Or use your specific application startup command
```

### 2. **Verify Application is Running**
- Open your browser and navigate to: `http://localhost:8080/ui/login`
- You should see the login page displayed
- Username field should have `id="username"`
- Password field should have `id="password"`
- Login button should have `id="login-button"`

### 3. **Admin Credentials**
The UI tests use the following credentials (configured in test-config.properties):
```
Username: admin
Password: admin123
```

## Running UI Tests

### Run Only UI Tests:
```bash
mvn clean verify -Drunner=RunUiTests
```

### Run with More Details:
```bash
mvn clean verify -Drunner=RunUiTests -X
```

### View Test Report:
After tests complete, open the report:
```
target/site/serenity/index.html
```

## Current Test Status

**Test:** `UI-PM-1 - Admin access to Plant Management Dashboard`

**Status:** ❌ **FAILING** - Login page not found

**Reason:** Application server is not running or not accessible at `http://localhost:8080/ui/login`

## Troubleshooting

### Issue: "Login page should be displayed"

**Possible Causes:**
1. ❌ Application server is not running
2. ❌ Server is running on a different port
3. ❌ Network connectivity issue
4. ❌ Firewall blocking localhost access

**Solution Steps:**

**Step 1:** Check if application is running
```bash
curl http://localhost:8080/ui/login
# or open in browser
```

**Step 2:** Start the application server
- Check documentation for how to start your specific application
- Ensure it's listening on port 8080
- Wait for full startup (may take 10-30 seconds)

**Step 3:** Retry the UI tests
```bash
mvn clean verify -Drunner=RunUiTests
```

**Step 4:** Check logs
- Look in `logs/` directory for application logs
- Check browser console (F12) for JavaScript errors
- Check test logs for detailed error messages

### Issue: Chrome/ChromeDriver compatibility warning

The tests show a warning about CDP (Chrome DevTools Protocol) version mismatch. This usually doesn't cause failures but indicates:
- Your Chrome browser version: 144
- Selenium DevTools version may need updating

This is typically safe to ignore, but if UI tests continue to fail, consider:
- Updating Selenium WebDriver
- Updating ChromeDriver
- Using headless mode: Update serenity.conf with `headless.mode = true`

## Test Files Modified

**Modified Files:**
- `src/test/java/com/qatraining/stepdefinitions/ui/PlantManagementUiStepDefinitions.java`
  - Added detailed logging to diagnose URL navigation issues
  - Shows actual URL being accessed and current page URL

**Configuration Files:**
- `serenity.conf` - Contains `webdriver.base.url = "http://localhost:8080"`
- `test-config.properties` - Contains `base.url=http://localhost:8080`

## Next Steps

1. **Start your application server** on port 8080
2. **Verify login page loads** at `http://localhost:8080/ui/login`
3. **Run the UI tests** again
4. **Review test report** for detailed results

---

**Note:** The application server setup is **outside the scope of test automation**. Please refer to your application's documentation for startup instructions.

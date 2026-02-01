# Quick Reference Card

## 📂 Files You Need to Edit

```
WHEN WRITING:                       EDIT THIS FILE:
─────────────────────────────────────────────────────────────
API Test Scenarios (Gherkin)    → src/test/resources/features/api/plant_management.feature
API Test Code (Java)            → src/test/java/com/qatraining/stepdefinitions/api/PlantManagementApiStepDefinitions.java

UI Test Scenarios (Gherkin)     → src/test/resources/features/ui/plant_management.feature
UI Test Code (Java)             → src/test/java/com/qatraining/stepdefinitions/ui/PlantManagementUiStepDefinitions.java
UI Page Objects (Java)          → src/test/java/com/qatraining/pages/LoginPage.java
                               → src/test/java/com/qatraining/pages/PlantsPage.java
```

## 🔄 The 3-Step Cycle

```
1. Write Feature File (.feature)
   ↓
2. Run: mvn clean verify
   ↓
3. Implement Step Definitions (.java)
   ↓
   Repeat for each test case
```

## 🎯 Quick Commands

```bash
# Compile project
mvn clean compile -DskipTests

# Compile tests  
mvn test-compile

# Run all tests
mvn clean verify

# Run API tests only
mvn verify -Drunner=api

# Run UI tests only
mvn verify -Drunner=ui

# View test report
open target/site/serenity/index.html
# or on Windows:
start target\site\serenity\index.html
```

## 📝 Code Templates

### API Step (Template)
```java
@Given("the API is configured")
public void setupApi() {
    baseUrl = "http://localhost:8080/api";
}

@When("I send a POST request to {string}")
public void sendPost(String endpoint, DataTable data) {
    Map<String, Object> body = data.asMap();
    response = SerenityRest.given()
        .baseUri(baseUrl)
        .contentType("application/json")
        .body(body)
        .when()
        .post(endpoint);
}

@Then("the response status is {int}")
public void verifyStatus(int code) {
    assertThat(response.getStatusCode()).isEqualTo(code);
}
```

### UI Step (Template)
```java
@Given("user is on login page")
public void navigateToLogin() {
    loginPage.open();
}

@When("user enters {string}")
public void enterUsername(String username) {
    loginPage.enterUsername(username);
}

@Then("dashboard is displayed")
public void verifyDashboard() {
    assertThat(plantsPage.isDisplayed()).isTrue();
}
```

### Page Object (Template)
```java
public class LoginPage extends BasePage {
    private By usernameField = By.id("username");
    private By passwordField = By.id("password");
    
    public void enterUsername(String username) {
        element(usernameField).sendKeys(username);
    }
    
    public void enterPassword(String password) {
        element(passwordField).sendKeys(password);
    }
}
```

### Feature File (Template)
```gherkin
@api @smoke
Scenario: Test name here
    Given precondition step
    When action step with "data"
    Then assertion step
    And additional assertion
```

## 🎓 Gherkin Keywords

| Keyword | Purpose | Example |
|---------|---------|---------|
| `Feature:` | Describe what you're testing | `Feature: Plant Management` |
| `Scenario:` | One test case | `Scenario: Create a plant` |
| `@tag` | Label for filtering | `@api @smoke` |
| `Given` | Setup/precondition | `Given the API is running` |
| `When` | Action/trigger | `When I create a plant` |
| `Then` | Expected result | `Then the plant is created` |
| `And` | Additional step | `And the ID is returned` |
| `Scenario Outline:` | Multiple test data | Use with `Examples:` |

## 📊 Assertions Cheat Sheet

```java
// String assertions
assertThat(name).isNotNull();
assertThat(name).isEqualTo("Rose");
assertThat(name).contains("Rose");

// Number assertions
assertThat(price).isEqualTo(500);
assertThat(price).isGreaterThan(100);
assertThat(quantity).isBetween(1, 1000);

// Collection assertions
assertThat(plantList).isNotEmpty();
assertThat(plantList).contains("Rose");
assertThat(plantList).hasSize(5);

// API Response assertions
assertThat(response.getStatusCode()).isEqualTo(201);
assertThat(response.jsonPath().getString("id")).isNotNull();
```

## 🔧 Useful Methods

```java
// Record data in test report
Serenity.recordReportData()
    .withTitle("Plant Created")
    .andContents("ID: 123");

// Store data between steps
Serenity.setSessionVariable("plantId").to(plantId);
String savedId = Serenity.sessionVariableCalled("plantId");

// RestAssured request
Response response = SerenityRest.given()
    .baseUri(baseUrl)
    .contentType("application/json")
    .header("Authorization", "Bearer " + token)
    .body(requestBody)
    .when()
    .post(endpoint);

// Parse JSON response
String id = response.jsonPath().getString("id");
int price = response.jsonPath().getInt("price");
List<?> plants = response.jsonPath().getList("$");

// Selenium element actions
element(locator).click();
element(locator).sendKeys("text");
element(locator).clear();
boolean isDisplayed = element(locator).isDisplayed();
```

## 🚨 Common Mistakes to Avoid

❌ **Wrong:** Hardcoding URLs in step definitions
✅ **Right:** Use configuration/properties files

❌ **Wrong:** Using Thread.sleep() for waits  
✅ **Right:** Use explicit waits

❌ **Wrong:** One big test scenario
✅ **Right:** Small, focused scenarios

❌ **Wrong:** Copying test code  
✅ **Right:** Reuse steps and page objects

❌ **Wrong:** No assertions
✅ **Right:** Assert every result

## 📞 Troubleshooting

**Problem:** "Step not implemented"
**Solution:** Add @Given/@When/@Then method matching the text

**Problem:** "Element not found"
**Solution:** Verify selector is correct, element is loaded

**Problem:** "Test passes but shouldn't"  
**Solution:** Check assertions are actually running

**Problem:** "Build fails"
**Solution:** Run `mvn clean compile` to see exact error

---

**Start with:** [README_SETUP.md](README_SETUP.md) for detailed guide
**Or Quick Start:** Create feature file → Implement steps → Run tests

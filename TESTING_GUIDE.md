# Step-by-Step Guide: Building Your Test Suite

## 📁 Project Structure - Files You'll Update

```
src/test/
├── java/com/qatraining/
│   ├── pages/              (Page Object Models - UI elements)
│   │   ├── BasePage.java
│   │   ├── LoginPage.java
│   │   └── PlantsPage.java
│   ├── stepdefinitions/    (Test code - matches feature file steps)
│   │   ├── api/
│   │   │   └── PlantManagementApiStepDefinitions.java  ← UPDATE WITH API TESTS
│   │   ├── ui/
│   │   │   └── PlantManagementUiStepDefinitions.java   ← UPDATE WITH UI TESTS
│   │   ├── Hooks.java      (Setup/Teardown logic)
│   │   └── runners/
│   │       ├── RunApiTests.java
│   │       ├── RunUiTests.java
│   │       └── TestRunner.java
│   └── runners/
└── resources/
    └── features/           (Test scenarios in Gherkin language)
        ├── api/
        │   └── plant_management.feature     ← UPDATE WITH API SCENARIOS
        └── ui/
            └── plant_management.feature     ← UPDATE WITH UI SCENARIOS
```

## 🔄 Workflow - What To Do First

### Step 1: Write Feature Files (Describe What To Test)
**Files to update:**
- [src/test/resources/features/api/plant_management.feature](src/test/resources/features/api/plant_management.feature)
- [src/test/resources/features/ui/plant_management.feature](src/test/resources/features/ui/plant_management.feature)

**What to write:** Test scenarios in Gherkin language (human-readable format)

**Example API feature file:**
```gherkin
@api @plant-management
Feature: Plant Management API

  Scenario: Create a new plant
    Given the API base URL is configured
    When I send a POST request to "/api/plants" with:
      | name     | Rose |
      | price    | 500  |
    Then the response status code should be 201
    And the response contains plant ID
```

### Step 2: Create Page Object Models (For UI Tests Only)
**Files to update:**
- [src/test/java/com/qatraining/pages/LoginPage.java](src/test/java/com/qatraining/pages/LoginPage.java)
- [src/test/java/com/qatraining/pages/PlantsPage.java](src/test/java/com/qatraining/pages/PlantsPage.java)

**What to write:** Methods that interact with web elements
- Element locators (By.id, By.xpath, etc.)
- User actions (click, type, submit)
- Assertions (verify elements)

**Example:**
```java
public class LoginPage extends BasePage {
    private By usernameField = By.id("username");
    private By passwordField = By.id("password");
    private By loginButton = By.id("login-btn");
    
    public void enterUsername(String username) {
        element(usernameField).sendKeys(username);
    }
    
    public void clickLoginButton() {
        element(loginButton).click();
    }
}
```

### Step 3: Write Step Definitions (Connect Features to Code)
**Files to update:**
- [src/test/java/com/qatraining/stepdefinitions/api/PlantManagementApiStepDefinitions.java](src/test/java/com/qatraining/stepdefinitions/api/PlantManagementApiStepDefinitions.java)
- [src/test/java/com/qatraining/stepdefinitions/ui/PlantManagementUiStepDefinitions.java](src/test/java/com/qatraining/stepdefinitions/ui/PlantManagementUiStepDefinitions.java)

**What to write:** Java methods that match your Gherkin steps

**For API Tests:**
```java
@Given("the API base URL is configured")
public void setupApiUrl() {
    baseUrl = "http://localhost:8080/api";
}

@When("I send a POST request to {string} with:")
public void sendPostRequest(String endpoint, DataTable data) {
    // Use RestAssured to make HTTP request
}

@Then("the response status code should be {int}")
public void verifyStatusCode(int expectedCode) {
    // Assert response code matches
}
```

**For UI Tests:**
```java
@Given("the admin user is logged into the application")
public void loginAdmin() {
    loginPage.open();
    loginPage.enterUsername("admin");
    loginPage.enterPassword("password");
    loginPage.clickLoginButton();
}

@When("the admin clicks the {string} button")
public void clickButton(String buttonName) {
    plantsPage.clickButton(buttonName);
}

@Then("the new plant {string} should appear")
public void verifyPlantExists(String plantName) {
    assertThat(plantsPage.isPlantInTable(plantName)).isTrue();
}
```

## 📝 Example Test Case - Complete Flow

### Example: Create a Plant (API Test)

**1. Feature File:**
```gherkin
@api @smoke
Scenario: Successfully create a new plant
    Given the API is running on "http://localhost:8080"
    And the user is authenticated with token
    When I submit a POST request to "/plants" with:
        | name     | Rose       |
        | price    | 500        |
        | quantity | 100        |
    Then the response status is 201
    And the response body contains:
        | id    | is not empty  |
        | name  | Rose          |
        | price | 500           |
```

**2. API Step Definition:**
```java
private Response response;
private String baseUrl;
private String authToken;

@Given("the API is running on {string}")
public void setupApiUrl(String url) {
    baseUrl = url;
}

@And("the user is authenticated with token")
public void authenticate() {
    // Get auth token
    authToken = "your-token-here";
}

@When("I submit a POST request to {string} with:")
public void postRequest(String endpoint, DataTable data) {
    Map<String, String> requestBody = data.asMap();
    
    response = SerenityRest.given()
        .baseUri(baseUrl)
        .contentType("application/json")
        .header("Authorization", "Bearer " + authToken)
        .body(requestBody)
        .when()
        .post(endpoint);
}

@Then("the response status is {int}")
public void verifyStatus(int expectedStatus) {
    assertThat(response.getStatusCode()).isEqualTo(expectedStatus);
}

@And("the response body contains:")
public void verifyResponseBody(DataTable data) {
    Map<String, String> expectedData = data.asMap();
    
    for (Map.Entry<String, String> entry : expectedData.entrySet()) {
        assertThat(response.jsonPath().getString(entry.getKey()))
            .contains(entry.getValue());
    }
}
```

## 🎯 Quick Checklist - What To Do

- [ ] **Write API Feature File** - Describe API test scenarios
- [ ] **Write API Step Definitions** - Implement API test code using RestAssured
- [ ] **Write UI Feature File** - Describe UI test scenarios
- [ ] **Update Page Objects** - Add LoginPage and PlantsPage methods
- [ ] **Write UI Step Definitions** - Implement UI test code using Selenium
- [ ] **Run Tests** - Execute and verify they pass

## 🚀 Running Your Tests

### Run All Tests:
```bash
mvn clean verify
```

### Run Only API Tests:
```bash
mvn clean verify -Drunner=RunApiTests
```

### Run Only UI Tests:
```bash
mvn clean verify -Drunner=RunUiTests
```

### View Test Results:
- Reports generated in: `target/site/serenity/`
- Open the HTML report in browser

## 🔧 Framework Details

**Framework:** Cucumber BDD + Serenity + RestAssured + Selenium
- **Cucumber:** Write tests in Gherkin (human language)
- **Serenity:** Test reporting & orchestration
- **RestAssured:** API testing
- **Selenium:** UI testing (WebDriver)

**Key Dependencies:**
- Maven (build tool)
- Java 21
- JUnit 5
- Cucumber 7.15
- Serenity 4.1.3

## 💡 Pro Tips

1. **One scenario = One test case** - Each Scenario in feature file = 1 test
2. **Data-driven tests** - Use DataTable for multiple test values
3. **Reusable steps** - Write generic steps, use parameters
4. **Wait for elements** - Use explicit waits in UI tests
5. **Mock external APIs** - Use mock servers for API tests
6. **Report data** - Use `Serenity.recordReportData()` for custom reporting

## 📚 Important Files Summary

| File | Purpose |
|------|---------|
| plant_management.feature | Define WHAT to test |
| PlantManagementApiStepDefinitions.java | Implement HOW to test APIs |
| PlantManagementUiStepDefinitions.java | Implement HOW to test UI |
| LoginPage.java | UI interactions for login |
| PlantsPage.java | UI interactions for plants |
| BasePage.java | Common UI methods (don't modify) |

---

**You are now ready to start writing your tests! Start with the feature files first.**

# ✅ SETUP COMPLETE - Your Test Project is Ready!

## 📊 What Was Done

### ✅ Cleaned All Code
- ✓ Removed all existing API step definitions
- ✓ Removed all existing UI step definitions  
- ✓ Cleared feature files (ready for your scenarios)
- ✓ Project compiles successfully with no errors

### 📁 Files You Will Update

```
Your Key Files:
├── 🔴 src/test/resources/features/api/plant_management.feature          ← WRITE API TEST SCENARIOS HERE
├── 🔴 src/test/resources/features/ui/plant_management.feature           ← WRITE UI TEST SCENARIOS HERE
├── 🟡 src/test/java/com/qatraining/stepdefinitions/api/PlantManagementApiStepDefinitions.java   ← IMPLEMENT API TESTS
├── 🟡 src/test/java/com/qatraining/stepdefinitions/ui/PlantManagementUiStepDefinitions.java    ← IMPLEMENT UI TESTS
├── 🟡 src/test/java/com/qatraining/pages/LoginPage.java                ← ADD UI ELEMENT LOCATORS & METHODS
└── 🟡 src/test/java/com/qatraining/pages/PlantsPage.java               ← ADD UI ELEMENT LOCATORS & METHODS

Color Legend:
🔴 = Critical (Must write tests here)
🟡 = Important (Write page objects and implementations)
🟢 = Done (Don't modify)
```

---

## 🎯 YOUR 3-STEP WORKFLOW

### STEP 1️⃣ : Write API Feature File
**File:** [src/test/resources/features/api/plant_management.feature](src/test/resources/features/api/plant_management.feature)

**What to write:** BDD test scenarios in Gherkin language

```gherkin
@api @smoke @create
Scenario: Create a new plant successfully
    Given the API base URL is configured
    When I send a POST request to "/api/plants" with:
        | name     | Rose |
        | price    | 500  |
        | quantity | 100  |
    Then the response status code should be 201
    And the response contains plant ID
```

### STEP 2️⃣ : Write API Step Definitions
**File:** [src/test/java/com/qatraining/stepdefinitions/api/PlantManagementApiStepDefinitions.java](src/test/java/com/qatraining/stepdefinitions/api/PlantManagementApiStepDefinitions.java)

**What to write:** Java code that matches your feature file steps

```java
@Given("the API base URL is configured")
public void setupApiUrl() {
    baseUrl = "http://localhost:8080/api";
}

@When("I send a POST request to {string} with:")
public void sendPostRequest(String endpoint, DataTable data) {
    // Use RestAssured library
    response = SerenityRest.given()
        .baseUri(baseUrl)
        .contentType("application/json")
        .body(data.asMap())
        .when()
        .post(endpoint);
}

@Then("the response status code should be {int}")
public void verifyStatusCode(int expectedCode) {
    assertThat(response.getStatusCode()).isEqualTo(expectedCode);
}

@And("the response contains plant ID")
public void verifyPlantId() {
    assertThat(response.jsonPath().getString("id")).isNotNull();
}
```

### STEP 3️⃣ : Write UI Tests (Optional - Follow Same Pattern)
**Files:** 
- [src/test/resources/features/ui/plant_management.feature](src/test/resources/features/ui/plant_management.feature)
- [src/test/java/com/qatraining/pages/LoginPage.java](src/test/java/com/qatraining/pages/LoginPage.java)
- [src/test/java/com/qatraining/pages/PlantsPage.java](src/test/java/com/qatraining/pages/PlantsPage.java)
- [src/test/java/com/qatraining/stepdefinitions/ui/PlantManagementUiStepDefinitions.java](src/test/java/com/qatraining/stepdefinitions/ui/PlantManagementUiStepDefinitions.java)

---

## 🚀 RUNNING YOUR TESTS

### Run All Tests:
```bash
cd d:\calss\Itfac_-batch21_62
mvn clean verify
```

### Run Only API Tests:
```bash
mvn verify -Drunner=api
```

### Run Only UI Tests:
```bash
mvn verify -Drunner=ui
```

### View Results:
After tests run, open test report:
```
target/site/serenity/index.html
```

---

## 📝 COMPLETE EXAMPLE - Create a Plant API Test

### Feature File Example:
```gherkin
@api @plant-management @smoke
Feature: Plant Management API

  Background:
    Given the API base URL is configured

  @create
  Scenario: Admin creates a new plant with valid details
    When I send a POST request to "/api/plants" with:
      | name     | Rose   |
      | price    | 500    |
      | quantity | 100    |
    Then the response status code should be 201
    And the response contains plant ID

  @update
  Scenario: Admin updates plant details
    Given a plant with ID "123" exists
    When I send a PUT request to "/api/plants/123" with:
      | price    | 600 |
      | quantity | 50  |
    Then the response status code should be 200

  @delete
  Scenario: Admin deletes a plant
    When I send a DELETE request to "/api/plants/123"
    Then the response status code should be 200
```

### Step Definition Example:
```java
package com.qatraining.stepdefinitions.api;

import io.cucumber.java.en.*;
import io.cucumber.datatable.DataTable;
import io.restassured.response.Response;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.rest.SerenityRest;
import static org.assertj.core.api.Assertions.assertThat;

public class PlantManagementApiStepDefinitions {
    
    private String baseUrl;
    private Response response;
    private Map<String, Object> requestPayload;
    
    @Given("the API base URL is configured")
    public void setupApiUrl() {
        baseUrl = "http://localhost:8080/api";
        Serenity.recordReportData()
            .withTitle("API Base URL")
            .andContents(baseUrl);
    }
    
    @When("I send a POST request to {string} with:")
    public void sendPostRequest(String endpoint, DataTable dataTable) {
        requestPayload = new HashMap<>();
        dataTable.asMap(String.class, String.class).forEach(requestPayload::put);
        
        response = SerenityRest.given()
            .baseUri(baseUrl)
            .contentType("application/json")
            .body(requestPayload)
            .when()
            .post(endpoint);
    }
    
    @Then("the response status code should be {int}")
    public void verifyStatusCode(int expectedCode) {
        assertThat(response.getStatusCode())
            .as("Expected status code: " + expectedCode)
            .isEqualTo(expectedCode);
    }
    
    @And("the response contains plant ID")
    public void verifyPlantId() {
        String plantId = response.jsonPath().getString("id");
        assertThat(plantId).isNotNull();
        Serenity.setSessionVariable("plantId").to(plantId);
    }
    
    @Given("a plant with ID {string} exists")
    public void setupPlant(String plantId) {
        Serenity.setSessionVariable("plantId").to(plantId);
    }
    
    @When("I send a PUT request to {string} with:")
    public void sendPutRequest(String endpoint, DataTable dataTable) {
        String plantId = Serenity.sessionVariableCalled("plantId");
        endpoint = endpoint.replace("{id}", plantId);
        
        requestPayload = new HashMap<>();
        dataTable.asMap(String.class, String.class).forEach(requestPayload::put);
        
        response = SerenityRest.given()
            .baseUri(baseUrl)
            .contentType("application/json")
            .body(requestPayload)
            .when()
            .put(endpoint);
    }
    
    @When("I send a DELETE request to {string}")
    public void sendDeleteRequest(String endpoint) {
        response = SerenityRest.given()
            .baseUri(baseUrl)
            .when()
            .delete(endpoint);
    }
}
```

---

## 🎓 LEARNING PATH

Follow this order to write your tests:

1. **Create Feature File** (.feature) - Write scenarios
2. **Run Tests** - Tests will fail (that's OK!)
3. **Implement Step Definitions** (.java) - Make tests pass
4. **Add Assertions** - Verify correct behavior
5. **Refactor & Reuse** - Use common steps in multiple tests

---

## 📚 Key Libraries You'll Use

### API Testing:
```java
// RestAssured - Make HTTP requests
import io.restassured.RestAssured;
import io.restassured.response.Response;
import net.serenitybdd.rest.SerenityRest;

// Example:
Response response = SerenityRest.given()
    .baseUri("http://localhost:8080")
    .contentType("application/json")
    .body(requestBody)
    .when()
    .post("/api/plants");
```

### UI Testing:
```java
// Selenium WebDriver - Control browser
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import net.serenitybdd.annotations.Managed;

// Example:
@Managed
WebDriver driver;

driver.findElement(By.id("username")).sendKeys("admin");
driver.findElement(By.id("password")).sendKeys("password123");
driver.findElement(By.id("login")).click();
```

### Assertions:
```java
// AssertJ - Verify test conditions
import static org.assertj.core.api.Assertions.assertThat;

assertThat(response.getStatusCode()).isEqualTo(201);
assertThat(plantName).isNotNull();
assertThat(plantsTable).contains("Rose");
```

### Data Tables:
```java
// Cucumber DataTable - Pass test data
@When("I enter the following data:")
public void enterData(DataTable dataTable) {
    Map<String, String> data = dataTable.asMap();
    String name = data.get("name");
    String price = data.get("price");
}
```

---

## ✨ HELPFUL TIPS

### 1. Use Meaningful Step Names
```gherkin
✅ Good:
    When I send a POST request to create a new plant
    Then the plant should be created successfully

❌ Avoid:
    When I do something
    Then the system works
```

### 2. Reuse Steps Across Scenarios
```gherkin
✅ Both scenarios use same step:
    Scenario: Create plant
        Given the API is configured
        
    Scenario: Delete plant
        Given the API is configured
```

### 3. Use Data Tables for Multiple Test Cases
```gherkin
✅ Good:
    Scenario Outline: Create different plants
        When I create a plant with:
            | name | price |
            | Rose | 500   |
            | Lily | 800   |

✅ Good:
    When I enter plant details:
        | name     | Rose |
        | price    | 500  |
        | quantity | 100  |
```

### 4. Document Your Tests
```java
/**
 * Creates a new plant via API
 * Expected: Plant created with ID returned
 */
@When("I send a POST request to create a plant")
public void createPlant(DataTable data) {
    // Implementation
}
```

### 5. Use Session Variables for Test Data
```java
// Store data from one step to use in another
Serenity.setSessionVariable("plantId").to(createdId);
String plantId = Serenity.sessionVariableCalled("plantId");
```

---

## 🐛 Common Issues & Solutions

| Problem | Solution |
|---------|----------|
| "Step not defined" error | Implement the @Given/@When/@Then method in step definitions |
| Feature file not found | Ensure .feature files are in `src/test/resources/features/` |
| Port already in use | Change port in application or kill existing process |
| Element not found | Check selector (By.id, By.xpath) in page objects |
| Test fails but shouldn't | Check baseUrl, auth tokens, and test data |
| Browser won't open | Install webdriver matching Chrome/Firefox version |

---

## 📞 You're Ready!

Your project is now clean and ready for testing! 

**Next Steps:**
1. ✅ Project setup complete
2. 📝 Write your first feature file (API or UI)
3. ▶️ Run `mvn clean verify` to execute tests
4. 📊 Check test results in `target/site/serenity/`
5. 🔄 Repeat for more test cases

---

## 📖 See Also

- [TESTING_GUIDE.md](TESTING_GUIDE.md) - Detailed explanation of project structure
- [Cucumber Documentation](https://cucumber.io/)
- [RestAssured Guide](https://rest-assured.io/)
- [Serenity BDD](https://serenity-bdd.github.io/)

---

**Happy Testing! 🚀**

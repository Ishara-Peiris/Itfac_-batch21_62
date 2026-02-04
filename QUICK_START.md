# 🎯 5-MINUTE QUICK START

## Your Project is Ready! ✅

```bash
cd d:\calss\Itfac_-batch21_62
mvn clean verify
```

**That's it!** This command will:
1. ✅ Compile all code
2. ✅ Run all tests
3. ✅ Generate beautiful reports
4. ✅ Show results in console

---

## 📊 What You'll See

```
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
[INFO] SERENITY REPORTS
[INFO] - Full Report: file:///D:/calss/Itfac_-batch21_62/target/site/serenity/index.html
```

---

## 🎓 NOW WRITE YOUR FIRST TEST

### 1️⃣ Open API Feature File
**File:** `src/test/resources/features/api/plant_management.feature`

### 2️⃣ Write a Scenario
```gherkin
@api @smoke @create
Scenario: Create a new plant
    Given the API base URL is configured
    When I send a POST request to "/api/plants" with:
        | name     | Rose |
        | price    | 500  |
    Then the response status code should be 201
```

### 3️⃣ Implement Steps
**File:** `src/test/java/com/qatraining/stepdefinitions/api/PlantManagementApiStepDefinitions.java`

```java
@Given("the API base URL is configured")
public void setupApi() {
    baseUrl = "http://localhost:8080/api";
}

@When("I send a POST request to {string} with:")
public void sendPost(String endpoint, DataTable data) {
    Map<String, Object> body = data.asMap();
    response = SerenityRest.given()
        .baseUri(baseUrl)
        .body(body)
        .when()
        .post(endpoint);
}

@Then("the response status code should be {int}")
public void verifyStatus(int code) {
    assertThat(response.getStatusCode()).isEqualTo(code);
}
```

### 4️⃣ Run Tests
```bash
mvn clean verify
```

### 5️⃣ View Results
Open: `target/site/serenity/index.html` in your browser

---

## ⚡ Most Important Commands

```bash
# Run ALL tests
mvn clean verify

# Run only API tests
mvn verify -Drunner=RunApiTests

# Run only UI tests
mvn verify -Drunner=RunUiTests

# Just compile (no tests)
mvn test-compile
```

---

## 📂 Key Files to Edit

```
src/test/resources/features/
├── api/plant_management.feature       ← Write API scenarios here
└── ui/plant_management.feature        ← Write UI scenarios here

src/test/java/com/qatraining/stepdefinitions/
├── api/PlantManagementApiStepDefinitions.java     ← Code API tests here
└── ui/PlantManagementUiStepDefinitions.java       ← Code UI tests here

src/test/java/com/qatraining/pages/
├── LoginPage.java        ← Add UI element selectors here
└── PlantsPage.java       ← Add UI element selectors here
```

---

## ✨ Your Workflow

```
1. Write Feature File (.feature)
        ↓
2. Run: mvn clean verify
        ↓
3. See PENDING/UNDEFINED steps error
        ↓
4. Write Step Definitions (.java)
        ↓
5. Run: mvn clean verify
        ↓
6. See PASSED/FAILED results
        ↓
7. Review: target/site/serenity/index.html
```

---

## 🎉 You're All Set!

Start writing tests now. Good luck! 🚀

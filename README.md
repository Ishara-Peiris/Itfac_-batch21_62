# Plant Management Automation Tests

A comprehensive test automation framework using **Serenity BDD** with **Cucumber** for Plant Management API and UI testing.

## 🛠 Tech Stack

- **Java 21**
- **Serenity BDD 4.1.3**
- **Cucumber 7.15.0**
- **JUnit 5**
- **Maven**
- **RestAssured** (for API testing)
- **Selenium WebDriver** (for UI testing)

## 📋 Test Cases

### API Tests (Student 2 - Hirusha-215124K)
| Test ID | Test Summary | Description |
|---------|--------------|-------------|
| API-PM-01 | Admin creates a new plant with valid details | Validates admin can create a plant with name, price, quantity, categoryId |
| API-PM-02 | Admin updates price and quantity of existing plant | Verifies ability to update inventory details for existing plant |
| API-PM-03 | Admin deletes a plant successfully | Ensures admin can remove a plant and it's no longer retrievable |

### UI Tests (Student 2 - Hirusha-215124K)
| Test ID | Test Summary | Description |
|---------|--------------|-------------|
| UI-PM-02 | Admin adds a new plant via Modal/Form | Verifies end-to-end flow of adding a plant through UI |
| UI-PM-03 | Admin validates empty fields in "Add Plant" form | Checks client-side validation messages for empty form |
| UI-PM-04 | Admin edits an existing plant's price | Verifies admin can modify plant details using Edit UI |

## 📁 Project Structure

```
src/test/
├── java/com/qatraining/
│   ├── pages/                  # Page Objects for UI testing
│   │   ├── BasePage.java
│   │   ├── LoginPage.java
│   │   └── PlantsPage.java
│   ├── runners/                # Cucumber test runners
│   │   ├── TestRunner.java     # Run all tests
│   │   ├── RunApiTests.java    # Run API tests only
│   │   └── RunUiTests.java     # Run UI tests only
│   ├── stepdefinitions/        # Cucumber step definitions
│   │   ├── api/PlantManagementApiStepDefinitions.java
│   │   ├── ui/PlantManagementUiStepDefinitions.java
│   │   └── Hooks.java
│   └── utils/                  # Utility classes
└── resources/
    ├── features/
    │   ├── api/plant_management.feature
    │   └── ui/plant_management.feature
    ├── serenity.conf
    ├── logback-test.xml
    └── test-config.properties
```

## 🚀 Getting Started

### Prerequisites

- Java 21 or higher
- Maven 3.8+
- Chrome browser (for UI tests)
- Application running on port 8080

### Installation

```bash
mvn clean install -DskipTests
```

## 🧪 Running Tests

### Run All Plant Management Tests

```bash
mvn clean verify
```

### Run API Tests Only (API-PM-01, API-PM-02, API-PM-03)

```bash
mvn clean verify -Dtest=RunApiTests
```

### Run UI Tests Only (UI-PM-02, UI-PM-03, UI-PM-04)

```bash
mvn clean verify -Dtest=RunUiTests
```

### Run Specific Test by Tag

```bash
# Run specific test
mvn clean verify -Dcucumber.filter.tags="@API-PM-01"
mvn clean verify -Dcucumber.filter.tags="@UI-PM-02"

# Run all create tests
mvn clean verify -Dcucumber.filter.tags="@create"

# Run all validation tests
mvn clean verify -Dcucumber.filter.tags="@validation"
```

### Run in Headless Mode

```bash
mvn clean verify -Dheadless.mode=true
```

## 📊 Reports

After test execution, Serenity reports are generated in:

```
target/site/serenity/index.html
```

To generate reports only:

```bash
mvn serenity:aggregate
```

## ⚙️ Configuration

### Application Under Test

The application runs on `http://localhost:8080`. Configure in `serenity.conf`:

```hocon
environments {
    default {
        webdriver.base.url = "http://localhost:8080"
        restapi.baseurl = "http://localhost:8080/api"
    }
}
```

## 🏷️ Tags

- `@api` - API tests
- `@ui` - UI tests  
- `@plant-management` - All plant management tests
- `@API-PM-01`, `@API-PM-02`, `@API-PM-03` - Specific API test IDs
- `@UI-PM-02`, `@UI-PM-03`, `@UI-PM-04` - Specific UI test IDs
- `@smoke` - Smoke tests
- `@create` - Create operation tests
- `@update` - Update operation tests
- `@delete` - Delete operation tests
- `@validation` - Validation tests
- `@negative` - Negative test scenarios
- `@ignore` - Tests to skip

## 👤 Tester

**Hirusha-215124K**

## 📄 License

This project is for training purposes.
```

## 🏷️ Tags

- `@api` - API tests
- `@ui` - UI tests
- `@smoke` - Smoke tests
- `@regression` - Regression tests
- `@negative` - Negative test scenarios
- `@ignore` - Tests to skip

## 🤝 Contributing

1. Create a feature branch
2. Write tests for new features
3. Ensure all tests pass
4. Submit a pull request

## 📄 License

This project is for training purposes.

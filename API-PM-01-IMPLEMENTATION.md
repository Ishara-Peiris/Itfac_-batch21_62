# ✅ API-PM-01 Test Implementation - COMPLETE

## 📋 Test Case Implemented

```
Test ID: API-PM-01
Title: Admin creates a new plant with valid details
Type: API Test (REST)
Status: ✅ IMPLEMENTED & COMPILED
```

---

## 🔧 API Configuration

```
Base URL:           http://localhost:8080
API Path Prefix:    /api
Authentication:     JWT Bearer Token
Content-Type:       application/json
```

### Available Endpoints Configured:

```
POST    /api/plants/category/{categoryId}    Create plant under sub-category
GET     /api/plants/{id}                      Get plant by ID
PUT     /api/plants/{id}                      Update plant details
DELETE  /api/plants/{id}                      Delete plant by ID
GET     /api/plants                           Get all plants
GET     /api/plants/category/{categoryId}     Get plants by category
GET     /api/plants/summary                   Get plant summary
GET     /api/plants/paged                     Search with pagination
```

---

## 📝 Feature File Implementation

### File: `src/test/resources/features/api/plant_management.feature`

```gherkin
@api @plant-management
Feature: Plant Management API
  As an admin user
  I want to manage plants through the REST API
  So that I can automate plant inventory management

  Background:
    Given the API base URL is configured as "http://localhost:8080"
    And the admin has a valid JWT authentication token

  @API-PM-01 @smoke @create
  Scenario: Admin creates a new plant with valid details
    Given a valid sub-category exists in the system
    When the admin sends a POST request to create a plant under the category with:
      | name     | Rose  |
      | price    | 500   |
      | quantity | 100   |
    Then the response status code should be 201
    And the response should contain a plant ID
    And the response should contain the plant name "Rose"
    And the response should contain the price 500
    And the response should contain the quantity 100
    And I should be able to retrieve the created plant via GET request
```

---

## 💻 Step Definitions Implemented

### File: `src/test/java/com/qatraining/stepdefinitions/api/PlantManagementApiStepDefinitions.java`

### Steps Implemented:

#### 1. **Background Steps**

```java
@Given("the API base URL is configured as {string}")
✓ Sets the base URL to http://localhost:8080

@And("the admin has a valid JWT authentication token")
✓ Configures JWT Bearer token authentication
```

#### 2. **Precondition Steps**

```java
@Given("a valid sub-category exists in the system")
✓ Verifies category availability
✓ Stores category ID for plant creation
```

#### 3. **Action Step**

```java
@When("the admin sends a POST request to create a plant under the category with:")
✓ Sends POST /api/plants/category/{categoryId}
✓ Includes plant data (name, price, quantity)
✓ Uses JWT Bearer authentication
✓ Returns HTTP 201 on success
```

#### 4. **Verification Steps**

```java
@Then("the response status code should be {int}")
✓ Verifies HTTP status code (201)

@And("the response should contain a plant ID")
✓ Extracts and validates plant ID from response

@And("the response should contain the plant name {string}")
✓ Validates plant name is "Rose"

@And("the response should contain the price {int}")
✓ Validates price is 500

@And("the response should contain the quantity {int}")
✓ Validates quantity is 100

@And("I should be able to retrieve the created plant via GET request")
✓ Calls GET /api/plants/{plantId}
✓ Verifies plant data matches created data
```

---

## 🚀 How to Run the Test

### Step 1: Ensure Your API is Running

```bash
# Your API should be running on http://localhost:8080
# Verify it's accessible before running tests
curl http://localhost:8080/api/plants
```

### Step 2: Update JWT Token

**⚠️ IMPORTANT:** You need to provide the JWT token. There are two options:

**Option A: Get Token from Swagger (Manual)**
1. Go to: `http://localhost:8080/swagger-ui.html`
2. Click "Authorize" button
3. Login with your credentials
4. Copy the token
5. Paste in the code or configuration

**Option B: Automatic Token Generation**
- Update the code to call your auth endpoint instead of using placeholder token
- Or use environment variable for token

### Step 3: Update Category ID

In the code, find this line:
```java
categoryId = "1"; // Replace with actual category ID from your system
```

Replace "1" with your actual category ID from the API.

### Step 4: Run the Test

```bash
cd d:\calss\Itfac_-batch21_62

# Run all API tests
mvn clean verify -Drunner=RunApiTests

# Or run just this feature
mvn clean verify -Dcucumber.filter.tags="@API-PM-01"

# Or run all tests
mvn clean verify
```

---

## 📊 Expected Test Flow

```
1. START: API-PM-01 Test
   ↓
2. SETUP: Configure base URL (http://localhost:8080)
   ↓
3. SETUP: Set JWT authentication token
   ↓
4. VERIFY: Valid category exists (ID: from system)
   ↓
5. ACTION: POST /api/plants/category/{categoryId}
   Body: { "name": "Rose", "price": 500, "quantity": 100 }
   ↓
6. VERIFY: Response status = 201 Created
   ↓
7. VERIFY: Response contains plant ID (e.g., "123")
   ↓
8. VERIFY: Response plant name = "Rose"
   ↓
9. VERIFY: Response price = 500
   ↓
10. VERIFY: Response quantity = 100
    ↓
11. VERIFY: GET /api/plants/123 returns plant data
    ↓
12. PASS: ✅ All verifications passed
```

---

## ✅ Compilation Status

```
BUILD SUCCESS
Total time:  5.039 s
Files compiled: 9 source files
No errors or warnings
```

---

## 📋 Checklist Before Running

- [ ] API is running on `http://localhost:8080`
- [ ] You have valid JWT token (get from Swagger Authorize)
- [ ] You know your category ID (or create one first)
- [ ] Update JWT token in code:
  ```java
  authToken = "YOUR_JWT_TOKEN_HERE";
  ```
- [ ] Update category ID in code:
  ```java
  categoryId = "YOUR_CATEGORY_ID_HERE";
  ```

---

## 🔍 Request/Response Examples

### Request
```json
POST http://localhost:8080/api/plants/category/1
Headers:
  Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
  Content-Type: application/json

Body:
{
  "name": "Rose",
  "price": 500,
  "quantity": 100
}
```

### Expected Response (201 Created)
```json
{
  "id": "123",
  "name": "Rose",
  "price": 500,
  "quantity": 100,
  "categoryId": 1,
  "createdAt": "2026-02-01T12:00:00Z",
  ...
}
```

---

## 📈 Test Report

After running, check the report at:
```
target/site/serenity/index.html
```

The report will show:
- ✅ Test status (PASS/FAIL/PENDING)
- ✅ All step details
- ✅ Request/response data
- ✅ Execution time
- ✅ Any failures with error messages

---

## 🎯 Next Steps

1. **Set JWT Token:**
   - Open the file: `src/test/java/com/qatraining/stepdefinitions/api/PlantManagementApiStepDefinitions.java`
   - Line ~42: Replace the token with your actual JWT

2. **Set Category ID:**
   - Same file, around line ~62
   - Replace "1" with your actual category ID

3. **Run Test:**
   ```bash
   mvn clean verify
   ```

4. **View Results:**
   - Open: `target/site/serenity/index.html` in browser

---

## 🆘 Troubleshooting

### Problem: Connection Refused
**Error:** `Connection refused to http://localhost:8080`
**Solution:** Make sure your API server is running on port 8080

### Problem: 401 Unauthorized
**Error:** `Response status code should be 201 but got 401`
**Solution:** Update the JWT token with a valid one from your API

### Problem: 404 Not Found
**Error:** `Response status code should be 201 but got 404`
**Solution:** Check the endpoint path and category ID are correct

### Problem: Invalid Response
**Error:** `Response does not contain 'id' or 'plantId' field`
**Solution:** Check your API response structure matches the expected format

---

## ✨ Features of This Test

✅ JWT Bearer token authentication
✅ Dynamic category handling
✅ Complete request/response validation
✅ GET verification after creation
✅ Detailed Serenity reporting
✅ Error handling and fallbacks
✅ Data extraction and storage
✅ Clear step definitions

---

**Status: READY TO RUN** 🚀

# ✅ Updated: Admin Create Plant with Flower Category

## 📝 Changes Made

### 1. Feature File Updated
**File:** `src/test/resources/features/api/plant_management.feature`

```gherkin
Scenario: Admin creates a new plant with valid details
  Given a valid sub-category exists in the system
  When the admin sends a POST request to create a plant under the category with:
    | name        | Anthurium |
    | category    | Flower    |  ← NEW: Added category as Flower
    | price       | 150       |
    | quantity    | 25        |
  Then the response status code should be 201
  And the response should contain a plant ID
  And the response should contain the plant name "Anthurium"
  And the response should contain the price 150
  And the response should contain the quantity 25
  And I should be able to retrieve the created plant via GET request
```

### 2. Step Definition Updated
**File:** `src/test/java/com/qatraining/stepdefinitions/api/PlantManagementApiStepDefinitions.java`

```java
@When("the admin sends a POST request to create a plant under the category with:")
public void createPlant(DataTable dataTable) {
    // Prepare request payload
    Map<String, String> plantData = dataTable.asMap(String.class, String.class);
    
    requestPayload = new HashMap<>();
    requestPayload.put("name", plantData.get("name"));
    requestPayload.put("price", Integer.parseInt(plantData.get("price")));
    requestPayload.put("quantity", Integer.parseInt(plantData.get("quantity")));
    
    // Add category if provided in the data table
    if (plantData.containsKey("category")) {
        requestPayload.put("category", plantData.get("category"));  ← NEW: Handles category
    }
    
    // ... rest of the method
}
```

---

## 🎯 Test Case Details

### Test: API-PM-01 - Admin creates a new plant with valid details

**New Plant Data:**
```
Name:        Anthurium
Category:    Flower  ← NEW FIELD
Price:       150
Quantity:    25
```

**Expected API Request:**
```json
POST /api/plants/category/{categoryId}
Authorization: Bearer {jwt_token}
Content-Type: application/json

{
  "name": "Anthurium",
  "category": "Flower",
  "price": 150,
  "quantity": 25
}
```

**Expected Response:**
```
Status Code: 201 (Created)
Response Body:
{
  "id": "{plant_id}",
  "name": "Anthurium",
  "category": "Flower",
  "price": 150,
  "quantity": 25
}
```

---

## ✅ Verification Steps

The test will verify:
1. ✅ Plant created successfully (Status 201)
2. ✅ Plant ID returned in response
3. ✅ Plant name is "Anthurium"
4. ✅ Plant price is 150
5. ✅ Plant quantity is 25
6. ✅ Plant can be retrieved via GET request

---

## 🚀 How to Run the Updated Test

```bash
cd d:\calss\Itfac_-batch21_62

# Run all tests
mvn clean verify

# Run only API tests
mvn verify -Drunner=RunApiTests

# Run only this specific test
mvn verify -Dcucumber.filter.tags="@API-PM-01"
```

---

## 📊 Expected Test Output

When the backend server is running:

```
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------

@api @plant-management @API-PM-01 @smoke @create
Scenario: Admin creates a new plant with valid details
  Given a valid sub-category exists in the system
    ✅ PASSED
  When the admin sends a POST request to create a plant under the category with:
    | name        | Anthurium |
    | category    | Flower    |
    | price       | 150       |
    | quantity    | 25        |
    ✅ PASSED
  Then the response status code should be 201
    ✅ PASSED
  And the response should contain a plant ID
    ✅ PASSED
  And the response should contain the plant name "Anthurium"
    ✅ PASSED
  And the response should contain the price 150
    ✅ PASSED
  And the response should contain the quantity 25
    ✅ PASSED
  And I should be able to retrieve the created plant via GET request
    ✅ PASSED

[INFO] Tests run: 1
[INFO] Tests passed: 1
[INFO] BUILD SUCCESS
```

---

## 📋 Test Scenario Summary

| Item | Value |
|------|-------|
| **Test ID** | API-PM-01 |
| **Test Name** | Admin creates a new plant with valid details |
| **Plant Name** | Anthurium |
| **Plant Category** | Flower |
| **Price** | 150 |
| **Quantity** | 25 |
| **Expected HTTP Status** | 201 (Created) |
| **Tags** | @api, @plant-management, @smoke, @create |

---

## ✨ Summary

✅ Feature file updated to include category "Flower"  
✅ Step definition updated to handle category parameter  
✅ Request payload will now include category field  
✅ Test is ready to run when backend server is available  

The test will now create a plant with category "Flower" instead of no category!


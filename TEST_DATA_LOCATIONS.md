# 📍 Test Data Locations - Category, Price & Plant Details

## 1️⃣ **Feature File - Test Scenario Data**

### File: 
[src/test/resources/features/api/plant_management.feature](src/test/resources/features/api/plant_management.feature)

### Plant Details (Test Data):

```gherkin
Line 15-18:  Plant Information
  | name     | Anthurium |    ← Line 16: Plant Name
  | price    | 150       |    ← Line 17: Plant Price
  | quantity | 25        |    ← Line 18: Plant Quantity
```

### Price Validation:

```gherkin
Line 22-24:  Assertions
  And the response should contain the plant name "Anthurium"    ← Line 22
  And the response should contain the price 150                 ← Line 23
  And the response should contain the quantity 25               ← Line 24
```

---

## 2️⃣ **Step Definitions - Code Implementation**

### File:
[src/test/java/com/qatraining/stepdefinitions/api/PlantManagementApiStepDefinitions.java](src/test/java/com/qatraining/stepdefinitions/api/PlantManagementApiStepDefinitions.java)

### Plant Creation with Data:

```java
Line 126-135:  Extracting Test Data
  @When("the admin sends a POST request to create a plant under the category with:")
  public void createPlant(DataTable dataTable) {
      // Line 128: Convert DataTable to Map
      Map<String, String> plantData = dataTable.asMap(String.class, String.class);
      
      // Line 130-132: Extract individual values
      requestPayload = new HashMap<>();
      requestPayload.put("name", plantData.get("name"));           ← Line 131: NAME
      requestPayload.put("price", Integer.parseInt(plantData.get("price")));  ← Line 132: PRICE
      requestPayload.put("quantity", Integer.parseInt(plantData.get("quantity")));  ← Line 133: QUANTITY
```

### Category ID:

```java
Line 77-128:   Category Retrieval
  @Given("a valid sub-category exists in the system")
  public void ensureCategoryExists() {
      // Line 81: GET sub-categories endpoint
      .get(API_BASE_PATH + "/subcategories");
      
      // Line 92: Extract category ID
      Integer subCatId = subcategoriesResponse.jsonPath().getInt("[0].id");
      categoryId = subCatId != null ? subCatId.toString() : "1";  ← Line 93: CATEGORY ID
```

### API Endpoint Call:

```java
Line 139-145:  Send POST Request with Price & Plant Data
  String endpoint = API_BASE_PATH + "/plants/category/" + categoryId;
  
  response = SerenityRest.given()
      .baseUri(baseUrl)
      .header("Authorization", "Bearer " + authToken)
      .contentType("application/json")
      .body(requestPayload)              ← Line 144: Includes price & name
      .when()
      .post(endpoint);
```

### Price Verification:

```java
Line 198-206:  Verify Price in Response
  @And("the response should contain the price {int}")
  public void verifyPrice(int expectedPrice) {          ← Line 199
      Integer actualPrice = response.jsonPath().getInt("price");  ← Line 200: Extract price
      assertThat(actualPrice)
          .as("Price should be " + expectedPrice + " but got " + actualPrice)
          .isEqualTo(expectedPrice);                    ← Line 203: Verify price 150
```

### Plant Name Verification:

```java
Line 190-197:  Verify Plant Name in Response
  @And("the response should contain the plant name {string}")
  public void verifyPlantName(String expectedName) {    ← Line 191
      String actualName = response.jsonPath().getString("name");  ← Line 192
      assertThat(actualName)
          .as("Plant name should be '" + expectedName + "' but got '" + actualName + "'")
          .isEqualTo(expectedName);                      ← Line 195: Verify "Anthurium"
```

### Quantity Verification:

```java
Line 207-215:  Verify Quantity in Response
  @And("the response should contain the quantity {int}")
  public void verifyQuantity(int expectedQuantity) {    ← Line 208
      Integer actualQuantity = response.jsonPath().getInt("quantity");  ← Line 209
      assertThat(actualQuantity)
          .as("Quantity should be " + expectedQuantity + " but got " + actualQuantity)
          .isEqualTo(expectedQuantity);                  ← Line 212: Verify 25
```

---

## 📊 **Test Data Summary**

| Data | Value | Feature File Line | Step Definition Line |
|------|-------|---|---|
| **Plant Name** | Anthurium | Line 16 | Line 131 (extraction), Line 192 (verification) |
| **Price** | 150 | Line 17 | Line 132 (extraction), Line 200 (verification) |
| **Quantity** | 25 | Line 18 | Line 133 (extraction), Line 209 (verification) |
| **Category ID** | 1 (or from subcategories) | - | Line 93 (retrieval) |
| **API Endpoint** | /api/plants/category/{categoryId} | - | Line 139 (creation) |

---

## 🔄 **Data Flow**

```
1. Feature File (plant_management.feature:16-18)
   ↓ Plant data in DataTable (Anthurium, 150, 25)
   
2. Step Definition (PlantManagementApiStepDefinitions.java:128)
   ↓ Extract values from DataTable
   
3. Create Request Payload (Line 130-133)
   ↓ Build JSON: { name: "Anthurium", price: 150, quantity: 25 }
   
4. Send POST Request (Line 144)
   ↓ POST /api/plants/category/1
   
5. Verify Response (Line 200, 192, 209)
   ↓ Assert price=150, name="Anthurium", quantity=25
```

---

## 💡 **Key Locations**

| What | File | Line |
|------|------|------|
| Test plant name | Feature file | 16 |
| Test plant price | Feature file | 17 |
| Test plant quantity | Feature file | 18 |
| Extract plant name | Step Definition | 131 |
| Extract plant price | Step Definition | 132 |
| Extract plant quantity | Step Definition | 133 |
| Verify plant name | Step Definition | 192 |
| Verify plant price | Step Definition | 200 |
| Verify plant quantity | Step Definition | 209 |
| Get category ID | Step Definition | 93 |
| API endpoint call | Step Definition | 139-144 |

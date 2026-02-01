package com.qatraining.stepdefinitions.api;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import io.restassured.response.Response;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.rest.SerenityRest;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

/**
 * Step definitions for Plant Management API tests
 * Test Case: API-PM-01 - Admin creates a new plant with valid details
 * Base URL: http://localhost:8080
 * Authentication: JWT (Bearer Token)
 */
public class PlantManagementApiStepDefinitions {
    
    private String baseUrl;
    private String authToken;
    private Response response;
    private Map<String, Object> requestPayload;
    private String createdPlantId;
    private String categoryId;
    private static final String API_BASE_PATH = "/api";

    @Given("the API base URL is configured as {string}")
    public void setBaseUrl(String url) {
        baseUrl = url;
        Serenity.recordReportData().withTitle("API Base URL").andContents(baseUrl);
    }

    @And("the admin has a valid JWT authentication token")
    public void setJwtToken() {
        // Call authentication endpoint to get JWT token dynamically
        try {
            // Prepare login credentials
            Map<String, String> loginPayload = new HashMap<>();
            loginPayload.put("username", "admin");
            loginPayload.put("password", "admin123");
            
            // Call POST /api/auth/login to get JWT token
            Response authResponse = SerenityRest.given()
                    .baseUri(baseUrl)
                    .contentType("application/json")
                    .body(loginPayload)
                    .when()
                    .post(API_BASE_PATH + "/auth/login");
            
            // Verify authentication was successful
            if (authResponse.getStatusCode() == 200) {
                authToken = authResponse.jsonPath().getString("token");
                
                Serenity.recordReportData()
                        .withTitle("JWT Authentication ✓")
                        .andContents("✓ Successfully obtained JWT token from /api/auth/login\n" +
                                "Token (first 50 chars): " + authToken.substring(0, Math.min(50, authToken.length())) + "...\n" +
                                "Status Code: 200");
            } else {
                throw new RuntimeException("Authentication failed! Status Code: " + authResponse.getStatusCode() + 
                        "\nResponse: " + authResponse.getBody().asString());
            }
            
            Serenity.setSessionVariable("authToken").to(authToken);
        } catch (Exception e) {
            Serenity.recordReportData()
                    .withTitle("JWT Authentication ✗ ERROR")
                    .andContents("Failed to get JWT token: " + e.getMessage());
            throw new RuntimeException("Failed to authenticate: " + e.getMessage(), e);
        }
    }

    @Given("a valid sub-category exists in the system")
    public void ensureCategoryExists() {
        try {
            // First, try to get all sub-categories via /api/subcategories endpoint
            Response subcategoriesResponse = SerenityRest.given()
                    .baseUri(baseUrl)
                    .header("Authorization", "Bearer " + authToken)
                    .contentType("application/json")
                    .when()
                    .get(API_BASE_PATH + "/subcategories");
            
            if (subcategoriesResponse.getStatusCode() == 200) {
                String responseBody = subcategoriesResponse.getBody().asString();
                
                // Try to get the first sub-category ID
                try {
                    Integer subCatId = subcategoriesResponse.jsonPath().getInt("[0].id");
                    categoryId = subCatId != null ? subCatId.toString() : "1";
                } catch (Exception e) {
                    // Fallback try different structure
                    try {
                        categoryId = subcategoriesResponse.jsonPath().getString("[0]");
                    } catch (Exception ex) {
                        categoryId = "1";
                    }
                }
                
                Serenity.recordReportData()
                        .withTitle("Sub-Category Lookup ✓")
                        .andContents("Sub-Categories API Status: " + subcategoriesResponse.getStatusCode() + 
                                    "\nUsing Sub-Category ID: " + categoryId +
                                    "\nResponse: " + responseBody);
            } else {
                categoryId = "1";
                Serenity.recordReportData()
                        .withTitle("Sub-Category Lookup - Fallback")
                        .andContents("Sub-Categories API Status: " + subcategoriesResponse.getStatusCode() + 
                                    "\nFalling back to category ID: 1");
            }
        } catch (Exception e) {
            categoryId = "1";
            Serenity.recordReportData()
                    .withTitle("Sub-Category Lookup - Error")
                    .andContents("Error fetching sub-categories. Using default category ID: 1\nError: " + e.getMessage());
        }
        
        Serenity.setSessionVariable("categoryId").to(categoryId);
    }

    @When("the admin sends a POST request to create a plant under the category with:")
    public void createPlant(DataTable dataTable) {
        // Prepare request payload
        Map<String, String> plantData = dataTable.asMap(String.class, String.class);
        
        requestPayload = new HashMap<>();
        requestPayload.put("name", plantData.get("name"));
        requestPayload.put("price", Integer.parseInt(plantData.get("price")));
        requestPayload.put("quantity", Integer.parseInt(plantData.get("quantity")));

        Serenity.recordReportData()
                .withTitle("Plant Creation Request")
                .andContents("Endpoint: POST /api/plants/category/" + categoryId + 
                           "\nPayload: " + requestPayload.toString());

        // Send POST request to create plant under category
        String endpoint = API_BASE_PATH + "/plants/category/" + categoryId;
        
        response = SerenityRest.given()
                .baseUri(baseUrl)
                .header("Authorization", "Bearer " + authToken)
                .contentType("application/json")
                .body(requestPayload)
                .when()
                .post(endpoint);

        Serenity.recordReportData()
                .withTitle("Response Status")
                .andContents("HTTP " + response.getStatusCode());
        
        Serenity.recordReportData()
                .withTitle("Response Body")
                .andContents(response.getBody().asString());
    }

    @Then("the response status code should be {int}")
    public void verifyStatusCode(int expectedCode) {
        assertThat(response.getStatusCode())
                .as("Expected HTTP " + expectedCode + " but got " + response.getStatusCode())
                .isEqualTo(expectedCode);
        
        Serenity.recordReportData()
                .withTitle("Status Code Verification ✓")
                .andContents("HTTP " + expectedCode + " - Created successfully");
    }

    @And("the response should contain a plant ID")
    public void extractPlantId() {
        try {
            // Try common ID field names
            String jsonPath = "id";
            try {
                createdPlantId = response.jsonPath().getString(jsonPath);
            } catch (Exception e) {
                // Try alternative field names
                createdPlantId = response.jsonPath().getString("plantId");
            }
            
            assertThat(createdPlantId).isNotNull().isNotEmpty();
            
            Serenity.setSessionVariable("plantId").to(createdPlantId);
            Serenity.recordReportData()
                    .withTitle("Plant ID Verification ✓")
                    .andContents("Plant ID: " + createdPlantId);
        } catch (Exception e) {
            throw new AssertionError("Response does not contain 'id' or 'plantId' field.\n" +
                    "Response: " + response.getBody().asString(), e);
        }
    }

    @And("the response should contain the plant name {string}")
    public void verifyPlantName(String expectedName) {
        String actualName = response.jsonPath().getString("name");
        assertThat(actualName)
                .as("Plant name should be '" + expectedName + "' but got '" + actualName + "'")
                .isEqualTo(expectedName);
        
        Serenity.recordReportData()
                .withTitle("Plant Name Verification ✓")
                .andContents("Name: " + actualName);
    }

    @And("the response should contain the price {int}")
    public void verifyPrice(int expectedPrice) {
        Double actualPrice = response.jsonPath().getDouble("price");
        int actualPriceInt = actualPrice == null ? 0 : actualPrice.intValue();
        assertThat(actualPriceInt)
                .as("Price should be " + expectedPrice + " but got " + actualPrice)
                .isEqualTo(expectedPrice);

        Serenity.recordReportData()
                .withTitle("Price Verification ✓")
                .andContents("Price: " + actualPrice);
    }

    @And("the response should contain the quantity {int}")
    public void verifyQuantity(int expectedQuantity) {
        Double actualQuantity = response.jsonPath().getDouble("quantity");
        int actualQuantityInt = actualQuantity == null ? 0 : actualQuantity.intValue();
        assertThat(actualQuantityInt)
                .as("Quantity should be " + expectedQuantity + " but got " + actualQuantity)
                .isEqualTo(expectedQuantity);

        Serenity.recordReportData()
                .withTitle("Quantity Verification ✓")
                .andContents("Quantity: " + actualQuantity);
    }

    @And("I should be able to retrieve the created plant via GET request")
    public void retrieveCreatedPlant() {
        String getEndpoint = API_BASE_PATH + "/plants/" + createdPlantId;
        
        response = SerenityRest.given()
                .baseUri(baseUrl)
                .header("Authorization", "Bearer " + authToken)
                .contentType("application/json")
                .when()
                .get(getEndpoint);

        assertThat(response.getStatusCode())
                .as("GET request for plant " + createdPlantId + " should return 200")
                .isEqualTo(200);

        String retrievedName = response.jsonPath().getString("name");
        assertThat(retrievedName)
                .as("Retrieved plant name should match created plant name")
                .isEqualTo((String) requestPayload.get("name"));
        
        Serenity.recordReportData()
                .withTitle("Plant Retrieval Verification ✓")
                .andContents("Successfully retrieved plant via GET /api/plants/" + createdPlantId +
                           "\nPlant Name: " + retrievedName);
    }

    // ==================== API-PM-02: Update Plant Price and Quantity ====================

    @Given("a plant exists with ID {string}")
    public void plantExistsWithId(String plantId) {
        createdPlantId = plantId;
        
        Serenity.recordReportData()
                .withTitle("Plant ID Setup ✓")
                .andContents("Using Plant ID: " + plantId + " for update operation");
        
        Serenity.setSessionVariable("plantIdForUpdate").to(plantId);
    }

    @When("the admin sends a PUT request to update the plant with:")
    public void updatePlant(DataTable dataTable) {
        // Prepare update payload
        Map<String, String> updateData = dataTable.asMap(String.class, String.class);
        
        Map<String, Object> updatePayload = new HashMap<>();
                // Some APIs require the full entity for PUT; fetch existing plant to include required fields (eg. name)
                try {
                        String getEndpoint = API_BASE_PATH + "/plants/" + createdPlantId;
                        Response getResp = SerenityRest.given()
                                        .baseUri(baseUrl)
                                        .header("Authorization", "Bearer " + authToken)
                                        .contentType("application/json")
                                        .when()
                                        .get(getEndpoint);

                        if (getResp.getStatusCode() == 200) {
                                // include existing name to satisfy validation
                                String existingName = getResp.jsonPath().getString("name");
                                if (existingName != null && !existingName.isEmpty()) {
                                        updatePayload.put("name", existingName);
                                }
                        } else {
                                // Fallback: if we created this plant in the same test run, try to use requestPayload
                                if (requestPayload != null && requestPayload.get("name") != null) {
                                        updatePayload.put("name", requestPayload.get("name"));
                                }
                        }
                } catch (Exception e) {
                        // ignore and proceed; validation will catch missing fields
                }

                updatePayload.put("price", Integer.parseInt(updateData.get("price")));
                updatePayload.put("quantity", Integer.parseInt(updateData.get("quantity")));
        
        Serenity.recordReportData()
                .withTitle("Plant Update Request")
                .andContents("Endpoint: PUT /api/plants/" + createdPlantId + 
                           "\nPayload: " + updatePayload.toString());
        
        // Send PUT request to update plant
        String endpoint = API_BASE_PATH + "/plants/" + createdPlantId;
        
        response = SerenityRest.given()
                .baseUri(baseUrl)
                .header("Authorization", "Bearer " + authToken)
                .contentType("application/json")
                .body(updatePayload)
                .when()
                .put(endpoint);
        
        Serenity.recordReportData()
                .withTitle("Update Response Status")
                .andContents("HTTP " + response.getStatusCode());
        
        Serenity.recordReportData()
                .withTitle("Update Response Body")
                .andContents(response.getBody().asString());
    }

    @And("the response should contain the updated price {int}")
    public void verifyUpdatedPrice(int expectedPrice) {
        Double actualPrice = response.jsonPath().getDouble("price");
        int actualPriceInt = actualPrice == null ? 0 : actualPrice.intValue();
        assertThat(actualPriceInt)
                .as("Updated price should be " + expectedPrice + " but got " + actualPrice)
                .isEqualTo(expectedPrice);

        Serenity.recordReportData()
                .withTitle("Updated Price Verification ✓")
                .andContents("Price: " + actualPrice + " (Updated from 150 to " + expectedPrice + ")");
    }

    @And("the response should contain the updated quantity {int}")
    public void verifyUpdatedQuantity(int expectedQuantity) {
        Double actualQuantity = response.jsonPath().getDouble("quantity");
        int actualQuantityInt = actualQuantity == null ? 0 : actualQuantity.intValue();
        assertThat(actualQuantityInt)
                .as("Updated quantity should be " + expectedQuantity + " but got " + actualQuantity)
                .isEqualTo(expectedQuantity);

        Serenity.recordReportData()
                .withTitle("Updated Quantity Verification ✓")
                .andContents("Quantity: " + actualQuantity + " (Updated from 25 to " + expectedQuantity + ")");
    }

    @And("I should be able to retrieve the updated plant via GET request to verify changes")
    public void retrieveAndVerifyUpdatedPlant() {
        try {
            String getEndpoint = API_BASE_PATH + "/plants/" + createdPlantId;
            
            Response getResponse = SerenityRest.given()
                    .baseUri(baseUrl)
                    .header("Authorization", "Bearer " + authToken)
                    .contentType("application/json")
                    .when()
                    .get(getEndpoint);
            
            assertThat(getResponse.getStatusCode())
                    .as("GET request for updated plant should return 200")
                    .isEqualTo(200);
            
            // Verify updated values from GET response
            Double retrievedPriceD = getResponse.jsonPath().getDouble("price");
            Double retrievedQuantityD = getResponse.jsonPath().getDouble("quantity");

            int retrievedPrice = retrievedPriceD == null ? 0 : retrievedPriceD.intValue();
            int retrievedQuantity = retrievedQuantityD == null ? 0 : retrievedQuantityD.intValue();

            Double respPriceD = response.jsonPath().getDouble("price");
            Double respQuantityD = response.jsonPath().getDouble("quantity");

            int respPrice = respPriceD == null ? 0 : respPriceD.intValue();
            int respQuantity = respQuantityD == null ? 0 : respQuantityD.intValue();

            assertThat(retrievedPrice)
                    .as("Retrieved price from GET should match updated price")
                    .isEqualTo(respPrice);

            assertThat(retrievedQuantity)
                    .as("Retrieved quantity from GET should match updated quantity")
                    .isEqualTo(respQuantity);
            
            Serenity.recordReportData()
                    .withTitle("Updated Plant Verification ✓")
                    .andContents("Successfully retrieved updated plant via GET /api/plants/" + createdPlantId +
                               "\nUpdated Price: " + retrievedPrice +
                               "\nUpdated Quantity: " + retrievedQuantity);
        } catch (Exception e) {
            throw new AssertionError("Failed to retrieve updated plant: " + e.getMessage(), e);
        }
    }
}

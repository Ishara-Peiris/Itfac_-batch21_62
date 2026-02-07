package com.qatraining.stepdefinitions.api;

import io.cucumber.java.en.*;
import net.serenitybdd.rest.SerenityRest;
import io.restassured.response.Response;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;

public class CategoryAPISteps {

    private final String BASE_URL = "http://localhost:8080";
    private String jwtToken;

    @Given("the API is authorized as {string}")
    public void authorizeAs(String role) {
        // 1. Perform a real Login to get the JWT Token
        Response response = SerenityRest.given()
                .baseUri(BASE_URL)
                .contentType("application/json")
                .body("{\"username\":\"admin\", \"password\":\"admin123\"}")
                .post("/api/auth/login");

        // 2. Extract the token from the response
        jwtToken = response.then().extract().path("token");

        System.out.println("DEBUG: Obtained JWT Token: " + jwtToken);
    }

    @When("I send a GET request to {string}")
    public void sendGetRequest(String path) {
        SerenityRest.given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .get(path);
    }

    @Then("the API response status should be {int}")
    public void verifyStatus(int statusCode) {
        SerenityRest.then().log().ifValidationFails().statusCode(statusCode);
    }

    @And("the response should contain a list of categories")
    public void verifyCategoryList() {
        SerenityRest.then().body("$", is(not(empty())));
    }

    @And("the response should contain category summary data")
    public void verifyCategorySummary() {
        SerenityRest.then()
                .body("$", notNullValue())
                .log().all();
    }

    @When("I send a POST request to {string} with name {string}")
    public void sendPostRequest(String path, String categoryName) {
        String jsonBody = "{\"name\": \"" + categoryName + "\"}";

        SerenityRest.given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + jwtToken)
                .contentType("application/json")
                .body(jsonBody)
                .when()
                .post(path);
    }

    @And("the response should contain the created category ID")
    public void verifyCategoryId() {
        SerenityRest.then()
                .body("id", notNullValue())
                .log().all();
    }

    @When("I send a POST request to {string} for sub-category {string} with parent ID {int}")
    public void sendPostRequestSubCategory(String path, String name, int parentId) {
        // Construct JSON body with parentId
        String jsonBody = "{" +
                "\"name\": \"" + name + "\"," +
                "\"parentId\": " + parentId +
                "}";

        SerenityRest.given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + jwtToken)
                .contentType("application/json")
                .body(jsonBody)
                .when()
                .post(path);
    }

    @And("the response should reflect the parent-child relationship with ID {int}")
    public void verifyParentRelationship(int expectedParentId) {
        // Get the name of the sub-category we just created from the last response
        String subCategoryName = SerenityRest.lastResponse().jsonPath().getString("name");

        // Verify by checking if the parent category now contains this sub-category in its list
        SerenityRest.given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .get("/api/categories/" + expectedParentId)
                .then()
                .body("subCategories.name", hasItem(subCategoryName))
                .log().all();
    }

    @When("I send a PUT request to {string} with ID {int} and new name {string}")
    public void sendPutRequest(String path, int id, String newName) {
        String jsonBody = "{\"name\": \"" + newName + "\"}";

        SerenityRest.given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + jwtToken)
                .contentType("application/json")
                .body(jsonBody)
                .when()
                .put(path + "/" + id); // Appends the ID to the path
    }

    @When("I send a DELETE request to {string} with ID {int}")
    public void sendDeleteRequest(String path, int id) {
        SerenityRest.given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .delete(path + "/" + id);
    }

    @And("the category with ID {int} should no longer be accessible")
    public void verifyCategoryDeleted(int id) {
        // Try to GET the deleted category; it should return 404 Not Found
        SerenityRest.given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .get("/api/categories/" + id)
                .then()
                .statusCode(404);
    }

    @When("I search for categories with name {string}, page {int}, size {int}, and sort {string}")
    public void searchCategories(String name, int page, int size, String sort) {
        SerenityRest.given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + jwtToken)
                // Sending parameters as query params: ?name=Rose&page=0&size=5&sort=id,asc
                .queryParam("name", name)
                .queryParam("page", page)
                .queryParam("size", size)
                .queryParam("sort", sort)
                .when()
                .get("/api/categories/page");
    }

    @And("the response should contain a paginated list of results")
    public void verifyPagination() {
        SerenityRest.then()
                .body("content", notNullValue())
                .body("totalPages", notNullValue())
                .body("totalElements", notNullValue());

        // Log the search result for manual verification in console
        System.out.println("Search Results: " + SerenityRest.lastResponse().asString());
    }

    @Given("the API is authorized as a standard {string}")
    public void authorizeAsUser(String role) {
        // Log in with testuser credentials using the correct password 'test123'
        Response response = SerenityRest.given()
                .baseUri(BASE_URL)
                .contentType("application/json")
                .body("{\"username\":\"testuser\", \"password\":\"test123\"}")
                .post("/api/auth/login");

        // Extract the token
        jwtToken = response.then().extract().path("token");

        // Validation to ensure the test doesn't proceed with a null token
        if (jwtToken == null) {
            System.err.println("CRITICAL ERROR: Could not log in testuser. Response: " + response.asString());
        }
        assertNotNull("JWT Token should not be null for testuser login", jwtToken);
    }

    @Then("the API should return a forbidden error status {int}")
    public void verifyForbidden(int statusCode) {
        SerenityRest.then()
                .log().ifValidationFails()
                .statusCode(statusCode); // Now properly expecting 403
    }


    @When("I send a GET request for a non-existent category with ID {int}")
    public void getNonExistentCategory(int id) {
        SerenityRest.given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .get("/api/categories/" + id);
    }

    @And("the response should contain an error message {string}")
    public void verifyErrorMessage(String expectedMessage) {
        SerenityRest.then()
                .body("message", containsString(expectedMessage));
    }


    @And("the response should contain a validation error for {string}")
    public void verifyValidationError(String field) {
        SerenityRest.then()
                .body("error", equalTo("BAD_REQUEST"))
                .body("message", containsString("Validation failed"))
                .body("details." + field, notNullValue());

        // Log details for the report
        System.out.println("Validation Error Details: " + SerenityRest.lastResponse().jsonPath().get("details"));
    }




}
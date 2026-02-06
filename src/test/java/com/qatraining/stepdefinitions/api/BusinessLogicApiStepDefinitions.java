package com.qatraining.stepdefinitions.api;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import net.serenitybdd.rest.SerenityRest;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import java.util.HashMap;
import java.util.Map;

public class BusinessLogicApiStepDefinitions {

    private String authToken;

    private String getBaseUrl() {
        return "http://localhost:8080/api";
    }

    @Given("I am authenticated as {string}")
    public void authenticate(String role) {
        // Use centralized AuthenticationManager to obtain JWT tokens
        if (role.equalsIgnoreCase("admin")) {
            this.authToken = com.qatraining.hooks.Authenticationmanager.getAdminToken();
        } else {
            this.authToken = com.qatraining.hooks.Authenticationmanager.getTestUserToken();
        }
    }

    @Given("a category exists with id {int}")
    public void checkCategoryExists(int id) {
        // Optional: In a real test, we might create one here. 
        // For now, we assume ID 1 exists as per deployment data.
    }

    // API-BL-01
    @When("I create a new category with name {string}")
    public void createCategory(String name) {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("name", name);
        
        SerenityRest.given()
            .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(jsonMap)
                .post(getBaseUrl() + "/categories");
    }

    @When("I attempt to create the same category {string} again")
    public void createDuplicateCategory(String name) {
        createCategory(name); // Call the same method to try creating it again
    }

    // API-BL-02, API-BL-03
    @When("I attempt to create a plant with name {string} and quantity {int}")
    public void createPlantWithQuantity(String name, int quantity) {
        createPlant(name, 100.0, quantity);
    }

    @When("I create a plant with name {string} price {double} and quantity {int}")
    public void createPlant(String name, double price, int quantity) {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("name", name);
        jsonMap.put("price", price);
        jsonMap.put("quantity", quantity);
        
        Map<String, Object> categoryMap = new HashMap<>();
        categoryMap.put("id", 1); // Assuming category ID 1 exists
        jsonMap.put("category", categoryMap);

        SerenityRest.given()
            .header("Authorization", "Bearer " + authToken)
                .contentType(ContentType.JSON)
                .body(jsonMap)
                .post(getBaseUrl() + "/plants");
    }

    // API-BL-04, API-BL-10
    @When("I retrieve the category details for id {int}")
    public void getCategoryById(int id) {
        SerenityRest.given()
            .header("Authorization", "Bearer " + authToken)
            .when().get(getBaseUrl() + "/categories/" + id);
    }

    @When("I request category details for invalid id {int}")
    public void getInvalidCategory(int id) {
        SerenityRest.given()
            .header("Authorization", "Bearer " + authToken)
            .when().get(getBaseUrl() + "/categories/" + id);
    }

    // API-BL-05
    @When("I request plant details for invalid id {int}")
    public void getInvalidPlant(int id) {
        SerenityRest.given()
            .header("Authorization", "Bearer " + authToken)
            .when().get(getBaseUrl() + "/plants/" + id);
    }

    // API-BL-06
    @When("I search for plants with name {string}")
    public void searchPlants(String name) {
        SerenityRest.given()
            .header("Authorization", "Bearer " + authToken)
            .queryParam("name", name)
            .when()
            .get(getBaseUrl() + "/plants");
    }

    // API-BL-07
    @When("I retrieve all categories with hierarchy")
    public void getAllCategories() {
        SerenityRest.given()
            .header("Authorization", "Bearer " + authToken)
            .when().get(getBaseUrl() + "/categories");
    }

    // API-BL-08
    @When("I request the plant list with page {int} and size {int}")
    public void getPlantsPaginated(int page, int size) {
        SerenityRest.given()
            .header("Authorization", "Bearer " + authToken)
            .queryParam("page", page)
            .queryParam("size", size)
            .when()
            .get(getBaseUrl() + "/plants");
    }

    // API-BL-09
    @When("I attempt to delete sale record with id {int}")
    public void deleteSale(int id) {
        SerenityRest.given()
            .header("Authorization", "Bearer " + authToken)
            .when().delete(getBaseUrl() + "/sales/" + id);
    }

    // -----------------------------------------------------------
    // ASSERTIONS (THEN steps)
    // -----------------------------------------------------------

    @Then("the response status code should be {int}")
    public void verifyStatusCode(int statusCode) {
        SerenityRest.then().statusCode(statusCode);
    }

    @Then("the response should contain valid category data")
    public void verifyCategoryData() {
        SerenityRest.then().body("id", Matchers.notNullValue());
        SerenityRest.then().body("name", Matchers.notNullValue());
    }

    @Then("the response body should contain a list of plants")
    public void verifyPlantList() {
        // Checks that the body is a list (array) or has content
        SerenityRest.then().body("content", Matchers.notNullValue());
    }

    @Then("the response should be a list")
    public void verifyResponseIsList() {
        // Verifies the root is an array (JSON list)
        SerenityRest.then().body("$", Matchers.instanceOf(java.util.List.class));
    }

    @Then("the response should contain pagination information")
    public void verifyPagination() {
        // Spring Data REST usually returns 'page' metadata or 'content'
        SerenityRest.then().body("content", Matchers.notNullValue());
        SerenityRest.then().body("pageable", Matchers.notNullValue());
    }
}

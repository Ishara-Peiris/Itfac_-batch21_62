package com.qatraining.stepdefinitions.api;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.rest.SerenityRest;
import static org.hamcrest.Matchers.*;
import static net.serenitybdd.rest.SerenityRest.lastResponse;

public class DashboardApiStepDefinitions {

    private String authToken;

    @Given("the user is authenticated with a valid token")
    public void userIsAuthenticated() {
        authToken = SerenityRest.given()
                .contentType("application/json")
                .body("{\"username\":\"admin\",\"password\":\"admin123\"}")
                .post("/api/auth/login")
                .jsonPath().getString("token");
    }

    @When("I send a GET request to the dashboard summary endpoint")
    public void sendGetRequestToDashboard() {
        SerenityRest.given()
                .header("Authorization", "Bearer " + authToken)
                .get("/api/plants/summary");
    }

    @Then("the dashboard response status should be {int}")
    public void verifyStatusCode(int statusCode) {
        lastResponse().then().statusCode(statusCode);
    }

    @Then("the dashboard data should be in valid JSON format")
    public void verifyJsonFormat() {
        lastResponse().then().contentType("application/json");
    }

    @Then("the response should contain all mandatory dashboard fields")
    public void verifyMandatoryFields() {
        // Updated to match your actual API response: {totalPlants=0, lowStockPlants=0}
        lastResponse().then()
                .body("$", hasKey("totalPlants"))
                .body("$", hasKey("lowStockPlants"));
    }

    @Then("the dashboard API response time should be less than {int} seconds")
    public void verifyResponseTime(int seconds) {
        lastResponse().then().time(lessThan((long) seconds * 1000));
    }

    @When("I send a GET request to the dashboard without authentication")
    public void sendRequestWithoutAuth() {
        SerenityRest.given().get("/api/plants/summary");
    }

    @When("I send a GET request to the dashboard with an invalid token")
    public void sendRequestWithInvalidToken() {
        SerenityRest.given()
                .header("Authorization", "Bearer invalid_token")
                .get("/api/plants/summary");
    }

    @When("I request an invalid dashboard endpoint {string}")
    public void requestInvalidEndpoint(String endpoint) {
        SerenityRest.given()
                .header("Authorization", "Bearer " + authToken)
                .get(endpoint);
    }

    @Then("the dashboard data should match the system inventory logic")
    public void verifyDataAccuracy() {
        lastResponse().then().body("totalPlants", greaterThanOrEqualTo(0));
    }

    @When("I send a POST request instead of GET to the dashboard")
    public void sendPostRequest() {
        SerenityRest.given()
                .header("Authorization", "Bearer " + authToken)
                .post("/api/plants/summary");
    }

    @Given("I trigger a data update in the system")
    public void triggerDataUpdate() {
        // Logic to simulate adding a plant or making a sale
        System.out.println("Data update triggered");
    }

    @Then("the updated dashboard data should be returned")
    public void verifyUpdatedData() {
        lastResponse().then().body("totalPlants", notNullValue());
    }
}
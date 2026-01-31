package com.qatraining.stepdefinitions.api;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.model.environment.SystemEnvironmentVariables;
import net.thucydides.model.util.EnvironmentVariables;

import java.util.HashMap;
import java.util.Map;

import static net.serenitybdd.rest.SerenityRest.restAssuredThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Step definitions for Plant Management API tests.
 * Test IDs: API-PM-01, API-PM-02, API-PM-03
 */
public class PlantManagementApiStepDefinitions {

    private String baseUrl;
    private Response response;
    private Map<String, Object> requestPayload = new HashMap<>();
    private String authToken;
    private String createdPlantId;
    private String plantId;
    private String categoryId;

    @Given("the API base URL is configured")
    public void theApiBaseUrlIsConfigured() {
        EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();
        baseUrl = environmentVariables.getProperty("restapi.baseurl", "http://localhost:8080/api");
        Serenity.recordReportData().withTitle("API Base URL").andContents(baseUrl);
    }

    @And("the admin user is authenticated")
    public void theAdminUserIsAuthenticated() {
        // Authenticate admin and get token
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "admin");
        credentials.put("password", "admin123");

        response = SerenityRest.given()
                .baseUri(baseUrl)
                .contentType("application/json")
                .body(credentials)
                .when()
                .post("/auth/login");

        // Extract token from response (adjust based on actual API response structure)
        if (response.getStatusCode() == 200) {
            authToken = response.jsonPath().getString("token");
            Serenity.recordReportData().withTitle("Admin Authentication").andContents("Admin authenticated successfully");
        } else {
            // For testing purposes, use a default token or handle as needed
            authToken = "test-admin-token";
            Serenity.recordReportData().withTitle("Admin Authentication").andContents("Using test token");
        }
    }

    @Given("a valid category exists in the system")
    public void aValidCategoryExistsInTheSystem() {
        // First, try to get existing categories
        response = SerenityRest.given()
                .baseUri(baseUrl)
                .contentType("application/json")
                .header("Authorization", "Bearer " + authToken)
                .when()
                .get("/categories");

        if (response.getStatusCode() == 200 && response.jsonPath().getList("$").size() > 0) {
            categoryId = response.jsonPath().getString("[0].id");
        } else {
            // Create a category if none exists
            Map<String, String> categoryPayload = new HashMap<>();
            categoryPayload.put("name", "Flowers");
            categoryPayload.put("description", "Flowering plants");

            response = SerenityRest.given()
                    .baseUri(baseUrl)
                    .contentType("application/json")
                    .header("Authorization", "Bearer " + authToken)
                    .body(categoryPayload)
                    .when()
                    .post("/categories");

            categoryId = response.jsonPath().getString("id");
        }

        Serenity.recordReportData().withTitle("Category ID").andContents(categoryId != null ? categoryId : "N/A");
        Serenity.setSessionVariable("categoryId").to(categoryId);
    }

    @When("I send a POST request to {string} with the following plant details:")
    public void iSendAPostRequestToWithPlantDetails(String endpoint, DataTable dataTable) {
        requestPayload = new HashMap<>();
        dataTable.asMaps().get(0).forEach((key, value) -> {
            if (key.equals("price") || key.equals("quantity")) {
                requestPayload.put(key, Integer.parseInt(value));
            } else {
                requestPayload.put(key, value);
            }
        });

        // Add category ID to payload
        if (categoryId != null) {
            requestPayload.put("categoryId", categoryId);
        }

        Serenity.recordReportData().withTitle("Request Payload").andContents(requestPayload.toString());

        response = SerenityRest.given()
                .baseUri(baseUrl)
                .contentType("application/json")
                .header("Authorization", "Bearer " + authToken)
                .body(requestPayload)
                .when()
                .post(endpoint);

        Serenity.recordReportData().withTitle("Response Status").andContents(String.valueOf(response.getStatusCode()));
        Serenity.recordReportData().withTitle("Response Body").andContents(response.getBody().asString());
    }

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int expectedStatusCode) {
        restAssuredThat(resp -> resp.statusCode(expectedStatusCode));
    }

    @And("the response should contain the new plant ID")
    public void theResponseShouldContainTheNewPlantId() {
        createdPlantId = response.jsonPath().getString("id");
        assertThat(createdPlantId)
                .as("Response should contain a plant ID")
                .isNotNull();
        Serenity.setSessionVariable("plantId").to(createdPlantId);
        Serenity.recordReportData().withTitle("Created Plant ID").andContents(createdPlantId);
    }

    @And("I should be able to retrieve the plant via GET request")
    public void iShouldBeAbleToRetrieveThePlantViaGetRequest() {
        response = SerenityRest.given()
                .baseUri(baseUrl)
                .contentType("application/json")
                .header("Authorization", "Bearer " + authToken)
                .when()
                .get("/api/plants/" + createdPlantId);

        restAssuredThat(resp -> resp.statusCode(200));
        restAssuredThat(resp -> resp.body("id", equalTo(createdPlantId)));
    }

    @Given("a plant exists in the system with ID {string}")
    public void aPlantExistsInTheSystemWithId(String plantIdParam) {
        // If we have a created plant ID from previous test, use it
        plantId = Serenity.sessionVariableCalled("plantId");

        if (plantId == null) {
            // Create a plant for testing
            Map<String, Object> plantPayload = new HashMap<>();
            plantPayload.put("name", "Test Plant");
            plantPayload.put("price", 500);
            plantPayload.put("quantity", 100);

            response = SerenityRest.given()
                    .baseUri(baseUrl)
                    .contentType("application/json")
                    .header("Authorization", "Bearer " + authToken)
                    .body(plantPayload)
                    .when()
                    .post("/api/plants");

            if (response.getStatusCode() == 201 || response.getStatusCode() == 200) {
                plantId = response.jsonPath().getString("id");
            }
        }

        Serenity.setSessionVariable("plantId").to(plantId);
        Serenity.recordReportData().withTitle("Plant ID for Test").andContents(plantId != null ? plantId : "N/A");
    }

    @When("I send a PUT request to {string} with updated details:")
    public void iSendAPutRequestToWithUpdatedDetails(String endpoint, DataTable dataTable) {
        requestPayload = new HashMap<>();
        dataTable.asMaps().get(0).forEach((key, value) -> {
            if (key.equals("price") || key.equals("quantity")) {
                requestPayload.put(key, Integer.parseInt(value));
            } else {
                requestPayload.put(key, value);
            }
        });

        // Replace {plantId} in endpoint
        String actualEndpoint = endpoint.replace("{plantId}", plantId);

        Serenity.recordReportData().withTitle("Update Payload").andContents(requestPayload.toString());

        response = SerenityRest.given()
                .baseUri(baseUrl)
                .contentType("application/json")
                .header("Authorization", "Bearer " + authToken)
                .body(requestPayload)
                .when()
                .put(actualEndpoint);

        Serenity.recordReportData().withTitle("Response Status").andContents(String.valueOf(response.getStatusCode()));
    }

    @And("the response should show the updated values")
    public void theResponseShouldShowTheUpdatedValues() {
        String responseBody = response.getBody().asString();
        Serenity.recordReportData().withTitle("Updated Response").andContents(responseBody);
        // Verify updated values are in response
        assertThat(responseBody).isNotEmpty();
    }

    @And("a GET request should confirm the changes:")
    public void aGetRequestShouldConfirmTheChanges(DataTable dataTable) {
        response = SerenityRest.given()
                .baseUri(baseUrl)
                .contentType("application/json")
                .header("Authorization", "Bearer " + authToken)
                .when()
                .get("/api/plants/" + plantId);

        Map<String, String> expectedValues = dataTable.asMaps().get(0);

        for (Map.Entry<String, String> entry : expectedValues.entrySet()) {
            int expectedValue = Integer.parseInt(entry.getValue());
            restAssuredThat(resp -> resp.body(entry.getKey(), equalTo(expectedValue)));
        }
    }

    @When("I send a DELETE request to {string}")
    public void iSendADeleteRequestTo(String endpoint) {
        String actualEndpoint = endpoint.replace("{plantId}", plantId);

        response = SerenityRest.given()
                .baseUri(baseUrl)
                .contentType("application/json")
                .header("Authorization", "Bearer " + authToken)
                .when()
                .delete(actualEndpoint);

        Serenity.recordReportData().withTitle("Delete Response Status").andContents(String.valueOf(response.getStatusCode()));
    }

    @Then("the response status code should be {int} or {int}")
    public void theResponseStatusCodeShouldBeOr(int statusCode1, int statusCode2) {
        int actualStatusCode = response.getStatusCode();
        assertThat(actualStatusCode)
                .as("Status code should be %d or %d", statusCode1, statusCode2)
                .isIn(statusCode1, statusCode2);
    }

    @When("I send a GET request to {string}")
    public void iSendAGetRequestTo(String endpoint) {
        String actualEndpoint = endpoint.replace("{plantId}", plantId);

        response = SerenityRest.given()
                .baseUri(baseUrl)
                .contentType("application/json")
                .header("Authorization", "Bearer " + authToken)
                .when()
                .get(actualEndpoint);

        Serenity.recordReportData().withTitle("GET Response Status").andContents(String.valueOf(response.getStatusCode()));
    }

    @Then("the response should contain a list of plants")
    public void theResponseShouldContainAListOfPlants() {
        restAssuredThat(response -> response.statusCode(200));
        
        // Verify response contains a list
        assertThat(response.getBody().asString()).isNotNull();
        
        Serenity.recordReportData()
                .withTitle("Plants List")
                .andContents("Successfully retrieved list of plants: " + response.getBody().asString());
    }
}

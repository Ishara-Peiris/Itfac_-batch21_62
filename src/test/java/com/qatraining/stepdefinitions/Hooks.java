package com.qatraining.stepdefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.rest.SerenityRest;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.qatraining.hooks.AuthenticationManager;
import java.util.List;

/**
 * Common hooks for Plant Management and Dashboard Cucumber scenarios.
 * These hooks run before and after each scenario.
 */
public class Hooks {

    private static final Logger LOGGER = LoggerFactory.getLogger(Hooks.class);

    @Before
    public void beforeScenario(Scenario scenario) {
        LOGGER.info("Starting scenario: {}", scenario.getName());
        LOGGER.info("Tags: {}", scenario.getSourceTagNames());
        Serenity.recordReportData().withTitle("Test Scenario").andContents(scenario.getName());
    }

    @After(order = 100)
    public void afterScenario(Scenario scenario) {
        LOGGER.info("Finished scenario: {} - Status: {}", scenario.getName(), scenario.getStatus());

        if (scenario.isFailed()) {
            LOGGER.error("Scenario failed: {}", scenario.getName());
            Serenity.recordReportData()
                    .withTitle("Failure Details")
                    .andContents("Scenario '" + scenario.getName() + "' failed");
        }
    }

    @Before("@api")
    public void beforeApiScenario(Scenario scenario) {
        LOGGER.info("Setting up API scenario: {}", scenario.getName());
    }

    @Before("@ui")
    public void beforeUiScenario(Scenario scenario) {
        LOGGER.info("Setting up UI scenario: {}", scenario.getName());
    }

    @After("@ui")
    public void afterUiScenario(Scenario scenario) {
        // Clean up test plants after UI scenarios to maintain a clean dashboard
        try {
            cleanupTestPlants();
        } catch (Exception e) {
            LOGGER.error("Error during cleanupTestPlants: {}", e.getMessage());
        }

        if (scenario.isFailed()) {
            LOGGER.info("UI Scenario failed - Check Serenity reports for screenshots");
        }
    }

    /**
     * Authenticate as admin and delete test-created plants.
     * Updated with Null-Safety to prevent the 'getObjectMapperConfig' error.
     */
    private void cleanupTestPlants() {
        String baseUrl = AuthenticationManager.getBaseUrl();
        LOGGER.info("Running cleanupTestPlants against {}", baseUrl);

        try {
            String token = AuthenticationManager.getAdminToken();

            if (token == null || token.isEmpty()) {
                LOGGER.warn("No token available for cleanup; skipping cleanup");
                return;
            }

            String authHeader = "Bearer " + token;

            // Use a raw Response object first to check for nulls (fixes your NPE)
            Response response = SerenityRest.given()
                    .header("Authorization", authHeader)
                    .get(baseUrl + "/api/plants");

            if (response == null || response.getStatusCode() != 200) {
                LOGGER.warn("Could not fetch plants list for cleanup. Status: {}",
                        response != null ? response.getStatusCode() : "NULL");
                return;
            }

            // Extract IDs safely
            List<Integer> idsToDelete = response.jsonPath()
                    .getList("findAll { it.name == 'Orchid' || it.name ==~ /(?i).*test.*/ }.id", Integer.class);

            if (idsToDelete == null || idsToDelete.isEmpty()) {
                LOGGER.info("No test plants found to delete");
                return;
            }

            for (Integer id : idsToDelete) {
                try {
                    SerenityRest.given()
                            .header("Authorization", authHeader)
                            .delete(baseUrl + "/api/plants/" + id)
                            .then()
                            .statusCode(204);
                    LOGGER.info("Deleted plant id={}", id);
                } catch (Exception e) {
                    LOGGER.warn("Failed to delete plant id={} : {}", id, e.getMessage());
                }
            }
        } catch (Exception e) {
            LOGGER.error("Exception in cleanupTestPlants: {}", e.getMessage());
        }
    }
}
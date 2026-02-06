package com.qatraining.stepdefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.rest.SerenityRest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.qatraining.hooks.Authenticationmanager;

/**
 * Common hooks for Plant Management Cucumber scenarios.
 * These hooks run before and after each scenario.
 */
public class Hook {

    private static final Logger LOGGER = LoggerFactory.getLogger(Hook.class);

    @Before
    public void beforeScenario(Scenario scenario) {
        LOGGER.info("Starting scenario: {}", scenario.getName());
        LOGGER.info("Tags: {}", scenario.getSourceTagNames());
        Serenity.recordReportData().withTitle("Test Scenario").andContents(scenario.getName());
    }

    @After
    public void afterScenario(Scenario scenario) {
        LOGGER.info("Finished scenario: {} - Status: {}", scenario.getName(), scenario.getStatus());
        
        if (scenario.isFailed()) {
            LOGGER.error("Scenario failed: {}", scenario.getName());
            Serenity.recordReportData()
                    .withTitle("Failure Details")
                    .andContents("Scenario '" + scenario.getName() + "' failed");
        }
        
        // Clean up test plants after UI scenarios
        if (scenario.getSourceTagNames().stream().anyMatch(t -> t.equalsIgnoreCase("@ui"))) {
            try {
                cleanupTestPlants();
            } catch (Exception e) {
                LOGGER.error("Error during cleanupTestPlants: {}", e.getMessage());
            }
        }
    }

    @Before("@api")
    public void beforeApiScenario(Scenario scenario) {
        LOGGER.info("Setting up API scenario: {}", scenario.getName());
        // Token will be obtained lazily when needed via AuthenticationManager
    }

    @Before("@ui")
    public void beforeUiScenario(Scenario scenario) {
        LOGGER.info("Setting up UI scenario: {}", scenario.getName());
    }

    @After("@ui")
    public void afterUiScenario(Scenario scenario) {
        if (scenario.isFailed()) {
            LOGGER.info("Taking screenshot for failed UI scenario");
        }
    }

    /**
     * Authenticate as admin, list plants and delete test-created plants by name.
     * This swallows errors and only logs results to avoid making cleanup failures fail tests.
     * Uses cached token from AuthenticationManager for efficiency.
     */
    private void cleanupTestPlants() {
        String baseUrl = Authenticationmanager.getBaseUrl();
        LOGGER.info("Running cleanupTestPlants against {}", baseUrl);

        try {
            // Get cached or newly obtained token
            String token = Authenticationmanager.getAdminToken();
            
            if (token == null || token.isEmpty()) {
                LOGGER.warn("No token available for cleanup; skipping cleanup");
                return;
            }

            String authHeader = "Bearer " + token;

            // List plants and find ones created by tests
            java.util.List<Integer> idsToDelete = SerenityRest.given()
                    .header("Authorization", authHeader)
                    .get(baseUrl + "/api/plants")
                    .then()
                    .statusCode(200)
                    .extract()
                    .jsonPath()
                    .getList("findAll { it.name == 'Orchid' || it.name ==~ /(?i).*test.*/ }.id", Integer.class);

            if (idsToDelete == null || idsToDelete.isEmpty()) {
                LOGGER.info("No test plants found to delete");
                return;
            }

            // Delete each plant by id
            for (Integer id : idsToDelete) {
                try {
                    SerenityRest.given()
                            .header("Authorization", authHeader)
                            .delete(baseUrl + "/api/plants/" + id)
                            .then()
                            .statusCode(204);  // 204 No Content is success for DELETE
                    LOGGER.info("Deleted plant id={}", id);
                } catch (Exception e) {
                    LOGGER.warn("Failed to delete plant id={} : {}", id, e.getMessage());
                }
            }
        } catch (Exception e) {
            LOGGER.error("Exception in cleanupTestPlants: {}", e.getMessage(), e);
        }
    }
}

package com.qatraining.stepdefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import net.serenitybdd.core.Serenity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Common hooks for Plant Management Cucumber scenarios.
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

    @After
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
        if (scenario.isFailed()) {
            LOGGER.info("Taking screenshot for failed UI scenario");
        }
    }
}

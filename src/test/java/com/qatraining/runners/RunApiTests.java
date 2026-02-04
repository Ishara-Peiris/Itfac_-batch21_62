package com.qatraining.runners;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.*;

/**
 * Test Runner for Plant Management API tests only.
 * Runs feature files tagged with @api and @plant-management.
 * Test IDs: API-PM-01, API-PM-02, API-PM-03
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features/api")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "io.cucumber.core.plugin.SerenityReporterParallel,pretty")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.qatraining.stepdefinitions")
@ConfigurationParameter(key = FILTER_TAGS_PROPERTY_NAME, value = "@api and @plant-management and not @ignore")
public class RunApiTests {
}

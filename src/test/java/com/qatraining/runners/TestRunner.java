package com.qatraining.runners;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.*;

/**
 * Test Runner for all Cucumber tests (UI and API).
 * Supports tag filtering via -Dcucumber.filter.tags system property.
 * Default: Runs all feature files except those tagged with @ignore.
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "io.cucumber.core.plugin.SerenityReporterParallel,pretty")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.qatraining.stepdefinitions")
public class TestRunner {
    static {
        // Set default tag filter if not provided via command line
        String tagsProperty = System.getProperty("cucumber.filter.tags");
        if (tagsProperty == null || tagsProperty.isEmpty()) {
            System.setProperty("cucumber.filter.tags", "not @ignore");
        }
    }
}

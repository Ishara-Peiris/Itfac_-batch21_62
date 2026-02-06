package com.qatraining.runners;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.*;



/**
 * Runner that executes only Sales API scenarios.
 * Requires feature scenarios tagged with @api and @sales-management.
 */
@Suite
@IncludeEngines("cucumber")
// point directly to sales features (optional). Use "features/api" if you prefer tag filtering only.
@SelectClasspathResource("features/api")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "io.cucumber.core.plugin.SerenityReporterParallel,pretty")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.qatraining.stepdefinitions.api")
@ConfigurationParameter(key = FILTER_TAGS_PROPERTY_NAME, value = "@api and @sales-management and not @ignore")
public class RunApiSalesTests {
}

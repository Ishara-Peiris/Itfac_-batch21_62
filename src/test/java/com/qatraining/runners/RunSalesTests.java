package com.qatraining.runners;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.*;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features/ui")
@ConfigurationParameter(
        key = PLUGIN_PROPERTY_NAME,
        value = "io.cucumber.core.plugin.SerenityReporterParallel,pretty"
)
@ConfigurationParameter(
        key = GLUE_PROPERTY_NAME,
        value = "com.qatraining.stepdefinitions"
)
@ConfigurationParameter(
        key = FILTER_TAGS_PROPERTY_NAME,
        value = "@mysales"
)
public class RunSalesTests {
}

package com.qatraining.runners;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        plugin = {"pretty"},
        features = "src/test/resources/features/ui",
        glue = "com.qatraining.stepdefinitions",
        tags = "@dashboard"
)
public class RunDashboardUiTests {
}

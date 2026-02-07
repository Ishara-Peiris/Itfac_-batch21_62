package com.qatraining.runners;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        plugin = {"pretty", "html:target/cucumber-reports.html"},
        features = "src/test/resources/features/api/dashboard_api.feature",
        glue = "com.qatraining.stepdefinitions.api",
        tags = "@DashboardAPI"
)
public class RunDashboardApiTests {}
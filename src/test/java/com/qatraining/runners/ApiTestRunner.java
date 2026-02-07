package com.qatraining.runners;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        plugin = {"pretty"},
        features = "src/test/resources/features/api/CategoryAPI.feature",
        glue = "com.qatraining.stepdefinitions.api",
        tags = "@CategoryApi"
)
public class ApiTestRunner {}
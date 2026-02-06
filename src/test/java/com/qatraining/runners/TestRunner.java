package com.qatraining.runners;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        plugin = {"pretty"},
        features = "src/test/resources/features", // Runs all features in this folder
        glue = "com.qatraining.stepdefinitions"   // Scans all step definitions
)
public class TestRunner {
}
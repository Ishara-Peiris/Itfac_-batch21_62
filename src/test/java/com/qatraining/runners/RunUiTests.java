package com.qatraining.runners;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        plugin = {"pretty"},
        features = "src/test/resources/features/ui", // Points specifically to UI features
        glue = "com.qatraining.stepdefinitions.ui",  // Scans only UI steps (optional optimization)
        tags = "not @ignore"                         // Skips scenarios tagged with @ignore
)
public class RunUiTests {
}
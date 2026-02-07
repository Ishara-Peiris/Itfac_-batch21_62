package com.qatraining.runners;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = "src/test/resources/features/ui/category_management.feature",
        glue = "com.qatraining.stepdefinitions",
        // This 'or' operator tells Cucumber to run any scenario with either tag
        tags = "@TC-UI-01 or @TC-UI-02   or @TC-UI-03 or @TC-UI-04 or @TC-UI-05 or @TC-UI-06 or @TC-UI-07 or @TC-UI-08 or @TC-UI-09 or @TC-UI-10 ",
        plugin = {"pretty"}
)
public class SingleTestRunner {}


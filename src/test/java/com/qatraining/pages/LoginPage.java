package com.qatraining.pages;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.support.FindBy;
import java.time.Duration;

public class LoginPage extends PageObject {

    @FindBy(name = "username")
    WebElementFacade usernameField;

    @FindBy(name = "password")
    WebElementFacade passwordField;

    @FindBy(xpath = "//button[@type='submit']")
    WebElementFacade loginButton;

    // -----------------------------------------------------------
    // EXISTING METHOD (Used by Step Definitions)
    // -----------------------------------------------------------
    public void doLogin(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
        
        // CRITICAL FIX: Wait for the URL to change to 'dashboard' 
        // This ensures we are logged in before the next step runs.
        // We catch errors in case the app redirects somewhere else, 
        // but this wait usually solves the "not logged in" issue.
        try {
            getDriver().manage().timeouts().implicitlyWait(Duration.ofMillis(500)); 
            waitFor(webDriver -> webDriver.getCurrentUrl().contains("dashboard"));
        } catch (Exception e) {
            // Ignore timeout, the next step will handle failure if we aren't logged in
        }
    }

    // -----------------------------------------------------------
    // HELPER METHODS
    // -----------------------------------------------------------
    public void enterUsername(String username) {
        usernameField.waitUntilVisible().type(username);
    }

    public void enterPassword(String password) {
        passwordField.waitUntilVisible().type(password);
    }

    public void clickLoginButton() {
        loginButton.waitUntilClickable().click();
    }

    public void navigateTo(String path) {
        try {
            String baseUrl = getDriver().getCurrentUrl().split("/ui/")[0];
            getDriver().navigate().to(baseUrl + path);
            // Wait for page to load
            Thread.sleep(500);
        } catch (Exception e) {
            // Continue if sleep fails
        }
    }
}
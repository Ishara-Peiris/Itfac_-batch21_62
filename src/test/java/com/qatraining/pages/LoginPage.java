package com.qatraining.pages;

import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.annotations.DefaultUrl;
import org.openqa.selenium.support.FindBy;

import java.time.Duration;

/**
 * Page Object for the Login page.
 * Contains all web elements and actions related to the login functionality.
 */
@DefaultUrl("/ui/login")
public class LoginPage extends BasePage {

    @FindBy(name = "username")
    private WebElementFacade usernameField;

    @FindBy(name = "password")
    private WebElementFacade passwordField;

    @FindBy(css = "button[type='submit']")
    private WebElementFacade loginButton;

    @FindBy(css = ".alert.alert-danger")
    private WebElementFacade errorMessage;

    @FindBy(css = ".invalid-feedback")
    private WebElementFacade validationError;

    /**
     * Enter username in the username field.
     * @param username the username to enter
     */
    public void enterUsername(String username) {
        usernameField.waitUntilVisible();
        usernameField.clear();
        usernameField.type(username);
    }

    /**
     * Enter password in the password field.
     * @param password the password to enter
     */
    public void enterPassword(String password) {
        passwordField.waitUntilVisible();
        passwordField.clear();
        passwordField.type(password);
    }

    /**
     * Click the login button.
     */
    public void clickLoginButton() {
        System.out.println("Login button visible: " + loginButton.isVisible());
        System.out.println("Login button enabled: " + loginButton.isEnabled());
        loginButton.waitUntilClickable().click();
    }

    /**
     * Perform login with username and password.
     * @param username the username
     * @param password the password
     */
    public void loginWith(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
    }

    /**
     * Get the error message text.
     * @return the error message text
     */
    public String getErrorMessage() {
        if (errorMessage.isVisible()) {
            return errorMessage.getText();
        }
        return "";
    }

    /**
     * Check if error message is displayed.
     * @return true if error message is visible
     */
    public boolean hasErrorMessage() {
        return errorMessage.isVisible();
    }

    /**
     * Check if validation errors are displayed.
     * @return true if validation errors are visible
     */
    public boolean hasValidationErrors() {
        return validationError.isVisible();
    }

    /**
     * Check if login page is displayed.
     * @return true if login page is displayed
     */
    public boolean isDisplayed() {
        return usernameField.isVisible() && passwordField.isVisible();
    }

    public void clickLogin() {
    }
}

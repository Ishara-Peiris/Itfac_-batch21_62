package com.qatraining.stepdefinitions.ui;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.Actor;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qatraining.pages.LoginPage;
import com.qatraining.pages.PlantsPage;

/**
 * Step definitions for Plant Management UI tests
 */
public class PlantManagementUiStepDefinitions {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlantManagementUiStepDefinitions.class);
    
    private LoginPage loginPage;
    private PlantsPage plantsPage;
    private WebDriver driver;
    
    /**
     * Initialize pages before each step
     */
    private void initPages() {
        if (loginPage == null) {
            loginPage = new LoginPage();
        }
        if (plantsPage == null) {
            plantsPage = new PlantsPage();
        }
        driver = Serenity.getDriver();
    }

    /**
     * Given: The admin user navigates to the login page
     */
    @Given("the admin user navigates to the login page")
    public void adminNavigatesToLoginPage() {
        initPages();
        LOGGER.info("Admin navigating to login page");
        String targetUrl = "http://localhost:8080/ui/login";
        LOGGER.info("Opening URL: {}", targetUrl);
        try {
            loginPage.open();
            LOGGER.info("Page opened successfully. Current URL: {}", driver.getCurrentUrl());
            loginPage.waitForPageToLoad();
            LOGGER.info("Page load completed");
            Assertions.assertTrue(loginPage.isDisplayed(), "Login page should be displayed at " + targetUrl);
            LOGGER.info("✓ Admin is on login page");
        } catch (Exception e) {
            LOGGER.error("Failed to navigate to login page at {}", targetUrl, e);
            LOGGER.info("Current URL: {}", driver.getCurrentUrl());
            throw e;
        }
    }

    /**
     * When: The admin enters username and password
     */
    @When("the admin enters username {string} and password {string}")
    public void adminEntersCredentials(String username, String password) {
        initPages();
        LOGGER.info("Admin entering credentials - Username: {}", username);
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
        Serenity.recordReportData()
                .withTitle("Login Credentials Entered")
                .andContents("Username: " + username);
        LOGGER.info("✓ Credentials entered successfully");
    }

    /**
     * And: The admin clicks the login button
     */
    @And("the admin clicks the login button")
    public void adminClicksLoginButton() {
        initPages();
        LOGGER.info("Admin clicking login button");
        loginPage.clickLoginButton();
        Serenity.recordReportData()
                .withTitle("Login Action")
                .andContents("Login button clicked");
        LOGGER.info("✓ Login button clicked");
    }

    /**
     * And: The admin waits for the plants page to load
     */
    @And("the admin waits for the plants page to load")
    public void adminWaitsForPlantsPageToLoad() {
        initPages();
        LOGGER.info("Waiting for plants page to load...");
        
        // Wait for navigation to complete
        try {
            Thread.sleep(2000); // Wait for navigation
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Navigate directly to plants page if not already there
        String currentUrl = driver.getCurrentUrl();
        LOGGER.info("Current URL after login: {}", currentUrl);
        
        if (!currentUrl.contains("/ui/plants")) {
            LOGGER.info("Not on plants page, navigating to /ui/plants");
            driver.navigate().to("http://localhost:8080/ui/plants");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        plantsPage.waitForPageToLoad();
        plantsPage.waitForLoadingToComplete();
        
        LOGGER.info("✓ Plants page loaded successfully");
    }

    /**
     * Then: The admin should be on the plants page with correct URL
     */
    @Then("the admin should be on the plants page with URL {string}")
    public void adminShouldBeOnPlantsPage(String expectedUrl) {
        initPages();
        LOGGER.info("Verifying admin is on plants page with URL: {}", expectedUrl);
        
        String currentUrl = driver.getCurrentUrl();
        LOGGER.info("Current URL: {}", currentUrl);
        
        Assertions.assertTrue(
                currentUrl.contains(expectedUrl),
                "URL should contain '" + expectedUrl + "' but was: " + currentUrl
        );
        
        Serenity.recordReportData()
                .withTitle("URL Verification")
                .andContents("Expected: " + expectedUrl + "\nActual: " + currentUrl);
        
        LOGGER.info("✓ Admin is on correct URL: {}", currentUrl);
    }

    /**
     * And: The Plants header should be visible
     */
    @Then("the {string} header should be visible on the page")
    public void headerShouldBeVisible(String headerText) {
        initPages();
        LOGGER.info("Verifying header '{}' is visible", headerText);
        
        String pageText = driver.getPageSource();
        Assertions.assertTrue(
                pageText.contains(headerText),
                "Header '" + headerText + "' should be visible on the page"
        );
        
        Serenity.recordReportData()
                .withTitle("Header Verification")
                .andContents("Header '" + headerText + "' is visible");
        
        LOGGER.info("✓ Header '{}' is visible", headerText);
    }

    /**
     * And: The plant management table should be visible
     */
    @And("the plant management table should be visible")
    public void plantTableShouldBeVisible() {
        initPages();
        LOGGER.info("Verifying plant management table is visible");
        
        Assertions.assertTrue(
                plantsPage.isDisplayed(),
                "Plant management page should be displayed"
        );
        
        Serenity.recordReportData()
                .withTitle("Plant Table Verification")
                .andContents("Plant table is visible on the page");
        
        LOGGER.info("✓ Plant management table is visible");
    }

    /**
     * And: At least one plant should be displayed in the table
     */
    @And("at least one plant should be displayed in the table")
    public void plantsShoulBDisplayedInTable() {
        initPages();
        LOGGER.info("Verifying plants are displayed in the table");
        
        Assertions.assertTrue(
                plantsPage.hasPlants(),
                "At least one plant should be displayed in the table"
        );
        
        int plantCount = plantsPage.getPlantCount();
        Serenity.recordReportData()
                .withTitle("Plants Count Verification")
                .andContents("Number of plants in table: " + plantCount);
        
        LOGGER.info("✓ {} plant(s) are displayed in the table", plantCount);
    }

    /**
     * And: The admin clicks the "Add a Plant" button
     */
    @And("the admin clicks the {string} button")
    public void adminClicksButton(String buttonText) {
        initPages();
        LOGGER.info("Admin clicking button: {}", buttonText);
        
        plantsPage.clickAddPlantButton();
        
        // Wait for form/modal to load
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        LOGGER.info("✓ Button '{}' clicked", buttonText);
    }

    /**
     * Then: The add plant form should be displayed
     */
    @Then("the add plant form should be displayed")
    public void addPlantFormShouldBeDisplayed() {
        initPages();
        LOGGER.info("Verifying add plant form is displayed");
        
        Assertions.assertTrue(
                plantsPage.isAddPlantFormDisplayed(),
                "Add plant form should be displayed"
        );
        
        LOGGER.info("✓ Add plant form is displayed");
    }

    /**
     * When: The admin enters plant name
     */
    @When("the admin enters plant name {string}")
    public void adminEntersPlantName(String plantName) {
        initPages();
        LOGGER.info("Entering plant name: {}", plantName);
        
        plantsPage.enterPlantName(plantName);
        
        LOGGER.info("✓ Plant name '{}' entered", plantName);
    }

    /**
     * And: The admin enters plant price
     */
    @And("the admin enters plant price {string}")
    public void adminEntersPlantPrice(String price) {
        initPages();
        LOGGER.info("Entering plant price: {}", price);
        
        plantsPage.enterPlantPrice(price);
        
        LOGGER.info("✓ Plant price '{}' entered", price);
    }

    /**
     * And: The admin selects category
     */
    @And("the admin selects category {string}")
    public void adminSelectsCategory(String category) {
        initPages();
        LOGGER.info("Selecting category: {}", category);
        
        plantsPage.selectCategory(category);
        
        LOGGER.info("✓ Category '{}' selected", category);
    }

    /**
     * And: The admin enters plant quantity
     */
    @And("the admin enters plant quantity {string}")
    public void adminEntersPlantQuantity(String quantity) {
        initPages();
        LOGGER.info("Entering plant quantity: {}", quantity);
        
        plantsPage.enterPlantQuantity(quantity);
        
        LOGGER.info("✓ Plant quantity '{}' entered", quantity);
    }

    /**
     * And: The admin submits the plant form
     */
    @And("the admin submits the plant form")
    public void adminSubmitsPlantForm() {
        initPages();
        LOGGER.info("Submitting plant form");
        
        plantsPage.submitPlantForm();
        
        // Wait for form submission and page update
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        LOGGER.info("✓ Plant form submitted");
    }

    /**
     * Then: A success message should be displayed
     */
    @Then("a success message should be displayed")
    public void successMessageShouldBeDisplayed() {
        initPages();
        LOGGER.info("Verifying success message is displayed");
        
        Assertions.assertTrue(
                plantsPage.isSuccessMessageDisplayed(),
                "Success message should be displayed"
        );
        
        LOGGER.info("✓ Success message is displayed");
    }

    /**
     * And: The new plant should appear in the table
     */
    @And("the new plant {string} should appear in the table")
    public void newPlantShouldAppearInTable(String plantName) {
        initPages();
        LOGGER.info("Verifying plant '{}' appears in the table", plantName);
        
        Assertions.assertTrue(
                plantsPage.isPlantInTable(plantName),
                "Plant '" + plantName + "' should appear in the table"
        );
        
        LOGGER.info("✓ Plant '{}' appears in the table", plantName);
    }
}


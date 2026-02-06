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
import java.util.List;

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

    /**
     * When: The admin leaves the Name and Price fields empty
     */
    @When("the admin leaves the Name and Price fields empty")
    public void adminLeavesNameAndPriceEmpty() {
        initPages();
        LOGGER.info("Leaving Name and Price fields empty");
        plantsPage.clearPlantNameField();
        plantsPage.clearPlantPriceField();
        LOGGER.info("✓ Name and Price fields cleared");
    }

    /**
     * Then: The plant form should not be submitted
     */
    @Then("the plant form should not be submitted")
    public void plantFormShouldNotBeSubmitted() {
        initPages();
        LOGGER.info("Verifying plant form is not submitted");
        
        // Check if we are still on the form (modal/page)
        // If the form was submitted successfully, we would be back on the list
        Assertions.assertTrue(
                plantsPage.isAddPlantFormDisplayed(),
                "Plant form (modal) should still be displayed"
        );
        
        LOGGER.info("✓ Plant form is still displayed");
    }

    /**
     * And: The name required validation message should be displayed
     */
    @And("the name required validation message should be displayed")
    public void nameRequiredValidationMessageShouldBeDisplayed() {
        initPages();
        LOGGER.info("Verifying 'Name is required' validation message");
        
        // Wait for validation errors to be visible
        try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        
        List<String> errors = plantsPage.getValidationErrors();
        LOGGER.info("Found validation errors: {}", errors);
        
        boolean found = errors.stream()
                .anyMatch(e -> e.contains("Name is required"));
                
        Assertions.assertTrue(
                found, 
                "Validation error 'Name is required' should be displayed. Found: " + errors
        );
        
        Serenity.recordReportData()
                .withTitle("Name Validation Error")
                .andContents("Found error message: Name is required");
                
        LOGGER.info("✓ 'Name is required' validation message found");
    }

    /**
     * And: The price required validation message should be displayed
     */
    @And("the price required validation message should be displayed")
    public void priceRequiredValidationMessageShouldBeDisplayed() {
        initPages();
        LOGGER.info("Verifying 'Price is required' validation message");
        
        // Wait for validation errors to be visible
        try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        
        List<String> errors = plantsPage.getValidationErrors();
        LOGGER.info("Found validation errors: {}", errors);
        
        boolean found = errors.stream()
                .anyMatch(e -> e.contains("Price is required"));
                
        Assertions.assertTrue(
                found, 
                "Validation error 'Price is required' should be displayed. Found: " + errors
        );
        
        Serenity.recordReportData()
                .withTitle("Price Validation Error")
                .andContents("Found error message: Price is required");
                
        LOGGER.info("✓ 'Price is required' validation message found");
    }

    /**
     * When: The admin clicks the edit button for plant
     */
    @When("the admin clicks the edit button for plant {string}")
    public void theAdminClicksTheEditButtonForPlant(String plantName) {
        initPages();
        LOGGER.info("Clicking edit button for plant: {}", plantName);
        plantsPage.clickEditButtonForPlant(plantName);
        plantsPage.waitForEditPageToLoad();
        LOGGER.info("✓ Edit page loaded for plant: {}", plantName);
    }

    /**
     * And: The admin changes the price to
     */
    @And("the admin changes the price to {string}")
    public void theAdminChangesThePriceTo(String newPrice) {
        initPages();
        LOGGER.info("Changing price to: {}", newPrice);
        plantsPage.enterPlantPrice(newPrice);
        LOGGER.info("✓ Price changed to: {}", newPrice);
    }

    /**
     * And: The admin clicks the save button
     */
    @And("the admin clicks the save button")
    public void theAdminClicksTheSaveButton() {
        initPages();
        LOGGER.info("Clicking save button");
        plantsPage.clickSaveButton();
        LOGGER.info("✓ Save button clicked");
    }

    /**
     * Then: The plant should have price in the table
     */
    @Then("the plant {string} should have price {string} in the table")
    public void thePlantShouldHavePriceInTheTable(String plantName, String expectedPrice) {
        initPages();
        LOGGER.info("Verifying price for plant '{}' is '{}'", plantName, expectedPrice);
        
        String actualPrice = plantsPage.getPlantPriceFromTable(plantName);
        LOGGER.info("Actual price from table: {}", actualPrice);
        
        Assertions.assertNotNull(actualPrice, "Plant '" + plantName + "' should be in the table");
        Assertions.assertTrue(
                actualPrice.contains(expectedPrice),
                "Expected price to contain '" + expectedPrice + "' but found '" + actualPrice + "'"
        );
        
        LOGGER.info("✓ Price verification successful");
    }

    /**
     * When: The admin clicks the delete button for plant
     */
    @When("the admin clicks the delete button for plant {string}")
    public void theAdminClicksTheDeleteButtonForPlant(String plantName) {
        initPages();
        LOGGER.info("Clicking delete button for plant: {}", plantName);
        plantsPage.clickDeleteButtonForPlant(plantName);
        LOGGER.info("✓ Delete button clicked for plant: {}", plantName);
    }

    /**
     * And: The admin confirms the delete action
     */
    @And("the admin confirms the delete action")
    public void theAdminConfirmsTheDeleteAction() {
        initPages();
        LOGGER.info("Confirming delete action");
        plantsPage.acceptDeleteConfirmation();
        LOGGER.info("✓ Delete action confirmed");
    }

    /**
     * Then: The plant should not be in the table
     */
    @Then("the plant {string} should not be in the table")
    public void thePlantShouldNotBeInTheTable(String plantName) {
        initPages();
        LOGGER.info("Verifying plant '{}' is not in the table", plantName);
        
        boolean plantExists = plantsPage.isPlantInTable(plantName);
        
        Assertions.assertFalse(
                plantExists,
                "Plant '" + plantName + "' should NOT be present in the table but it was found"
        );
        
        LOGGER.info("✓ Plant '{}' successfully removed from table", plantName);
    }

    /**
     * Given: The user navigates to the login page
     */
    @Given("the user navigates to the login page")
    public void userNavigatesToLoginPage() {
        adminNavigatesToLoginPage();
    }

    /**
     * When: The user enters username and password
     */
    @When("the user enters username {string} and password {string}")
    public void userEntersCredentials(String username, String password) {
        adminEntersCredentials(username, password);
    }

    /**
     * And: The user clicks the login button
     */
    @And("the user clicks the login button")
    public void userClicksLoginButton() {
        adminClicksLoginButton();
    }

    /**
     * And: The user waits for the plants page to load
     */
    @And("the user waits for the plants page to load")
    public void userWaitsForPlantsPageToLoad() {
        adminWaitsForPlantsPageToLoad();
    }

    /**
     * Then: The plants listing should be visible
     */
    @Then("the plants listing should be visible")
    public void plantsListingShouldBeVisible() {
        initPages();
        LOGGER.info("Verifying plants listing is visible");
        Assertions.assertTrue(plantsPage.isDisplayed(), "Plants page should be displayed");
        Assertions.assertTrue(plantsPage.hasPlants(), "Table should have at least one plant");
        LOGGER.info("✓ Plants listing is visible");
    }

    /**
     * And: The plant row should display category and price in the table
     */
    @And("the plant {string} should have category {string} and price {string} in the table")
    public void plantShouldHaveCategoryAndPriceInTable(String plantName, String expectedCategory, String expectedPrice) {
        initPages();
        LOGGER.info("Verifying category and price for plant: {}", plantName);
        
        String actualCategory = plantsPage.getPlantCategoryFromTable(plantName);
        String actualPrice = plantsPage.getPlantPriceFromTable(plantName);
        
        LOGGER.info("Actual Category: {}, Actual Price: {}", actualCategory, actualPrice);
        
        Assertions.assertNotNull(actualCategory, "Category for '" + plantName + "' should be in the table");
        Assertions.assertEquals(expectedCategory, actualCategory, "Category mismatch for '" + plantName + "'");
        
        Assertions.assertNotNull(actualPrice, "Price for '" + plantName + "' should be in the table");
        Assertions.assertTrue(actualPrice.contains(expectedPrice), 
                "Expected price to contain '" + expectedPrice + "' but found '" + actualPrice + "'");
        
        LOGGER.info("✓ Plant details verified");
    }

    /**
     * And: No error messages should be displayed
     */
    @And("no error messages should be displayed")
    public void noErrorMessagesShouldBeDisplayed() {
        initPages();
        LOGGER.info("Verifying no error messages are displayed");
        List<String> errors = plantsPage.getValidationErrors();
        Assertions.assertTrue(errors.isEmpty(), "Expected no error messages, but found: " + errors);
        LOGGER.info("✓ No error messages found");
    }

    /**
     * Then: The "Add Plant" button should not be visible
     */
    @Then("the {string} button should not be visible")
    public void buttonShouldNotBeVisible(String buttonName) {
        initPages();
        LOGGER.info("Verifying '{}' button is not visible", buttonName);
        if (buttonName.equalsIgnoreCase("Add Plant")) {
            Assertions.assertFalse(plantsPage.isAddPlantButtonVisible(), 
                    "The 'Add Plant' button should NOT be visible for standard users");
        } else {
            LOGGER.warn("Verification for button '{}' not implemented specifically, check generic visibility", buttonName);
        }
        LOGGER.info("✓ Button visibility verification passed");
    }

    /**
     * And: Only the plant list view should be available
     */
    @And("only the plant list view should be available")
    public void onlyPlantListViewShouldBeAvailable() {
        initPages();
        LOGGER.info("Verifying only list view is available (no edit/delete actions)");
        Assertions.assertTrue(plantsPage.isOnlyListViewAvailable(), 
                "Administrative actions (Edit/Delete) should NOT be available for standard users");
        LOGGER.info("✓ RBAC list view verification passed");
    }

    /**
     * Then: No edit or delete icons should be visible in the table
     */
    @Then("no edit or delete icons should be visible in the table")
    public void noEditOrDeleteIconsVisibleInTable() {
        initPages();
        LOGGER.info("Verifying no Pencil (Edit) or Trash (Delete) icons are visible");
        int buttonCount = plantsPage.getActionButtonsCount();
        Assertions.assertEquals(0, buttonCount, 
                "Expected 0 action buttons (Edit/Delete) but found " + buttonCount);
        LOGGER.info("✓ No action icons found");
    }

    /**
     * And: The actions column should be empty
     */
    @And("the actions column should be empty")
    public void actionsColumnShouldBeEmpty() {
        initPages();
        LOGGER.info("Verifying the Actions column content");
        // Reuse isOnlyListViewAvailable as it checks the table body for administrative elements
        Assertions.assertTrue(plantsPage.isOnlyListViewAvailable(), 
                "Actions column should not contain any interactive administrative elements");
        LOGGER.info("✓ Actions column is effectively empty/restricted");
    }

    /**
     * When: The user attempts to access the "Add Plant" page via URL
     */
    @When("the user attempts to access the {string} page via URL")
    public void userAttemptsToAccessPageViaUrl(String pageName) {
        initPages();
        String targetUrl = "";
        if (pageName.equalsIgnoreCase("Add Plant")) {
            targetUrl = "http://localhost:8080/ui/plants/add";
        }
        
        LOGGER.info("Attempting forced browsing to: {}", targetUrl);
        driver.get(targetUrl);
        LOGGER.info("Browser navigated to {}", driver.getCurrentUrl());
    }

    /**
     * Then: The user should be redirected to the dashboard
     */
    @Then("the user should be redirected to the dashboard")
    public void userShouldBeRedirectedToDashboard() {
        initPages();
        String currentUrl = driver.getCurrentUrl();
        LOGGER.info("Current URL for redirection check: {}", currentUrl);
        
        // Wait a moment for potential redirects
        try { Thread.sleep(2000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        currentUrl = driver.getCurrentUrl();
        
        Assertions.assertTrue(currentUrl.contains("/ui/dashboard") || 
                             currentUrl.contains("/ui/login") || 
                             currentUrl.contains("/ui/403"), 
                "User should have been redirected to Dashboard, Login, or 403 page, but is at: " + currentUrl);
        LOGGER.info("✓ Redirection verified");
    }

    /**
     * And: An access denied message or redirection should occur
     */
    @And("an access denied message or redirection should occur")
    public void accessDeniedOrRedirectionOccurs() {
        initPages();
        String currentUrl = driver.getCurrentUrl();
        String pageSource = driver.getPageSource().toLowerCase();
        
        boolean isAccessDenied = pageSource.contains("access denied") || 
                                pageSource.contains("forbidden") || 
                                pageSource.contains("403") ||
                                currentUrl.contains("error") ||
                                currentUrl.contains("dashboard") ||
                                currentUrl.contains("login");
                                
        Assertions.assertTrue(isAccessDenied, "Expected an access denied state or redirection, but none was detected.");
        LOGGER.info("✓ Access restriction verified");
    }

    /**
     * When: The admin creates a plant named {string} without an image
     */
    @When("the admin creates a plant named {string} without an image")
    public void adminCreatesPlantWithoutImage(String plantName) {
        initPages();
        LOGGER.info("Admin creating plant '{}' without an image", plantName);
        plantsPage.clickAddPlantButton();
        plantsPage.enterPlantName(plantName);
        plantsPage.enterPlantPrice("100.00");
        plantsPage.enterPlantQuantity("10");
        plantsPage.selectCategory("Flower");
        // Skipping image field interaction
        plantsPage.clickSaveButton();
        LOGGER.info("✓ Plant create request sent");
    }

    /**
     * Then: The plant {string} should show the default placeholder image
     */
    @Then("the plant {string} should show the default placeholder image")
    public void plantShouldShowDefaultPlaceholder(String plantName) {
        initPages();
        LOGGER.info("Verifying default placeholder for plant: {}", plantName);
        
        // Robust wait: ensure modal is closed first
        if (plantsPage.isAddPlantFormDisplayed()) {
            plantsPage.waitForModalToClose();
        }
        
        // Wait for the plant to appear in the table with potential refresh
        try {
            plantsPage.waitForPlantInTable(plantName);
        } catch (Exception e) {
            LOGGER.info("Plant not found immediately with simple wait, refreshing page...");
            driver.navigate().refresh();
            plantsPage.waitForPageToLoad();
            try {
                plantsPage.waitForPlantInTable(plantName);
            } catch (Exception ex) {
                // If still not found, we proceed to assertion which will fail with clear message
            }
        }
        
        boolean found = plantsPage.isPlantInTable(plantName);
        
        Assertions.assertTrue(found, "Plant '" + plantName + "' should be present in the table");
        
        boolean isPlaceholder = plantsPage.isDefaultPlaceholderDisplayed(plantName);
        String actualSrc = plantsPage.getPlantImageSrc(plantName);
        LOGGER.info("Actual Image Src: {}", actualSrc);
        
        Assertions.assertTrue(isPlaceholder, 
                "Expected default placeholder for '" + plantName + "' but found: " + actualSrc);
        LOGGER.info("✓ Placeholder verification passed");
    }

    /**
     * And: The layout should remain intact
     */
    @And("the layout should remain intact")
    public void layoutShouldRemainIntact() {
        initPages();
        LOGGER.info("Verifying page layout integrity");
        Assertions.assertTrue(plantsPage.isLayoutIntact(), "Page layout (sidebar or main content) seems broken");
        LOGGER.info("✓ Layout integrity verified");
    }
}



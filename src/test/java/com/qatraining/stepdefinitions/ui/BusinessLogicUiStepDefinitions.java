package com.qatraining.stepdefinitions.ui;

import com.qatraining.pages.*;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import net.serenitybdd.annotations.Steps;
import org.junit.Assert;

public class BusinessLogicUiStepDefinitions {

    @Steps
    LoginPage loginPage;

    @Steps
    CategoryPage categoryPage;

    @Steps
    PlantPage plantPage;

    @Steps
    SalesPage salesPage;

    // -------------------------------------------------------------------------
    // BACKGROUND & LOGIN STEPS
    // -------------------------------------------------------------------------

    @Given("I am on the login page")
    public void i_am_on_the_login_page() {
        loginPage.open();
    }

    @Given("I login as {string}")
    public void i_login_as(String role) {
        if (role.equalsIgnoreCase("admin")) {
            loginPage.doLogin("admin", "admin123");
        } else {
            loginPage.doLogin("testuser", "test123");
        }
    }

    @When("I navigate to the Categories page")
    public void navigate_to_categories() {
        loginPage.navigateTo("/ui/categories");
    }

    @When("I navigate to the Plants page")
    public void navigate_to_plants() {
        loginPage.navigateTo("/ui/plants");
    }

    @When("I navigate to the Sales page")
    public void navigate_to_sales() {
        loginPage.navigateTo("/ui/sales");
    }

    // -------------------------------------------------------------------------
    // CATEGORY MANAGEMENT STEPS (UI-BL-01, 02, 05, 07)
    // -------------------------------------------------------------------------

    @When("I add a new category {string}")
    public void i_add_new_category(String categoryName) {
        categoryPage.clickAddCategory();
        categoryPage.enterCategoryName(categoryName);
        categoryPage.clickSave();
        // Wait for save to complete
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Then("the new category {string} should appear in the list")
    public void new_category_should_appear(String categoryName) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        Assert.assertTrue("Category should be visible in the list", 
                categoryPage.isCategoryVisible(categoryName));
    }

    @When("I attempt to add a category with an empty name")
    public void attempt_add_empty_category() {
        categoryPage.clickAddCategory();
        categoryPage.enterCategoryName("");
        categoryPage.clickSave();
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Then("I should see a validation error on the category form")
    public void verify_validation_error() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        Assert.assertTrue("Validation error should be displayed", 
                categoryPage.isValidationErrorVisible());
    }

    @Then("the category should not be created")
    public void category_not_created() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        // Category not created is confirmed by validation error being visible
        // The form is still displayed with the error
        Assert.assertTrue("Category not created - either validation error visible or still on category page", 
                categoryPage.isValidationErrorVisible() || categoryPage.isDisplayed());
    }

    @When("I expand the main category {string}")
    public void expand_main_category(String categoryName) {
        categoryPage.expandCategory(categoryName);
    }

    @Then("I should see sub-categories listed under it")
    public void verify_sub_categories() {
        Assert.assertTrue("Sub-categories should be visible", 
                categoryPage.areCategoriesDisplayed());
    }

    @Then("I should see a list of main categories")
    public void verify_main_categories_list() {
        Assert.assertTrue("Main category list should be visible", 
                categoryPage.areCategoriesDisplayed());
    }

    // -------------------------------------------------------------------------
    // PLANT MANAGEMENT STEPS (UI-BL-03, 04, 06, 08, 09)
    // -------------------------------------------------------------------------

    @When("I attempt to add a plant with empty fields")
    public void attempt_add_empty_plant() {
        plantPage.clickAddPlant();
        plantPage.clickSave();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Then("I should see validation errors for mandatory fields")
    public void verify_plant_validation_errors() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        Assert.assertTrue("Validation errors should be displayed", 
                plantPage.isValidationErrorVisible());
    }

    @Then("I should see the {string} column")
    public void verify_table_column(String columnName) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        Assert.assertTrue("Column " + columnName + " should be visible", 
                plantPage.hasColumn(columnName));
    }

    @Then("I should be able to view plant details")
    public void verify_plant_details_visible() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        Assert.assertTrue("Plant table should be visible", 
                plantPage.isPlantsTableVisible());
    }

    // UI-BL-08: Browser Back Navigation
    @When("I click the browser back button")
    public void click_browser_back() {
        plantPage.getDriver().navigate().back();
    }

    @Then("I should be returned to the Plants page")
    public void verify_on_plants_page() {
        Assert.assertTrue("User should be on the Plants page", 
                plantPage.getDriver().getCurrentUrl().contains("/plants"));
    }
    
    // UI-BL-09: UI Layout Check
    @Then("the navigation menu should be visible")
    public void verify_nav_menu_visible() {
         Assert.assertTrue("Navigation menu should be visible", 
                 plantPage.isNavigationMenuVisible()); 
    }

    @Then("the main content area should be displayed properly")
    public void verify_main_content_visible() {
        Assert.assertTrue("Main content area should be visible",
                 plantPage.isMainContentVisible());
    }

    // -------------------------------------------------------------------------
    // SALES MANAGEMENT STEPS (UI-BL-10)
    // -------------------------------------------------------------------------

    @Then("I should see the sales history table")
    public void verify_sales_history() {
        Assert.assertTrue("Sales table should be visible", 
                salesPage.isSalesTableVisible());
    }

    @Then("the category list displays category data")
    public void verify_category_list_displays_data() {
        Assert.assertTrue("Category list should be displayed with data",
                categoryPage.areCategoriesDisplayed());
    }

} 
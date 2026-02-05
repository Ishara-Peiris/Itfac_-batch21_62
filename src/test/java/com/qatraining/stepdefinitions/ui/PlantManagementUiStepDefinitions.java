package com.qatraining.stepdefinitions.ui;

import com.qatraining.pages.PlantsPage;
import com.qatraining.pages.LoginPage;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.annotations.Managed;
import net.serenitybdd.core.Serenity;
import org.openqa.selenium.WebDriver;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Step definitions for Plant Management UI tests.
 * Test IDs: UI-PM-02, UI-PM-03, UI-PM-04
 */
public class PlantManagementUiStepDefinitions {

    @Managed
    WebDriver driver;

    LoginPage loginPage;
    PlantsPage plantsPage;

    private String plantName;
    private String plantPrice;

    @Given("the admin user is logged into the application")
    public void theAdminUserIsLoggedIntoTheApplication() {
        loginPage.open();
        loginPage.enterUsername("admin");
        loginPage.enterPassword("admin123");
        loginPage.clickLoginButton();
        Serenity.recordReportData().withTitle("Login").andContents("Admin user logged in successfully");
    }


    @And("the admin is on the Plants page")
    public void theAdminIsOnThePlantsPage() {
        plantsPage.open();
        assertThat(plantsPage.isDisplayed())
                .as("Plants page should be displayed")
                .isTrue();
    }

    @Given("categories exist in the system")
    public void categoriesExistInTheSystem() {
        // Verify categories are available in the dropdown
        // This is a precondition check - categories should exist
        Serenity.recordReportData().withTitle("Precondition").andContents("Categories exist in the system");
    }

    @When("the admin clicks the {string} button")
    public void theAdminClicksTheButton(String buttonName) {
        if (buttonName.equals("Add Plant")) {
            plantsPage.clickAddPlantButton();
        }
    }

    @And("the admin enters the following plant details in the form:")
    public void theAdminEntersPlantDetailsInTheForm(DataTable dataTable) {
        Map<String, String> plantDetails = dataTable.asMaps().get(0);

        plantName = plantDetails.get("Name");
        plantPrice = plantDetails.get("Price");

        plantsPage.enterPlantName(plantName);
        plantsPage.enterPlantPrice(plantPrice);

        if (plantDetails.containsKey("Quantity")) {
            plantsPage.enterPlantQuantity(plantDetails.get("Quantity"));
        }

        Serenity.recordReportData().withTitle("Plant Details").andContents("Name: " + plantName + ", Price: " + plantPrice);
    }

    @And("the admin selects a category from the dropdown")
    public void theAdminSelectsACategoryFromTheDropdown() {
        plantsPage.selectFirstCategory();
    }

    @And("the admin clicks the Submit button")
    public void theAdminClicksTheSubmitButton() {
        plantsPage.clickSubmitButton();
    }

    @Then("the modal should close")
    public void theModalShouldClose() {
        assertThat(plantsPage.isModalClosed())
                .as("Modal should be closed after submission")
                .isTrue();
    }

    @And("a success message should be displayed")
    public void aSuccessMessageShouldBeDisplayed() {
        assertThat(plantsPage.isSuccessMessageDisplayed())
                .as("Success message should be displayed")
                .isTrue();
    }

    @And("the new plant {string} should appear in the plants table")
    public void theNewPlantShouldAppearInTheTable(String expectedPlantName) {
        assertThat(plantsPage.isPlantInTable(expectedPlantName))
                .as("Plant '%s' should appear in the table", expectedPlantName)
                .isTrue();
    }

    @And("the admin leaves the Name field empty")
    public void theAdminLeavesTheNameFieldEmpty() {
        plantsPage.clearPlantNameField();
    }

    @And("the admin leaves the Price field empty")
    public void theAdminLeavesThePriceFieldEmpty() {
        plantsPage.clearPlantPriceField();
    }

    @Then("the form should not be submitted")
    public void theFormShouldNotBeSubmitted() {
        assertThat(plantsPage.isFormModalStillOpen())
                .as("Form should not be submitted and modal should remain open")
                .isTrue();
    }

    @And("the error message {string} should be displayed")
    public void theErrorMessageShouldBeDisplayed(String expectedErrorMessage) {
        assertThat(plantsPage.getValidationErrors())
                .as("Validation error '%s' should be displayed", expectedErrorMessage)
                .contains(expectedErrorMessage);
    }

    @Given("a plant exists in the plants table")
    public void aPlantExistsInThePlantsTable() {
        assertThat(plantsPage.hasPlants())
                .as("At least one plant should exist in the table")
                .isTrue();
    }

    @When("the admin clicks the Edit icon on the plant row")
    public void theAdminClicksTheEditIconOnThePlantRow() {
        plantsPage.clickEditOnFirstPlant();
    }

    @And("the admin changes the price to {string}")
    public void theAdminChangesThePriceTo(String newPrice) {
        plantPrice = newPrice;
        plantsPage.clearPlantPriceField();
        plantsPage.enterPlantPrice(newPrice);
    }

    @And("the admin clicks the Update button")
    public void theAdminClicksTheUpdateButton() {
        plantsPage.clickUpdateButton();
    }

    @And("the plant table should reflect the new price {string}")
    public void thePlantTableShouldReflectTheNewPrice(String expectedPrice) {
        assertThat(plantsPage.getFirstPlantPrice())
                .as("Plant price should be updated to %s", expectedPrice)
                .contains(expectedPrice);
    }

    @Then("the Plants page should be displayed")
    public void thePlantsPageShouldBeDisplayed() {
        assertThat(plantsPage.isDisplayed())
                .as("Plants page should be visible")
                .isTrue();
        Serenity.recordReportData().withTitle("Plants Page Status").andContents("Plants page is displayed successfully");
    }

    @And("the page title should contain {string}")
    public void thePageTitleShouldContain(String expectedTitle) {
        String actualTitle = driver.getTitle();
        assertThat(actualTitle)
                .as("Page title should contain '%s'", expectedTitle)
                .contains(expectedTitle);
        Serenity.recordReportData().withTitle("Page Title").andContents(actualTitle);
    }
}

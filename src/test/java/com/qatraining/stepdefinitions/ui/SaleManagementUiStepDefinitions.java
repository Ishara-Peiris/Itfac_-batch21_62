package com.qatraining.stepdefinitions.ui;


import com.qatraining.pages.LoginPage;
import com.qatraining.pages.SalesPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.core.pages.WebElementFacade;

import static net.serenitybdd.screenplay.ensure.Ensure.that;



public class SaleManagementUiStepDefinitions {
    LoginPage loginPage;
    SalesPage salesPage;


    @Given("Admin user logs into the system")
    public void admin_logs_in(){
        loginPage.open();
        loginPage.loginWith("admin","admin123");
    }

    @When("Admin navigates to Sales page")
    public void navigate_sales(){
        salesPage.openUrl("http://localhost:8080/ui/sales");
    }

    @Then("Sales management table should be visible")
    public void verify_table(){
        that(salesPage.salesTable.isDisplayed()).isTrue();
    }

    @Then("Add sale button should be visible")
    public void verify_button(){
        that(salesPage.addSaleButton.isDisplayed()).isTrue();
    }

    @When("Admin clicks the {string} button")
    public void admin_clicks_button(String buttonName) {
        if (buttonName.equals("Add Sale")) {
            salesPage.clickAddSale();
        }
    }

    @Then("Sales form should be displayed")
    public void verify_sales_form_visible() {
        that(salesPage.salesForm.isDisplayed()).isTrue();
    }

    @Then("Plant dropdown should be visible")
    public void verify_plant_dropdown() {
        that(salesPage.plantDropdown.isDisplayed()).isTrue();
    }

    @When("Admin enters quantity {string}")
    public void admin_enters_quantity(String qty){
        salesPage.enterQuantity(qty);
    }

    @When("Admin submits the sale form")
    public void admin_submits_form(){
        salesPage.clickSell();
    }

    @Then("Quantity validation message should appear")
    public void quantity_validation_should_appear(){
        that(salesPage.isQuantityFieldInvalid()).isTrue();
    }

    @When("Admin selects a plant")
    public void admin_selects_plant(){
        salesPage.selectFirstPlant();
    }

    @When("Admin enters valid quantity {string}")
    public void admin_enters_valid_quantity(String qty){
        salesPage.enterQuantity(qty);
    }

    @When("Admin submits the sale")
    public void admin_submits_sale(){
        salesPage.submitSale();
    }

    @Then("New sale with quantity {string} should appear in sales list")
    public void sale_should_appear(String qty){
        that(salesPage.isSalePresentInTable(qty)).isTrue();
    }

    @When("Admin enters excessive quantity")
    public void enter_excess_quantity(){
        salesPage.enterQuantity("100000");
    }

//    @When("Admin submits the sale form")
//    public void submit_form(){
//        salesPage.submitSale();
//    }

    @Then("Stock validation error should be displayed")
    public void verify_stock_error(){
        that(salesPage.stockErrorMessage.isDisplayed()).isTrue();
    }

    @Given("Read only user logs into the system")
    public void readonly_user_login(){
        loginPage.open();
        loginPage.loginWith("testuser","test123");
    }
    @When("User tries to open the sales creation URL directly")
    public void user_opens_sales_url(){
        salesPage.openUrl("http://localhost:8080/ui/sales/new");
    }
    @Then("Access to sales creation page should be denied")
    public void verify_access_denied() {
        that(salesPage.accessDeniedMessage.isDisplayed()).isTrue();
    }

    @When("User navigates to Sales page")
    public void user_navigates_to_sales_page() {
        salesPage.openUrl("http://localhost:8080/ui/sales");
    }

    //test that read-only user can see sales history but cannot add new sales
    @Then("Sales history should be visible to read only user")
    public void verify_sales_history_visible() {

        // Either table OR no-records message should appear
        boolean tableVisible = salesPage.salesTable.isPresent();
        boolean noRecordsVisible = salesPage.noRecordsMessage.isPresent();

        that(tableVisible || noRecordsVisible).isTrue();
    }

    @Then("User should not see option to add sales")
    public void verify_no_add_sale_button() {
        that(salesPage.addSaleButton.isPresent()).isFalse();
    }

    //when user tries to submit sale without selecting plant
    @Then("Plant validation error should be displayed")
    public void plant_validation_error() {
        salesPage.errorAlert.waitUntilVisible();
        that(salesPage.errorAlert.isVisible()).isTrue();
    }



}
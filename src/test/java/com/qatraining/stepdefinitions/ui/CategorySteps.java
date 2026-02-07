package com.qatraining.stepdefinitions.ui;

import io.cucumber.java.en.*;
import org.junit.Assert;
import com.qatraining.pages.LoginPage;
import com.qatraining.pages.CategoryPage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CategorySteps {

    LoginPage loginPage;
    CategoryPage categoryPage;

    @Given("the admin is logged in and on the Category page")
    public void admin_login() {
        loginPage.open();
        loginPage.loginWith("admin", "admin123");
        categoryPage.navigateToCategoryPage();
        categoryPage.clickAddCategoryIfOnList();
    }

    @Given("the {string} is logged in and on the Category page")
    public void role_login(String role) {
        loginPage.open();
        if (role.equalsIgnoreCase("User")) {
            // UPDATED: Correct credentials for non-admin user
            loginPage.loginWith("testuser", "test123");
        } else {
            loginPage.loginWith("admin", "admin123");
        }
        categoryPage.navigateToCategoryPage();
    }

    @Given("I am on the Category Management page")
    public void generic_nav() {
        loginPage.open();
        loginPage.loginWith("admin", "admin123");
        categoryPage.navigateToCategoryPage();
    }

    @When("the admin enters the category name {string}")
    public void enterName(String name) {
        categoryPage.enterCategoryName(name);
    }

    @And("the admin leaves Parent Category empty")
    public void leaveParentEmpty() {
        categoryPage.selectParentCategory("Main Category");
    }

    @And("the admin selects {string} as the Parent Category")
    public void selectParent(String parent) {
        categoryPage.selectParentCategory(parent);
    }

    @And("the admin clicks the Save button")
    public void save() {
        categoryPage.clickSave();
    }

    @Then("the category success message should be displayed")
    public void verifySuccess() {
        Assert.assertTrue("Success message not visible!", categoryPage.isSuccessMessageDisplayed());
    }

    @And("the new category {string} should be visible in the table")
    public void verifyInTable(String name) {
        categoryPage.navigateToCategoryPage();
        Assert.assertTrue("Category not in table!", categoryPage.isCategoryInTable(name));
    }

    @And("the category {string} should be visible as a child of {string}")
    public void verifyLinkage(String child, String parent) {
        categoryPage.navigateToCategoryPage();
        Assert.assertTrue("Child-Parent link incorrect!", categoryPage.isCategoryLinkedToParent(child, parent));
    }

    @Then("the error message {string} should be displayed")
    public void verifyError(String msg) {
        Assert.assertEquals(msg, categoryPage.getValidationErrorMessage());
    }

    @Then("the Add Category button should not be visible")
    public void verifyRBAC() {
        Assert.assertFalse("Security Fail: User can see Add button!", categoryPage.isAddButtonVisible());
    }

    @When("I enter {string} in the search bar and press search")
    public void search(String term) {
        categoryPage.enterSearchTerm(term);
        categoryPage.clickSearch();
    }

    @Then("the list should only show categories containing {string}")
    public void verifySearch(String term) {
        List<String> results = categoryPage.getVisibleCategoryNames();
        for (String res : results) {
            Assert.assertTrue(res.toLowerCase().contains(term.toLowerCase()));
        }
    }

    // FIXED: Accepts the String parameter to prevent Cucumber arity mismatch
    @Then("the message {string} should be displayed in the table")
    public void verifyEmptyState(String expectedMsg) {
        Assert.assertTrue("No category found message missing!", categoryPage.isNoCategoryFoundMessageVisible());
    }

    @When("the user clicks the ID column header")
    public void sortId() {
        categoryPage.clickIdHeader();
    }

    @Then("the categories should be sorted by ID in {string} order")
    public void verifySort(String order) {
        List<Integer> actual = categoryPage.getAllIdsInTable();
        List<Integer> expected = new ArrayList<>(actual);
        if (order.equalsIgnoreCase("ascending")) Collections.sort(expected);
        else Collections.sort(expected, Collections.reverseOrder());
        Assert.assertEquals(expected, actual);
    }

    @When("the user clicks the Next page button")
    public void next() {
        categoryPage.clickNextPage();
    }

    @Then("the UI should load page {string}")
    public void verifyPage(String num) {
        Assert.assertEquals(num, categoryPage.getActivePageNumber());
    }
}
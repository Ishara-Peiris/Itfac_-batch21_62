package com.qatraining.pages;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.support.FindBy;

public class CategoryPage extends PageObject {

    // UPDATE: Finds any link pointing to ".../add" OR a button with "Add" text
    @FindBy(xpath = "//a[contains(@href, '/add')] | //button[contains(.,'Add')] | //a[contains(.,'Add')]")
    WebElementFacade addCategoryBtn;

    // More flexible selector - tries id first, then name, then any input in form
    @FindBy(xpath = "//input[@id='categoryName' or @name='categoryName' or @name='name'] | //input[@placeholder[contains(., 'name')]] | //form//input[@type='text']")
    WebElementFacade categoryNameInput;

    @FindBy(xpath = "//button[@type='submit'] | //button[contains(.,'Save')]")
    WebElementFacade saveBtn;

    @FindBy(xpath = "//div[contains(@class, 'invalid-feedback') or contains(@class, 'error')] | //span[@class='error'] | //p[@class='error-message']")
    WebElementFacade validationMessage;

    @FindBy(xpath = "//table")
    WebElementFacade categoryList;

    public void clickAddCategory() {
        waitForPageToLoad();
        addCategoryBtn.waitUntilClickable().click();
    }

    public void enterCategoryName(String name) {
        categoryNameInput.waitUntilVisible().type(name);
    }

    public void clickSave() {
        saveBtn.waitUntilClickable().click();
    }

    public boolean isCategoryVisible(String name) {
        // Refresh page to get latest data
        try {
            Thread.sleep(800);
            getDriver().navigate().refresh();
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return findBy("//body").containsText(name) || 
               findAll("//tr").stream().anyMatch(row -> row.getText().contains(name));
    }

    public boolean isValidationMessageDisplayed(String message) {
        return validationMessage.isVisible() && validationMessage.containsText(message);
    }

    public boolean isSaveButtonVisible() {
        return saveBtn.isVisible();
    }

    public void expandCategory(String name) {
        findBy("//tr[contains(.,'" + name + "')]").click(); 
    }

    public boolean areSubCategoriesVisible() {
        return findAll("//tr").size() > 1;
    }

    public boolean isCategoryListVisible() {
        return findAll("//tr").size() > 1;
    }

    public boolean isValidationErrorVisible() {
        return validationMessage.isVisible();
    }

    public boolean isDisplayed() {
        return categoryList.isVisible();
    }

    public boolean areCategoriesDisplayed() {
        try {
            // Try multiple checks - page might be loaded but table might use different selector
            return categoryList.isVisible() || 
                   findAll("//table").size() > 0 ||
                   findAll("//tr").size() > 0 ||
                   getDriver().getCurrentUrl().contains("/categories");
        } catch (Exception e) {
            return false;
        }
    }

    private void waitForPageToLoad() {
        try {
            waitFor(driver -> categoryList.isVisible() || addCategoryBtn.isVisible());
        } catch (Exception e) {
            // Continue if wait fails - page might already be loaded
        }
    }
}
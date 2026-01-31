package com.qatraining.pages;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.annotations.DefaultUrl;
import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Page Object for the Plants Management page.
 * Contains all web elements and actions related to plant management functionality.
 */
@DefaultUrl("/plants")
public class PlantsPage extends PageObject {

    // Add Plant Button
    @FindBy(css = "button[data-testid='add-plant-btn'], button.add-plant-btn, button:contains('Add Plant')")
    private WebElementFacade addPlantButton;

    // Modal/Form Elements
    @FindBy(css = ".modal, .plant-modal, [role='dialog']")
    private WebElementFacade modal;

    @FindBy(css = "input[name='name'], input#plant-name, input[data-testid='plant-name']")
    private WebElementFacade plantNameField;

    @FindBy(css = "input[name='price'], input#plant-price, input[data-testid='plant-price']")
    private WebElementFacade plantPriceField;

    @FindBy(css = "input[name='quantity'], input#plant-quantity, input[data-testid='plant-quantity']")
    private WebElementFacade plantQuantityField;

    @FindBy(css = "select[name='category'], select#category, select[data-testid='category-select']")
    private WebElementFacade categoryDropdown;

    @FindBy(css = "button[type='submit'], button.submit-btn, button[data-testid='submit-btn']")
    private WebElementFacade submitButton;

    @FindBy(css = "button.update-btn, button[data-testid='update-btn'], button:contains('Update'), button:contains('Save')")
    private WebElementFacade updateButton;

    // Success/Error Messages
    @FindBy(css = ".success-message, .toast-success, .alert-success, [role='alert'].success")
    private WebElementFacade successMessage;

    @FindBy(css = ".error-message, .validation-error, .field-error, .invalid-feedback")
    private List<WebElementFacade> validationErrors;

    // Plants Table
    @FindBy(css = "table tbody tr, .plant-row, [data-testid='plant-row']")
    private List<WebElementFacade> plantRows;

    @FindBy(css = "table tbody tr:first-child, .plant-row:first-child")
    private WebElementFacade firstPlantRow;

    @FindBy(css = "table tbody tr:first-child .edit-btn, table tbody tr:first-child button[aria-label='Edit'], .plant-row:first-child .edit-icon")
    private WebElementFacade firstEditButton;

    @FindBy(css = "table tbody tr:first-child td.price, table tbody tr:first-child [data-field='price']")
    private WebElementFacade firstPlantPriceCell;

    // Page Container
    @FindBy(css = ".plants-page, .plants-container, [data-testid='plants-page']")
    private WebElementFacade plantsContainer;

    /**
     * Check if plants page is displayed.
     * @return true if page is visible
     */
    public boolean isDisplayed() {
        return plantsContainer.isVisible() || !plantRows.isEmpty();
    }

    /**
     * Click the Add Plant button.
     */
    public void clickAddPlantButton() {
        addPlantButton.waitUntilClickable();
        addPlantButton.click();
    }

    /**
     * Enter plant name in the form.
     * @param name the plant name
     */
    public void enterPlantName(String name) {
        plantNameField.waitUntilVisible();
        plantNameField.clear();
        plantNameField.type(name);
    }

    /**
     * Enter plant price in the form.
     * @param price the plant price
     */
    public void enterPlantPrice(String price) {
        plantPriceField.waitUntilVisible();
        plantPriceField.clear();
        plantPriceField.type(price);
    }

    /**
     * Enter plant quantity in the form.
     * @param quantity the plant quantity
     */
    public void enterPlantQuantity(String quantity) {
        plantQuantityField.waitUntilVisible();
        plantQuantityField.clear();
        plantQuantityField.type(quantity);
    }

    /**
     * Clear the plant name field.
     */
    public void clearPlantNameField() {
        plantNameField.waitUntilVisible();
        plantNameField.clear();
    }

    /**
     * Clear the plant price field.
     */
    public void clearPlantPriceField() {
        plantPriceField.waitUntilVisible();
        plantPriceField.clear();
    }

    /**
     * Select the first category from the dropdown.
     */
    public void selectFirstCategory() {
        categoryDropdown.waitUntilVisible();
        categoryDropdown.selectByIndex(1); // Select first non-empty option
    }

    /**
     * Select category by name.
     * @param categoryName the category name to select
     */
    public void selectCategory(String categoryName) {
        categoryDropdown.waitUntilVisible();
        categoryDropdown.selectByVisibleText(categoryName);
    }

    /**
     * Click the Submit button.
     */
    public void clickSubmitButton() {
        submitButton.waitUntilClickable();
        submitButton.click();
    }

    /**
     * Click the Update button.
     */
    public void clickUpdateButton() {
        updateButton.waitUntilClickable();
        updateButton.click();
    }

    /**
     * Check if modal is closed.
     * @return true if modal is not visible
     */
    public boolean isModalClosed() {
        return !modal.isVisible();
    }

    /**
     * Check if form modal is still open.
     * @return true if modal is visible
     */
    public boolean isFormModalStillOpen() {
        return modal.isVisible();
    }

    /**
     * Check if success message is displayed.
     * @return true if success message is visible
     */
    public boolean isSuccessMessageDisplayed() {
        return successMessage.isVisible();
    }

    /**
     * Get all validation error messages.
     * @return list of error message texts
     */
    public List<String> getValidationErrors() {
        return validationErrors.stream()
                .filter(WebElementFacade::isVisible)
                .map(WebElementFacade::getText)
                .collect(Collectors.toList());
    }

    /**
     * Check if plant exists in the table.
     * @param plantName the plant name to search for
     * @return true if plant is found in table
     */
    public boolean isPlantInTable(String plantName) {
        return plantRows.stream()
                .anyMatch(row -> row.getText().contains(plantName));
    }

    /**
     * Check if table has any plants.
     * @return true if table has at least one plant
     */
    public boolean hasPlants() {
        return !plantRows.isEmpty();
    }

    /**
     * Click edit button on the first plant row.
     */
    public void clickEditOnFirstPlant() {
        firstEditButton.waitUntilClickable();
        firstEditButton.click();
    }

    /**
     * Get the price of the first plant in the table.
     * @return the price text
     */
    public String getFirstPlantPrice() {
        if (firstPlantPriceCell.isVisible()) {
            return firstPlantPriceCell.getText();
        }
        // Alternative: get from first row text
        return firstPlantRow.getText();
    }

    /**
     * Get the number of plants in the table.
     * @return count of plant rows
     */
    public int getPlantCount() {
        return plantRows.size();
    }
}

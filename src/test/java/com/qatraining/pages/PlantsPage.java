package com.qatraining.pages;

import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.annotations.DefaultUrl;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;
import java.util.concurrent.TimeUnit;

/**
 * Page Object for the Plants Management page.
 * Contains all web elements and actions related to plant management functionality.
 */
@DefaultUrl("/ui/plants")
public class PlantsPage extends BasePage {

    // Page Header
    @FindBy(css = "h2, h3")
    private WebElementFacade pageHeader;

    // Navigation Menu
    @FindBy(css = ".sidebar")
    private WebElementFacade navigationMenu;

    @FindBy(css = "a[href='/ui/plants']")
    private WebElementFacade plantsNavLink;

    // Add Plant Button
    @FindBy(css = "a[href='/ui/plants/add']")
    private WebElementFacade addPlantButton;

    // Modal/Form Elements
    @FindBy(css = ".modal, .plant-modal, [role='dialog']")
    private WebElementFacade modal;

    @FindBy(id = "name")
    private WebElementFacade plantNameField;

    @FindBy(id = "price")
    private WebElementFacade plantPriceField;

    @FindBy(id = "quantity")
    private WebElementFacade plantQuantityField;

    @FindBy(id = "categoryId")
    private WebElementFacade categoryDropdown;

    @FindBy(id = "image")
    private WebElementFacade imageField;

    @FindBy(css = "button.btn-primary")
    private WebElementFacade submitButton;

    @FindBy(css = "button.update-btn, button[data-testid='update-btn'], button:contains('Update'), button:contains('Save')")
    private WebElementFacade updateButton;

    // Success/Error Messages
    @FindBy(css = ".success-message, .toast-success, .alert-success, [role='alert'].success")
    private WebElementFacade successMessage;

    @FindBy(css = ".error-message, .validation-error, .field-error, .invalid-feedback")
    private List<WebElementFacade> validationErrors;

    // Table Interaction Helpers
    public WebElementFacade getRowForPlant(String plantName) {
        return plantRows.stream()
                .filter(row -> row.getText().contains(plantName))
                .findFirst()
                .orElse(null);
    }

    // Plants Table
    @FindBy(css = "table.table tbody tr")
    private List<WebElementFacade> plantRows;

    @FindBy(css = "table.table tbody tr:first-child")
    private WebElementFacade firstPlantRow;

    @FindBy(css = "table.table tbody tr:first-child a[href*='/edit']")
    private WebElementFacade firstEditButton;

    @FindBy(css = "table.table tbody tr:first-child td:nth-child(3)")
    private WebElementFacade firstPlantPriceCell;

    // Table Header
    @FindBy(css = "table.table thead")
    private WebElementFacade tableHeader;

    // Page Container
    @FindBy(css = ".main-content, .app-layout")
    private WebElementFacade plantsContainer;

    /**
     * Check if plants page is displayed.
     * @return true if page is visible
     */
    public boolean isDisplayed() {
        return plantsContainer.isVisible() || !plantRows.isEmpty();
    }

    /**
     * Check if page has fully loaded (wait for loading spinner to disappear)
     */
    public void waitForPageToLoad() {
        withTimeoutOf(2, TimeUnit.SECONDS).waitFor(pageHeader);
    }

    /**
     * Check if table has plants loaded.
     * @return true if at least one plant row exists
     */
    public boolean hasPlants() {
        return !plantRows.isEmpty();
    }

    /**
     * Get the number of plants in the table.
     * @return count of plant rows
     */
    public int getPlantCount() {
        return plantRows.size();
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
     * Click the Add Plant button with reduced wait time.
     */
    public void clickAddPlantButton() {
        addPlantButton.withTimeoutOf(2, TimeUnit.SECONDS).waitUntilClickable();
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
     * Submit the plant form with reduced wait time.
     */
    public void submitPlantForm() {
        submitButton.withTimeoutOf(2, TimeUnit.SECONDS).waitUntilClickable();
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
     * Check if add plant form is displayed.
     * @return true if form is visible
     */
    public boolean isAddPlantFormDisplayed() {
        return plantNameField.isVisible() && plantPriceField.isVisible();
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
     * Retrieve all validation error messages displayed on the page.
     * @return List of validation error messages
     */
    public List<String> getValidationErrors() {
        return validationErrors.stream()
                .filter(WebElementFacade::isVisible)
                .map(WebElementFacade::getText)
                .collect(Collectors.toList());
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
     * Get the page title/header text.
     * @return the header text
     */
    public String getPageHeaderText() {
        if (pageHeader.isVisible()) {
            return pageHeader.getText();
        }
        return "";
    }

    /**
     * Check if page header is visible.
     * @return true if header is visible
     */
    public boolean isPageHeaderVisible() {
        return pageHeader.isVisible();
    }

    /**
     * Check if Plants navigation link is visible.
     * @return true if Plants nav link is visible
     */
    public boolean isPlantsNavLinkVisible() {
        return plantsNavLink.isVisible();
    }

    /**
     * Click on Plants navigation link.
     */
    public void clickPlantsNavLink() {
        plantsNavLink.waitUntilClickable();
        plantsNavLink.click();
    }

    /**
     * Get all plant names from the table.
     * @return list of plant names
     */
    public List<String> getAllPlantNames() {
        return plantRows.stream()
                .map(WebElementFacade::getText)
                .collect(Collectors.toList());
    }

    /**
     * Click the edit button for a specific plant by name.
     * @param plantName the name of the plant to edit
     */
    public void clickEditButtonForPlant(String plantName) {
        WebElementFacade row = getRowForPlant(plantName);
        if (row != null) {
            WebElementFacade editButton = row.find(org.openqa.selenium.By.cssSelector("a[title='Edit']"));
            editButton.waitUntilClickable();
            editButton.click();
        } else {
            throw new RuntimeException("Plant not found: " + plantName);
        }
    }

    /**
     * Get the price of a specific plant from the table.
     * @param plantName the name of the plant
     * @return the price text from the table
     */
    public String getPlantPriceFromTable(String plantName) {
        WebElementFacade row = getRowForPlant(plantName);
        if (row != null) {
            // Price is in the 3rd column (index 3 in CSS selector)
            WebElementFacade priceCell = row.find(org.openqa.selenium.By.cssSelector("td:nth-child(3)"));
            return priceCell.getText();
        }
        return null;
    }

    /**
     * Get the category of a specific plant from the table.
     * @param plantName the name of the plant
     * @return the category text from the table
     */
    public String getPlantCategoryFromTable(String plantName) {
        WebElementFacade row = getRowForPlant(plantName);
        if (row != null) {
            // Category is in the 2nd column
            WebElementFacade categoryCell = row.find(org.openqa.selenium.By.cssSelector("td:nth-child(2)"));
            return categoryCell.getText();
        }
        return null;
    }

    /**
     * Wait for the edit page to load.
     */
    public void waitForEditPageToLoad() {
        plantNameField.waitUntilVisible();
        plantPriceField.waitUntilVisible();
    }

    /**
     * Click the Save button on edit form.
     */
    public void clickSaveButton() {
        submitButton.waitUntilClickable();
        submitButton.click();
    }

    /**
     * Click the delete button for a specific plant by name.
     * @param plantName the name of the plant to delete
     */
    public void clickDeleteButtonForPlant(String plantName) {
        WebElementFacade row = getRowForPlant(plantName);
        if (row != null) {
            WebElementFacade deleteButton = row.find(org.openqa.selenium.By.cssSelector("button[title='Delete']"));
            deleteButton.waitUntilClickable();
            deleteButton.click();
        } else {
            throw new RuntimeException("Plant not found: " + plantName);
        }
    }

    /**
     * Accept the browser confirmation alert for delete action.
     */
    public void acceptDeleteConfirmation() {
        try {
            getDriver().switchTo().alert().accept();
        } catch (Exception e) {
            // Alert might not appear if handled by JavaScript
        }
    }

    /**
     * Check if the "Add Plant" button is visible on the page.
     * @return true if button is visible, false otherwise
     */
    public boolean isAddPlantButtonVisible() {
        return addPlantButton.isPresent() && addPlantButton.isVisible();
    }

    /**
     * Verify if only the list view is available (no edit/delete buttons in table).
     * @return true if no administrative action buttons are found in the rows
     */
    public boolean isOnlyListViewAvailable() {
        // Find any edit or delete buttons in the table body
        return findAll(org.openqa.selenium.By.cssSelector("table.table tbody a[title='Edit'], table.table tbody button[title='Delete']")).isEmpty();
    }

    /**
     * Get the count of action buttons (Edit/Delete) in the table.
     * @return count of found buttons
     */
    public int getActionButtonsCount() {
        return findAll(org.openqa.selenium.By.cssSelector("table.table tbody a[title='Edit'], table.table tbody button[title='Delete'], .bi-pencil, .bi-trash")).size();
    }

    /**
     * Get the image src for a specific plant.
     * @param plantName the name of the plant
     * @return the src attribute of the plant's image
     */
    public String getPlantImageSrc(String plantName) {
        WebElementFacade row = getRowForPlant(plantName);
        if (row != null) {
            List<org.openqa.selenium.WebElement> images = row.findElements(org.openqa.selenium.By.tagName("img"));
            return !images.isEmpty() ? images.get(0).getAttribute("src") : null;
        }
        return null;
    }

    /**
     * Check if the plant has a default placeholder image.
     * @param plantName the name of the plant
     * @return true if the image src contains default placeholder indicators
     */
    public boolean isDefaultPlaceholderDisplayed(String plantName) {
        WebElementFacade row = getRowForPlant(plantName);
        if (row == null) return false;

        String src = getPlantImageSrc(plantName);
        if (src != null) {
            return src.contains("default") || src.contains("no-image") || src.contains("placeholder");
        }

        // If no img tag, check for icon-based placeholder (e.g., Bootstrap Icons)
        return !row.findElements(org.openqa.selenium.By.cssSelector("i.bi-image, i.bi-camera, .placeholder-icon")).isEmpty();
    }

    /**
     * Check if the layout is intact by verifying table and sidebar visibility.
     * @return true if main structural elements are visible
     */
    public boolean isLayoutIntact() {
        return plantsContainer.isVisible() && navigationMenu.isVisible();
    }
}

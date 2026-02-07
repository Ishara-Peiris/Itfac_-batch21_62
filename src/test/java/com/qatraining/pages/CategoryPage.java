package com.qatraining.pages;

import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.annotations.DefaultUrl;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import java.util.List;
import java.util.stream.Collectors;
import java.time.Duration;

@DefaultUrl("/ui/categories")
public class CategoryPage extends BasePage {

    @FindBy(css = "a[href='/ui/categories/add']")
    private WebElementFacade addCategoryButton;

    @FindBy(id = "name")
    private WebElementFacade nameField;

    @FindBy(id = "parentId")
    private WebElementFacade parentCategoryDropdown;

    @FindBy(xpath = "//button[contains(text(),'Save')]")
    private WebElementFacade saveButton;

    @FindBy(css = ".alert-success, .alert-info, .alert.alert-success")
    private WebElementFacade successMessage;

    @FindBy(css = ".invalid-feedback, .alert-danger")
    private WebElementFacade validationErrorMessage;

    @FindBy(css = "table.table tbody tr")
    private List<WebElementFacade> categoryRows;

    @FindBy(name = "name")
    private WebElementFacade searchInputField;

    @FindBy(css = "button[type='submit']")
    private WebElementFacade searchButton;

    @FindBy(xpath = "//th[a[contains(@href, 'sortField=id')]]/a")
    private WebElementFacade idColumnHeader;

    @FindBy(css = "table.table tbody tr td:nth-child(1)")
    private List<WebElementFacade> idColumnCells;

    @FindBy(xpath = "//td[contains(text(),'No category found')]")
    private WebElementFacade noCategoryFoundMessage;

    @FindBy(xpath = "//a[contains(text(),'Next')]")
    private WebElementFacade nextButton;

    @FindBy(css = "li.page-item.active a")
    private WebElementFacade activePage;

    public void navigateToCategoryPage() {
        if (!getDriver().getCurrentUrl().contains("/ui/categories")) {
            open();
        }
    }

    public void clickAddCategoryIfOnList() {
        addCategoryButton.withTimeoutOf(Duration.ofSeconds(5)).waitUntilClickable().click();
    }

    public void enterCategoryName(String name) {
        nameField.waitUntilVisible().clear();
        nameField.type(name);
    }

    public void selectParentCategory(String parentName) {
        parentCategoryDropdown.waitUntilVisible();
        if (parentName == null || parentName.isEmpty() || parentName.equalsIgnoreCase("Main Category")) {
            parentCategoryDropdown.selectByIndex(0);
        } else {
            parentCategoryDropdown.selectByVisibleText(parentName);
        }
    }

    public void clickSave() {
        saveButton.waitUntilClickable().click();
    }

    public boolean isSuccessMessageDisplayed() {
        try {
            return successMessage.withTimeoutOf(Duration.ofSeconds(5)).isCurrentlyVisible();
        } catch (Exception e) {
            return false;
        }
    }

    public String getValidationErrorMessage() {
        return validationErrorMessage.waitUntilVisible().getText().trim();
    }

    public boolean isCategoryInTable(String name) {
        getDriver().navigate().refresh();
        return categoryRows.stream().anyMatch(row -> row.getText().contains(name));
    }

    public boolean isCategoryLinkedToParent(String child, String parent) {
        return categoryRows.stream().anyMatch(row ->
                row.find(By.xpath("./td[2]")).getText().trim().equals(child) &&
                        row.find(By.xpath("./td[3]")).getText().trim().contains(parent));
    }

    public boolean isAddButtonVisible() {
        return addCategoryButton.isCurrentlyVisible();
    }

    public void enterSearchTerm(String term) {
        searchInputField.waitUntilVisible().type(term);
    }

    public void clickSearch() {
        searchButton.click();
    }

    public List<String> getVisibleCategoryNames() {
        return categoryRows.stream()
                .map(row -> row.find(By.xpath("./td[2]")).getText().trim())
                .collect(Collectors.toList());
    }

    public void clickIdHeader() {
        idColumnHeader.click();
    }

    public List<Integer> getAllIdsInTable() {
        return idColumnCells.stream()
                .map(cell -> Integer.parseInt(cell.getText().trim()))
                .collect(Collectors.toList());
    }

    public boolean isNoCategoryFoundMessageVisible() {
        return noCategoryFoundMessage.withTimeoutOf(Duration.ofSeconds(3)).isVisible();
    }

    public void clickNextPage() {
        nextButton.click();
    }

    public String getActivePageNumber() {
        return activePage.getText().trim();
    }
}
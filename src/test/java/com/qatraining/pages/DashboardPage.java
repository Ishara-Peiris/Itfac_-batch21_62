package com.qatraining.pages;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.support.FindBy;

public class DashboardPage extends PageObject {

    @FindBy(css = "nav.sidebar") // Adjust selector based on actual UI
    WebElementFacade navMenu;

    @FindBy(id = "main-content")
    WebElementFacade mainContent;

    @FindBy(linkText = "Categories")
    WebElementFacade categoriesLink;

    @FindBy(linkText = "Plants")
    WebElementFacade plantsLink;

    @FindBy(linkText = "Sales")
    WebElementFacade salesLink;

    public void clickCategoriesMenu() {
        categoriesLink.click();
    }

    public void clickPlantsMenu() {
        plantsLink.click();
    }

    public void clickSalesMenu() {
        salesLink.click();
    }

    public boolean isNavMenuVisible() {
        return navMenu.isVisible();
    }

    public boolean isMainContentVisible() {
        return mainContent.isVisible();
    }
}
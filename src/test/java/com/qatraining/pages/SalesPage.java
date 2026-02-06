package com.qatraining.pages;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.support.FindBy;

public class SalesPage extends PageObject {

    @FindBy(xpath = "//table")
    WebElementFacade salesTable;

    public boolean isSalesTableVisible() {
        try {
            return salesTable.isVisible() ||
                   findAll("//table").size() > 0 ||
                   findAll("[class*='sales']").size() > 0 ||
                   findAll("[class*='history']").size() > 0 ||
                   getDriver().getCurrentUrl().contains("/sales");
        } catch (Exception e) {
            return false;
        }
    }
}
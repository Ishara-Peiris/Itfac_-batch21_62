package com.qatraining.pages;

import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.WebElement;

import java.util.List;

public class SalesPage extends PageObject {

    @FindBy(xpath = "//table[.//a[contains(text(),'Plant')] and .//a[contains(text(),'Quantity')]]")
    public WebElementFacade salesTable;

//    @FindBy(xpath = "//button[contains(text(),'Add Sale')]")
//    public WebElementFacade addSaleButton;

    @FindBy(xpath = "//a[@href='/ui/sales/new']")
    public WebElementFacade addSaleButton;


    @FindBy(xpath = "//form[@action='/ui/sales']")
    public WebElementFacade salesForm;


    @FindBy(id = "plantId")
    public WebElementFacade plantDropdown;

    @FindBy(xpath="//div[contains(@class,'text-danger')]")
    public WebElementFacade errorAlert;



    public void clickAddSale() {
        waitFor(addSaleButton).waitUntilClickable().click();
    }

    @FindBy(id = "quantity")
    public WebElementFacade quantityField;

    @FindBy(xpath = "//button[contains(text(),'Sell')]")
    public WebElementFacade sellButton;

    public void enterQuantity(String qty){
        quantityField.clear();
        quantityField.type(qty);
    }

    public void clickSell(){
        sellButton.click();
    }

    public boolean isQuantityFieldInvalid(){
        return quantityField.getAttribute("validationMessage").length() > 0;
    }

    @FindBy(xpath = "//table//tr")
    public List<WebElementFacade> tableRows;

    public void selectFirstPlant(){
        plantDropdown.waitUntilVisible();
        plantDropdown.selectByIndex(1); // first real plant (index 0 is --Select--)
    }

    public void submitSale(){
        sellButton.waitUntilEnabled().click();
    }

    public boolean isSalePresentInTable(String qty){
        waitFor(salesTable).waitUntilVisible();

        for(WebElementFacade row : tableRows){
            if(row.getText().contains(qty)){
                return true;
            }
        }
        return false;
    }

    @FindBy(css = "div.alert.alert-danger")
    public WebElementFacade stockErrorMessage;

    @FindBy(xpath = "//h2[contains(text(),'Access Denied')]")
    public WebElementFacade accessDeniedMessage;

    //if no sales present, a message like "No records found" or "No sales available" is shown
    @FindBy(xpath = "//*[contains(text(),'No records') or contains(text(),'No sales')]")
    public WebElementFacade noRecordsMessage;

}

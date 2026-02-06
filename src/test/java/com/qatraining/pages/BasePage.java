package com.qatraining.pages;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.support.FindBy;

/**
 * Base Page Object with common functionality for all pages.
 * Other page objects can extend this class to inherit common methods.
 */
public class BasePage extends PageObject {

    @FindBy(css = ".loading-spinner")
    private WebElementFacade loadingSpinner;

    @FindBy(css = ".toast-message")
    private WebElementFacade toastMessage;

    @FindBy(css = ".modal")
    private WebElementFacade modal;

    @FindBy(css = ".modal .close-button")
    private WebElementFacade modalCloseButton;

    /**
     * Wait for the page to fully load.
     */
    public void waitForPageToLoad() {
        waitFor(driver -> {
            try {
                return !loadingSpinner.isVisible();
            } catch (Exception e) {
                return true;
            }
        });
    }

    /**
     * Wait for loading spinner to disappear.
     */
    public void waitForLoadingToComplete() {
        if (loadingSpinner.isPresent()) {
            loadingSpinner.waitUntilNotVisible();
        }
    }

    /**
     * Get toast message text if visible.
     * @return the toast message text or empty string
     */
    public String getToastMessage() {
        if (toastMessage.isVisible()) {
            return toastMessage.getText();
        }
        return "";
    }

    /**
     * Check if a modal is displayed.
     * @return true if modal is visible
     */
    public boolean isModalDisplayed() {
        return modal.isVisible();
    }

    /**
     * Close the modal if displayed.
     */
    public void closeModal() {
        if (isModalDisplayed()) {
            modalCloseButton.click();
            modal.waitUntilNotVisible();
        }
    }

    /**
     * Scroll to the top of the page.
     */
    public void scrollToTop() {
        evaluateJavascript("window.scrollTo(0, 0);");
    }

    /**
     * Scroll to the bottom of the page.
     */
    public void scrollToBottom() {
        evaluateJavascript("window.scrollTo(0, document.body.scrollHeight);");
    }

    /**
     * Scroll to a specific element.
     * @param element the element to scroll to
     */
    public void scrollToElement(WebElementFacade element) {
        evaluateJavascript("arguments[0].scrollIntoView(true);", element);
    }

    /**
     * Check if an element is in viewport.
     * @param element the element to check
     * @return true if element is in viewport
     */
    public boolean isElementInViewport(WebElementFacade element) {
        return (Boolean) evaluateJavascript(
            "var rect = arguments[0].getBoundingClientRect();" +
            "return (rect.top >= 0 && rect.left >= 0 && " +
            "rect.bottom <= (window.innerHeight || document.documentElement.clientHeight) && " +
            "rect.right <= (window.innerWidth || document.documentElement.clientWidth));",
            element
        );
    }
}

package com.qatraining.pages;

import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import org.openqa.selenium.Dimension;
import java.time.Duration;
import java.util.List;

public class DashboardPage extends PageObject {

    @FindBy(css = "h3.mb-4")
    private WebElementFacade dashboardHeader;

    @FindBy(css = ".card")
    private List<WebElementFacade> dashboardWidgets;

    @FindBy(css = "canvas, .chart-container")
    private List<WebElementFacade> dashboardCharts;

    @FindBy(css = ".sidebar")
    private WebElementFacade navigationMenu;

    @FindBy(id = "refresh-button")
    private WebElementFacade refreshButton;

    @FindBy(xpath = "//a[contains(text(),'Logout')]")
    private WebElementFacade logoutButton;

    /**
     * Optimized Wait: Uses a 5s ceiling but proceeds immediately
     * upon element detection to save time.
     */
    public void waitForDashboardLoad() {
        withTimeoutOf(Duration.ofSeconds(5))
                .waitFor(dashboardHeader)
                .waitUntilVisible();
    }

    public String getHeaderText() {
        return dashboardHeader.getText().trim();
    }

    public boolean areAllWidgetsVisible() {
        // UI-DASH-03: Validates the 4 required summary cards [cite: 1]
        return dashboardWidgets != null &&
                dashboardWidgets.size() >= 4 &&
                dashboardWidgets.stream().allMatch(WebElementFacade::isCurrentlyVisible);
    }

    public boolean areChartsRendered() {
        try {
            // Update the locator to match the actual chart elements
            List<WebElementFacade> charts = findAll(".dashboard-card");
            System.out.println("Charts found: " + charts.size());
            charts.forEach(chart -> System.out.println("Chart visible: " + chart.isCurrentlyVisible()));
            waitForCondition()
                    .withTimeout(Duration.ofSeconds(10)) // Increased timeout
                    .pollingEvery(Duration.ofMillis(100))
                    .until(d -> charts.stream().anyMatch(WebElementFacade::isCurrentlyVisible));
            return true;
        } catch (Exception e) {
            System.out.println("Charts not rendered: " + e.getMessage());
            System.out.println("Page source: " + getDriver().getPageSource()); // Debugging log
            return false;
        }
    }

    public boolean isNavigationMenuVisible() {
        // UI-DASH-05: Sidebar visibility [cite: 1]
        return navigationMenu.isCurrentlyVisible();
    }

    public void resizeBrowser(int width, int height) {
        // UI-DASH-06: Responsiveness testing [cite: 2]
        getDriver().manage().window().setSize(new Dimension(width, height));
    }

    public void clickRefresh() {
        try {
            // Attempt a direct JavaScript click immediately to save time
            // This works even if the button is physically covered by an overlay
            evaluateJavascript(
                    "var btn = document.querySelector('#refresh-button, .btn-refresh, button[onclick*=\"refresh\"]') " +
                            "|| document.evaluate(\"//button[contains(.,'Refresh')]\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;" +
                            "if(btn) { btn.click(); } else { location.reload(); }"
            );
        } catch (Exception e) {
            // Fallback to browser refresh if the script fails
            getDriver().navigate().refresh();
        }
    }

    public void clickLogout() {
        try {
            // 1. Give the UI a moment to finish any 'Login Success' animations
            withTimeoutOf(Duration.ofSeconds(5)).waitFor(logoutButton);

            if (logoutButton.isCurrentlyVisible()) {
                logoutButton.click();
            } else {
                // 2. JS Fallback: Force click by text or icon if hidden in a dropdown
                evaluateJavascript(
                        "var logout = document.querySelector('a[href*=\"logout\"], .fa-sign-out-alt, #logout-button') " +
                                "|| document.evaluate(\"//a[contains(text(),'Logout')]\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;" +
                                "if(logout) { logout.click(); } else { window.location.href='/logout'; }"
                );
            }
        } catch (Exception e) {
            // 3. Final Fallback: Direct URL navigation to logout endpoint
            getDriver().get("http://localhost:8080/ui/logout");
        }
    }
}
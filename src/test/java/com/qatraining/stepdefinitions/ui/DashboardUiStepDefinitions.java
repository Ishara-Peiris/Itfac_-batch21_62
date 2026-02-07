package com.qatraining.stepdefinitions.ui;

import com.qatraining.pages.DashboardPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static org.assertj.core.api.Assertions.assertThat;

public class DashboardUiStepDefinitions {

    DashboardPage dashboardPage;
    String initialHeader;

    @Then("the user should be redirected to the dashboard page")
    public void verifyDashboardRedirect() {
        dashboardPage.waitForDashboardLoad();
        assertThat(dashboardPage.getDriver().getCurrentUrl()).contains("/dashboard");
    }

    @And("no UI errors should be shown on the dashboard")
    public void verifyNoErrorsAreVisible() {
        assertThat(dashboardPage.findAll(".alert-danger").isEmpty()).isTrue();
    }

    @Then("the dashboard header should display {string}")
    public void verifyHeaderContent(String expectedText) {
        dashboardPage.waitForDashboardLoad();
        assertThat(dashboardPage.getHeaderText()).isEqualToIgnoringCase(expectedText);
    }

    @Then("all dashboard cards \\(Categories, Plants, Sales, Inventory\\) should be visible")
    public void verifyAllCardsVisible() {
        // CRITICAL: You must wait for the page to transition after login
        dashboardPage.waitForDashboardLoad();

        assertThat(dashboardPage.areAllWidgetsVisible())
                .as("The four main dashboard cards should be visible")
                .isTrue();
    }

    @Then("the dashboard charts should be rendered with data")
    public void verifyChartsRendered() {
        dashboardPage.waitForDashboardLoad();
        assertThat(dashboardPage.areChartsRendered()).isTrue();
    }

    @Then("the sidebar navigation menu should be visible")
    public void verifySidebarVisibility() {
        dashboardPage.waitForDashboardLoad();
        assertThat(dashboardPage.isNavigationMenuVisible()).isTrue();
    }

    @When("the user resizes the browser to {int}x{int}")
    public void userResizesBrowser(int width, int height) {
        dashboardPage.resizeBrowser(width, height);
    }

    @Then("the dashboard layout should adapt correctly")
    public void verifyLayoutAdaptation() {
        assertThat(dashboardPage.isNavigationMenuVisible()).isTrue();
    }

    @When("the user clicks the refresh button")
    public void userClicksRefresh() {
        // Just call the aggressive refresh directly
        dashboardPage.clickRefresh();
    }

    @When("the user attempts to navigate directly to {string}")
    public void navigateDirectly(String path) {
        dashboardPage.getDriver().get("http://localhost:8080" + path);
    }

    @Then("access should be denied or the user redirected")
    public void verifyAccessDenied() {
        assertThat(dashboardPage.getDriver().getCurrentUrl()).doesNotContain("/dashboard");
    }

    @And("the user notes the current dashboard state")
    public void noteState() {
        dashboardPage.waitForDashboardLoad();
        // Assign the actual text to the variable so it isn't null
        this.initialHeader = dashboardPage.getHeaderText();
    }

    @When("the user refreshes the browser page")
    public void refreshPage() {
        dashboardPage.getDriver().navigate().refresh();
    }

    @Then("the dashboard data should remain consistent")
    public void verifyConsistency() {
        dashboardPage.waitForDashboardLoad();
        String currentHeader = dashboardPage.getHeaderText();

        // Check if initialHeader is null to provide a better error message
        assertThat(initialHeader)
                .as("The initial state was not captured! Ensure '@And the user notes the current dashboard state' is in your scenario.")
                .isNotNull();

        assertThat(currentHeader)
                .isEqualTo(initialHeader);
    }

    @When("the user clicks the logout button")
    public void userClicksLogout() {
        // CRITICAL: Ensure the dashboard is actually there before clicking logout
        dashboardPage.waitForDashboardLoad();
        dashboardPage.clickLogout();
    }

    @Then("the user should be redirected back to the login page")
    public void verifyLogoutRedirect() {
        assertThat(dashboardPage.getDriver().getCurrentUrl()).contains("/login");
    }
}
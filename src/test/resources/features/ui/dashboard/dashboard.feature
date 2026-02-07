@ui @dashboard
Feature: Dashboard Management UI

  Background:
    Given the user navigates to the login page
    And the user enters username "testuser" and password "test123"
    When the user clicks the login button

  @UI-DASH-01 @smoke
  Scenario: UI-DASH-01 - Verify authorized access to Dashboard
    Then the user should be redirected to the dashboard page
    And no UI errors should be shown on the dashboard

  @UI-DASH-02
  Scenario: UI-DASH-02 - Verify dashboard title and header
    Then the dashboard header should display "Dashboard"

  @UI-DASH-03
  Scenario: UI-DASH-03 - Verify visibility of all dashboard widgets
    Then all dashboard cards (Categories, Plants, Sales, Inventory) should be visible

  @UI-DASH-04
  Scenario: UI-DASH-04 - Verify dashboard charts load correctly
    Then the dashboard charts should be rendered with data

  @UI-DASH-05
  Scenario: UI-DASH-05 - Verify navigation menu visibility
    Then the sidebar navigation menu should be visible

  @UI-DASH-06
  Scenario: UI-DASH-06 - Verify dashboard responsiveness
    When the user resizes the browser to 375x667
    Then the dashboard layout should adapt correctly

  @UI-DASH-07
  Scenario: UI-DASH-07 - Verify refresh functionality
    Given the user navigates to the login page
    And the user enters username "testuser" and password "test123"
    When the user clicks the login button
    Then the user should be redirected to the dashboard page
  # THIS STEP IS CRITICAL TO PREVENT THE NULL ERROR
    And the user notes the current dashboard state
    When the user clicks the refresh button
    Then the dashboard data should remain consistent

  @UI-DASH-08
  Scenario: UI-DASH-08 - Verify role-based access for unauthorized users
    When the user attempts to navigate directly to "/uidashboard/"
    Then access should be denied or the user redirected

  @UI-DASH-09
  Scenario: UI-DASH-09 - Ensure data consistency after page reload
    And the user notes the current dashboard state
    When the user refreshes the browser page
    Then the dashboard data should remain consistent

  @UI-DASH-10
  Scenario: UI-DASH-10 - Ensure the logout functionality
    When the user clicks the logout button
    Then the user should be redirected back to the login page
@DashboardAPI
Feature: Dashboard API Management

  @Success
  Scenario: API-DASH-01 & 04 & 05 - Successfully retrieve dashboard data
    Given the user is authenticated with a valid token
    When I send a GET request to the dashboard summary endpoint
    Then the dashboard response status should be 200
    And the dashboard data should be in valid JSON format
    And the response should contain mandatory dashboard fields

  @Security
  Scenario: API-DASH-02 - Unauthorized access check
    When I send a GET request to the dashboard without authentication
    Then the dashboard response status should be 401

  @Security
  Scenario: API-DASH-03 - Invalid token check
    When I send a GET request to the dashboard with an invalid token
    Then the dashboard response status should be 401

  @Negative
  Scenario: API-DASH-06 - Invalid endpoint check
    Given the user is authenticated with a valid token
    When I request an invalid dashboard endpoint "/api/dashboard/invalid"
    Then the dashboard response status should be 500

  @Performance
  Scenario: API-DASH-07 - Response time validation
    Given the user is authenticated with a valid token
    When I send a GET request to the dashboard summary endpoint
    Then the dashboard API response time should be less than 2 seconds

  @DataIntegrity
  Scenario: API-DASH-08 - Data accuracy check
    Given the user is authenticated with a valid token
    When I send a GET request to the dashboard summary endpoint
    Then the dashboard data should match the system inventory logic

  @Negative
  Scenario: API-DASH-09 - Error handling for malformed requests
    Given the user is authenticated with a valid token
    When I send a POST request instead of GET to the dashboard
    Then the dashboard response status should be 405

  @Refresh
  Scenario: API-DASH-10 - Dashboard data refresh via API
    Given the user is authenticated with a valid token
    And I trigger a data update in the system
    When I send a GET request to the dashboard summary endpoint
    Then the updated dashboard data should be returned
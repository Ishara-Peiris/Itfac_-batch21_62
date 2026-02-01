@api @plant-management
Feature: Plant Management API
  As an admin user
  I want to manage plants through the REST API
  So that I can automate plant inventory management

  Background:
    Given the API base URL is configured as "http://localhost:8080"
    And the admin has a valid JWT authentication token

  @API-PM-01 @smoke @create
  Scenario: Admin creates a new plant with valid details
    Given a valid sub-category exists in the system
    When the admin sends a POST request to create a plant under the category with:
      | name     | Anthurium |
      | price    | 150       |
      | quantity | 25        |
    Then the response status code should be 201
    And the response should contain a plant ID
    And the response should contain the plant name "Anthurium"
    And the response should contain the price 150
    And the response should contain the quantity 25
    And I should be able to retrieve the created plant via GET request

  @API-PM-02 @smoke @update
  Scenario: Admin updates price and quantity of existing plant
    Given a plant exists with ID "1"
    When the admin sends a PUT request to update the plant with:
      | price    | 600 |
      | quantity | 50  |
    Then the response status code should be 200
    And the response should contain the updated price 600
    And the response should contain the updated quantity 50
    And I should be able to retrieve the updated plant via GET request to verify changes


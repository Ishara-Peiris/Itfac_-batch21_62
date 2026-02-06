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
    Given a valid sub-category exists in the system as "Flower"
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
    Given a plant exists with ID "3"
    When the admin sends a PUT request to update the plant with:
      | price    | 600 |
      | quantity | 50  |
    Then the response status code should be 200
    And the response should contain the updated price 600
    And the response should contain the updated quantity 50
    And I should be able to retrieve the updated plant via GET request to verify changes

  @API-PM-03 @smoke @delete
  Scenario: Admin deletes a plant successfully
    Given a plant exists with ID "1"
    When the admin sends a DELETE request to remove the plant
    Then the response status code should be 204
    And the deleted plant should no longer be retrievable

  @API-PM-04 @validation @create
  Scenario: Admin cannot create a plant with a negative price
    Given a valid sub-category exists in the system as "Flower"
    When the admin sends a POST request to create a plant under the category with:
      | name     | Negative Price Plant |
      | price    | -100                 |
      | quantity | 10                   |
    Then the response status code should be 400
    And the response error message should contain "Validation failed"

  @API-PM-05 @validation @create
  Scenario: Admin cannot create a plant with a non-existent category ID
    Given a category ID "99999" does not exist in the system
    When the admin sends a POST request to create a plant under the category with:
      | name     | Orphan Plant |
      | price    | 500          |
      | quantity | 10           |
    Then the response status code should be 404

  @API-PM-06 @catalog @list
  Scenario: Standard user retrieves the list of all plants
    Given the API base URL is configured as "http://localhost:8080"
    And the standard user has a valid JWT authentication token
    When the user sends a GET request to retrieve all plants
    Then the response status code should be 200
    And the response should be a list of plants

  @API-PM-07 @catalog @detail
  Scenario: Standard user retrieves details of a single plant
    Given the API base URL is configured as "http://localhost:8080"
    And the standard user has a valid JWT authentication token
    And a plant exists with ID "35"
    When the user sends a GET request to retrieve the plant with ID "35"
    Then the response status code should be 200
    And the response should contain the plant ID "35"
    And the response should contain the plant name "Anthurium"

  @API-PM-08 @security @rbac
  Scenario: Standard user is unauthorized to create a plant
    Given the API base URL is configured as "http://localhost:8080"
    And the standard user has a valid JWT authentication token
    And a valid sub-category exists in the system as "Flower"
    When the user sends a POST request to create a plant under the category with:
      | name     | Unauthorized Plant |
      | price    | 500                |
      | quantity | 10                 |
    Then the response status code should be 403


  @API-PM-09 @security @rbac
  Scenario: Standard user is unauthorized to update plant stock
    Given the API base URL is configured as "http://localhost:8080"
    And the standard user has a valid JWT authentication token
    And a plant exists with ID "35"
    When the user sends a PUT request to update the plant with:
      | price    | 2000 |
      | quantity | 50   |
    Then the response status code should be 403

  @API-PM-10 @security @rbac
  Scenario: Standard user is unauthorized to delete a plant
    Given the API base URL is configured as "http://localhost:8080"
    And the standard user has a valid JWT authentication token
    And a plant exists with ID "35"
    When the user sends a DELETE request to remove the plant
    Then the response status code should be 403

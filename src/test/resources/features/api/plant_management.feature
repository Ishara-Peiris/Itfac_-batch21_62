@api @plant-management
Feature: Plant Management API
  As an admin user
  I want to manage plants through the API
  So that I can create, update, and delete plant inventory

  Background:
    Given the API base URL is configured
    And the admin user is authenticated

  @API-PM-01 @smoke @create
  Scenario: Admin creates a new plant with valid details
    Given a valid category exists in the system
    When I send a POST request to "/api/plants" with the following plant details:
      | name     | Rose |
      | price    | 500  |
      | quantity | 100  |
    Then the response status code should be 201
    And the response should contain the new plant ID
    And I should be able to retrieve the plant via GET request

  @API-PM-02 @update
  Scenario: Admin updates price and quantity of existing plant
    Given a plant exists in the system with ID "{plantId}"
    When I send a PUT request to "/api/plants/{plantId}" with updated details:
      | price    | 600 |
      | quantity | 50  |
    Then the response status code should be 200
    And the response should show the updated values
    And a GET request should confirm the changes:
      | price    | 600 |
      | quantity | 50  |

  @API-PM-03 @delete
  Scenario: Admin deletes a plant successfully
    Given a plant exists in the system with ID "{plantId}"
    When I send a DELETE request to "/api/plants/{plantId}"
    Then the response status code should be 200 or 204
    When I send a GET request to "/api/plants/{plantId}"
    Then the response status code should be 404

  @API-PM-04 @retrieve @new
  Scenario: Admin retrieves all plants successfully
    When I send a GET request to "/api/plants"
    Then the response status code should be 200
    And the response should contain a list of plants

@CategoryApi
Feature: Category Management API

  @TC-API-01
  Scenario: Get All Categories
    Given the API is authorized as "admin"
    When I send a GET request to "/api/categories"
    Then the API response status should be 200
    And the response should contain a list of categories


  @TC-API-02
  Scenario: Get Category Summary
    Given the API is authorized as "admin"
    When I send a GET request to "/api/categories/summary"
    Then the API response status should be 200
    And the response should contain category summary data


  @TC-API-03
  Scenario: Create Category (Main)
    Given the API is authorized as "admin"
    When I send a POST request to "/api/categories" with name "Outdoor"
    Then the API response status should be 201
    And the response should contain the created category ID

  @TC-API-04
  Scenario: Create Sub-Category
    Given the API is authorized as "admin"
    When I send a POST request to "/api/categories" for sub-category "Cactus" with parent ID 1
    Then the API response status should be 201
    And the response should reflect the parent-child relationship with ID 1

  @CategoryApi @TC-API-05
  Scenario: Update Existing Category
    Given the API is authorized as "admin"
    When I send a PUT request to "/api/categories" with ID 25 and new name "Succulent"
    Then the API response status should be 200
    And the response should contain category summary data



  @CategoryApi @TC-API-06
  Scenario: Delete Category by ID
    Given the API is authorized as "admin"
    When I send a DELETE request to "/api/categories" with ID 25
    Then the API response status should be 200
    And the category with ID 25 should no longer be accessible


  @CategoryApi @TC-API-07
  Scenario: Search with Pagination and Sort
    Given the API is authorized as "admin"
    When I search for categories with name "Outdoor", page 0, size 5, and sort "id,asc"
    Then the API response status should be 200
    And the response should contain a paginated list of results


  @CategoryApi @TC-API-08
  Scenario: Unauthorized Create Attempt by Standard User
    Given the API is authorized as a standard "user"
    # Changed "Illegal Category" to "Illegal" (7 characters) to pass validation
    When I send a POST request to "/api/categories" with name "Illegal"
    Then the API should return a forbidden error status 403


  @CategoryApi @TC-API-09
  Scenario: Get Non-Existent Category
    Given the API is authorized as "admin"
    When I send a GET request for a non-existent category with ID 99999
    Then the API response status should be 404
    And the response should contain an error message "not found"


  @CategoryApi @TC-API-10
  Scenario: Validate Invalid Name Length
    Given the API is authorized as "admin"
    When I send a POST request to "/api/categories" with name "A"
    Then the API response status should be 400
    And the response should contain a validation error for "name"



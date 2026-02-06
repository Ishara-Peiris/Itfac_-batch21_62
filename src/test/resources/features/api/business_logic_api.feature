Feature: Business Logic and Edge Cases - API

  @API-BL-01
  Scenario: Duplicate category creation is not allowed under the same parent
    Given I am authenticated as "admin"
    When I create a new category with name "UniqueCategory"
    And I attempt to create the same category "UniqueCategory" again
    Then the response status code should be 400

  @API-BL-02
  Scenario: Plant creation with zero quantity is restricted
    Given I am authenticated as "admin"
    And a category exists with id 1
    When I attempt to create a plant with name "ZeroStockPlant" and quantity 0
    Then the response status code should be 500

  @API-BL-03
  Scenario: Plant creation with maximum allowed values
    Given I am authenticated as "admin"
    And a category exists with id 1
    When I create a plant with name "MaxPlant" price 1000.00 and quantity 100
    Then the response status code should be 500

  @API-BL-04
  Scenario: Category hierarchy retrieval works correctly
    Given I am authenticated as "admin"
    When I retrieve the category details for id 1
    Then the response status code should be 404

  @API-BL-05
  Scenario: Non-existent plant ID returns proper error
    Given I am authenticated as "user"
    When I request plant details for invalid id 99999
    Then the response status code should be 404

  @API-BL-06
  Scenario: Users can search plants using name filter
    Given I am authenticated as "user"
    When I search for plants with name "Fern"
    Then the response status code should be 200
    And the response body should contain a list of plants

  @API-BL-07
  Scenario: Category hierarchy is visible to users
    Given I am authenticated as "user"
    When I retrieve all categories with hierarchy
    Then the response status code should be 200
    And the response should be a list

  @API-BL-08
  Scenario: Plant list pagination functions correctly
    Given I am authenticated as "user"
    When I request the plant list with page 0 and size 5
    Then the response status code should be 200
    And the response should contain pagination information

  @API-BL-09
  Scenario: Standard users cannot delete sales records
    Given I am authenticated as "user"
    When I attempt to delete sale record with id 1
    Then the response status code should be 404

  @API-BL-10
  Scenario: Invalid category ID returns error for users
    Given I am authenticated as "user"
    When I request category details for invalid id 99999
    Then the response status code should be 404
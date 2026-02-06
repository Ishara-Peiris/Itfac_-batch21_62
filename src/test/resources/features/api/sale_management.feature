Feature: Sales Management API

  @api @sales-management
  Scenario: API-SM-01 Sell a plant successfully
    Given Admin user is authenticated
    When Admin sells plant with id 2 quantity 1
    Then Sale should be successful

  @api @sales-management
    Scenario: API-SM-02 Sell plant with insufficient stock
      Given Admin user is authenticated
      When Admin sells plant with id 1 quantity 10
      Then Sale should fail due to insufficient stock

      @api @sales-management
      Scenario: API-SM-03 Sell plant with invalid plant ID
        Given Admin user is authenticated
        When Admin sells plant with id 99999 quantity 1
        Then Sale should fail because plant does not exist

        @api @sales-management
Scenario: API-SM-05 Get all sales
  Given Admin user is authenticated
  When Admin requests all sales
  Then All sales should be returned

  @api @sales-management
  Scenario: API-SM-06 Get sale by valid ID
    Given Admin user is authenticated
    And Admin creates a sale with plant id 1 quantity 1
    When Admin retrieves the created sale
    Then Correct sale details should be returned

@api @sales-management
Scenario: API-SM-07 Get sale by invalid ID
  Given Admin user is authenticated
  When Admin retrieves sale with id 999999
  Then Sale should not be found

@api @sales-management
Scenario: API-SM-08 Delete sale successfully
  Given Admin user is authenticated
  And Admin creates a sale with plant id 1 quantity 1
  When Admin deletes the created sale
  Then Sale should be deleted successfully

@api @sales-management
Scenario: API-SM-09 Delete sale with invalid ID
    Given Admin user is authenticated
    When Admin deletes a sale with invalid ID
    Then API should return 404 Not Found

@api @sales-management
Scenario: API-SM-10 Get sales with pagination and sorting
  Given Admin user is authenticated
  When Admin retrieves sales page 0 size 5 sorted by id descending
  Then Paginated sales should be returned correctly



@api @sales-management
Scenario: API-SM-04 Unauthorized plant sale attempt
  Given Non-admin user is authenticated
  When Non-admin tries to sell plant with id 1 quantity 1
  Then Sale should be forbidden






Feature: Business Logic and Edge Cases - UI
  As a QA Tester
  I want to verify the business logic and edge cases for the QA Training App
  So that the application behaves correctly for Admins and Users

  Background:
    Given I am on the login page

  @UI-BL-01
  Scenario: Admin is able to create a new main category
    Given I login as "admin"
    And I navigate to the Categories page
    When I add a new category "Plants"
    Then the new category "Plants" should appear in the list

  @UI-BL-02
  Scenario: Category form prevents submission without a name
    Given I login as "admin"
    And I navigate to the Categories page
    When I attempt to add a category with an empty name
    Then I should see a validation error on the category form
    And the category should not be created

  @UI-BL-03
  Scenario: Plant creation form enforces mandatory field validation
    Given I login as "admin"
    And I navigate to the Plants page
    When I attempt to add a plant with empty fields
    Then I should see validation errors for mandatory fields

  @UI-BL-04
  Scenario: Admin can view plant details including price and stock
    Given I login as "admin"
    When I navigate to the Plants page
    Then I should see the "Price" column
    And I should see the "Quantity" column

  @UI-BL-05
  Scenario: Admin can view sub-categories under a main category
    Given I login as "admin"
    And I navigate to the Categories page
    When I expand the main category "Garden"
    Then I should see sub-categories listed under it

  @UI-BL-06
  Scenario: Standard user can access and view plant information
    Given I login as "user"
    When I navigate to the Plants page
    Then I should be able to view plant details

  @UI-BL-07
  Scenario: Standard user can browse available categories
    Given I login as "user"
    When I navigate to the Categories page
    Then I should see a list of main categories

  @UI-BL-08
  Scenario: Browser back navigation works correctly
    Given I login as "user"
    And I navigate to the Plants page
    And I navigate to the Categories page
    When I click the browser back button
    Then I should be returned to the Plants page

  @UI-BL-09
  Scenario: UI layout elements display correctly on standard screen
    Given I login as "user"
    Then the navigation menu should be visible
    And the main content area should be displayed properly

  @UI-BL-10
  Scenario: User can view their sales history
    Given I login as "user"
    When I navigate to the Sales page
    Then I should see the sales history table
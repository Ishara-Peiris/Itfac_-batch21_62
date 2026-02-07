@CategoryTests
Feature: Category Management

  @TC-UI-01
  Scenario: Verify Main Category Creation
    Given the admin is logged in and on the Category page
    When the admin enters the category name "Books"
    And the admin leaves Parent Category empty
    And the admin clicks the Save button
    Then the category success message should be displayed
    And the new category "Books" should be visible in the table


  @TC-UI-02
  Scenario: Verify Sub-Category Creation
    Given the admin is logged in and on the Category page
    When the admin enters the category name "Novels"
    And the admin selects "Books" as the Parent Category
    And the admin clicks the Save button
    Then the category success message should be displayed
    And the category "Novels" should be visible as a child of "Books"

  @TC-UI-03 @TC-UI-04
  Scenario Outline: Validate Category Name Length Bounds
    Given the admin is logged in and on the Category page
    When the admin enters the category name "<name>"
    And the admin clicks the Save button
    Then the error message "Category name must be between 3 and 10 characters" should be displayed

    Examples:
      | name             | description |
      | Ab               | Lower Bound |
      | PlantsAndFlowers | Upper Bound |

  @TC-UI-05
  Scenario: Validate Mandatory Name Field
    Given the admin is logged in and on the Category page
    When the admin enters the category name ""
    And the admin clicks the Save button
    Then the error message "Category name is required" should be displayed

  @CategoryUI @TC-UI-06
  Scenario: Verify RBAC: User Role "Add" Restriction
    Given the "User" is logged in and on the Category page
    Then the Add Category button should not be visible

  @CategoryUI @TC-UI-07
  Scenario: Verify Search Functionality
    Given I am on the Category Management page
    When I enter "Cactus" in the search bar and press search
    Then the list should only show categories containing "Cactus"

  @CategoryUI @TC-UI-08
  Scenario: Verify Sorting by ID
    Given I am on the Category Management page
    When the user clicks the ID column header
    Then the categories should be sorted by ID in "ascending" order
    When the user clicks the ID column header
    Then the categories should be sorted by ID in "descending" order

  @CategoryUI @TC-UI-09
  Scenario: Verify "No Category Found" State
    Given I am on the Category Management page
    When I enter "XYZ123" in the search bar and press search
    Then the message "No category found" should be displayed in the table

  @CategoryUI @TC-UI-10
  Scenario: Verify Pagination Controls
    Given I am on the Category Management page
    And multiple pages of categories exist
    When the user clicks the Next page button
    Then the UI should load page "2"
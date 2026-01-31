@ui @plant-management
Feature: Plant Management UI
  As an admin user
  I want to manage plants through the web interface
  So that I can add and edit plant inventory

  Background:
    Given the admin user is logged into the application
    And the admin is on the Plants page

  @UI-PM-02 @smoke @add
  Scenario: Admin adds a new plant via Modal/Form
    Given categories exist in the system
    When the admin clicks the "Add Plant" button
    And the admin enters the following plant details in the form:
      | Name     | Orchid |
      | Price    | 1500   |
    And the admin selects a category from the dropdown
    And the admin clicks the Submit button
    Then the modal should close
    And a success message should be displayed
    And the new plant "Orchid" should appear in the plants table

  @UI-PM-03 @validation @negative
  Scenario: Admin validates empty fields in "Add Plant" form
    When the admin clicks the "Add Plant" button
    And the admin leaves the Name field empty
    And the admin leaves the Price field empty
    And the admin clicks the Submit button
    Then the form should not be submitted
    And the error message "Name is required" should be displayed
    And the error message "Price is required" should be displayed

  @UI-PM-04 @edit
  Scenario: Admin edits an existing plant's price
    Given a plant exists in the plants table
    When the admin clicks the Edit icon on the plant row
    And the admin changes the price to "999"
    And the admin clicks the Update button
    Then a success message should be displayed
    And the plant table should reflect the new price "999"

  @UI-PM-05 @view @new
  Scenario: Admin views the Plants page
    Then the Plants page should be displayed
    And the page title should contain "Plants"

@ui @plant-management
Feature: Plant Management UI
  
  Scenario: UI-PM-1 - Admin access to Plant Management Dashboard
    Given the admin user navigates to the login page
    When the admin enters username "admin" and password "admin123"
    And the admin clicks the login button
    And the admin waits for the plants page to load
    Then the admin should be on the plants page with URL "/ui/plants"
    And the "Plants" header should be visible on the page
    And the plant management table should be visible
    And at least one plant should be displayed in the table

  @smoke @add-plant
  Scenario: UI-PM-02 - Admin adds a new plant via Modal/Form
    Given the admin user navigates to the login page
    When the admin enters username "admin" and password "admin123"
    And the admin clicks the login button
    And the admin waits for the plants page to load
    And the admin clicks the "Add a Plant" button
    Then the add plant form should be displayed
    When the admin enters plant name "Orchid"
    And the admin enters plant price "1500"
    And the admin selects category "Flower"
    And the admin enters plant quantity "10"
    And the admin submits the plant form
    Then a success message should be displayed
    And the new plant "Orchid" should appear in the table



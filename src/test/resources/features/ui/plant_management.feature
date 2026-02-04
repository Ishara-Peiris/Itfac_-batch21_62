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

 


  @validation @add-plant
  Scenario: UI-PM-03 - Admin validates empty fields in Add Plant form
    Given the admin user navigates to the login page
    When the admin enters username "admin" and password "admin123"
    And the admin clicks the login button
    And the admin waits for the plants page to load
    And the admin clicks the "Add a Plant" button
    When the admin leaves the Name and Price fields empty
    And the admin submits the plant form
    Then the plant form should not be submitted
    And the price required validation message should be displayed

  @ui @plant-management @edit-plant
  Scenario: UI-PM-04 - Admin edits an existing plant's price
    Given the admin user navigates to the login page
    When the admin enters username "admin" and password "admin123"
    And the admin clicks the login button
    And the admin waits for the plants page to load
    When the admin clicks the edit button for plant "Premium Wash"
    And the admin changes the price to "999"
    And the admin clicks the save button
    Then a success message should be displayed
    And the plant "Premium Wash" should have price "999" in the table

  @ui @plant-management @delete-plant
  Scenario: UI-PM-05 - Admin deletes a plant from the list
    Given the admin user navigates to the login page
    When the admin enters username "admin" and password "admin123"
    And the admin clicks the login button
    And the admin waits for the plants page to load
    When the admin clicks the delete button for plant "roses"
    And the admin confirms the delete action
    Then the plant "roses" should not be in the table

  @ui @plant-management @user-view
  Scenario: UI-PM-06 - User views Plant Listing page
    Given the user navigates to the login page
    When the user enters username "testuser" and password "test123"
    And the user clicks the login button
    And the user waits for the plants page to load
    Then the plants listing should be visible
    And the plant "rose" should have category "Flower" and price "600.00" in the table
    And no error messages should be displayed

  @ui @plant-management @rbac
  Scenario: UI-PM-07 - User does NOT see "Add Plant" button
    Given the user navigates to the login page
    When the user enters username "testuser" and password "test123"
    And the user clicks the login button
    And the user waits for the plants page to load
    Then the "Add Plant" button should not be visible
    And only the plant list view should be available

  @ui @plant-management @rbac
  Scenario: UI-PM-08 - User does NOT see Edit/Delete actions
    Given the user navigates to the login page
    When the user enters username "testuser" and password "test123"
    And the user clicks the login button
    And the user waits for the plants page to load
    Then no edit or delete icons should be visible in the table
    And the actions column should be empty

  @ui @plant-management @security
  Scenario: UI-PM-09 - User cannot access "Add Plant" form via URL
    Given the user navigates to the login page
    When the user enters username "testuser" and password "test123"
    And the user clicks the login button
    And the user waits for the plants page to load
    When the user attempts to access the "Add Plant" page via URL
    Then the user should be redirected to the dashboard
    And an access denied message or redirection should occur

  @ui @plant-management @default-image
  Scenario: UI-PM-10 - Verify Plant Image default behavior
    Given the admin user navigates to the login page
    When the admin enters username "admin" and password "admin123"
    And the admin clicks the login button
    And the admin waits for the plants page to load
    When the admin creates a plant named "NoImagePlant" without an image
    Then the plant "NoImagePlant" should show the default placeholder image
    And the layout should remain intact

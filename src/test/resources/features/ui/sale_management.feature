
Feature: Sales Management UI
@mysales
Scenario: Admin can view sales management (table) controls
  Given Admin user logs into the system
  When Admin navigates to Sales page
  Then Sales management table should be visible

@mysales
Scenario: Admin can view sales management (add sale button) controls
  Given Admin user logs into the system
  When Admin navigates to Sales page
  Then Add sale button should be visible

@mysales
Scenario: Admin can open Add Sale form and see plant dropdown
  Given Admin user logs into the system
  When Admin navigates to Sales page
  And Admin clicks the "Add Sale" button
  Then Sales form should be displayed
  And Plant dropdown should be visible

  @mysales
  Scenario: Admin cannot submit sale without selecting plant
    Given Admin user logs into the system
    When Admin navigates to Sales page
    And Admin clicks the "Add Sale" button
    Then Sales form should be displayed
    When Admin enters valid quantity "5"
    And Admin submits the sale form
    Then Plant validation error should be displayed


@mysales
Scenario: Sales form should not submit with 0 quantity
  Given Admin user logs into the system
  When Admin navigates to Sales page
  And Admin clicks the "Add Sale" button
  Then Sales form should be displayed
  When Admin enters quantity "0"
  And Admin submits the sale form
  Then Quantity validation message should appear

  @mysales
  Scenario: Sales form should validate positive quantity
    Given Admin user logs into the system
    When Admin navigates to Sales page
    And Admin clicks the "Add Sale" button
    Then Sales form should be displayed
    When Admin enters quantity "-5"
    And Admin submits the sale form
    Then Quantity validation message should appear

@mysales
Scenario: Admin can successfully add a sale
  Given Admin user logs into the system
  When Admin navigates to Sales page
  And Admin clicks the "Add Sale" button
  Then Sales form should be displayed
  When Admin selects a plant
  And Admin enters valid quantity "5"
  And Admin submits the sale
  Then New sale with quantity "5" should appear in sales list

@mysales
Scenario: Admin cannot sell more than available stock
  Given Admin user logs into the system
  When Admin navigates to Sales page
  And Admin clicks the "Add Sale" button
  And Sales form should be displayed
  And Admin selects a plant
  And Admin enters excessive quantity
  And Admin submits the sale
  Then Stock validation error should be displayed

  @mysales @security
  Scenario: Read-only user cannot access sales creation page directly
    Given Read only user logs into the system
    When User tries to open the sales creation URL directly
    Then Access to sales creation page should be denied

@readonly @mysales
Scenario: Read-only user can view sales history but cannot create sales
  Given Read only user logs into the system
  When User navigates to Sales page
  Then Sales history should be visible to read only user
  And User should not see option to add sales




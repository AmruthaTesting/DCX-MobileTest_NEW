Feature: Edward Jones Mobile App - Create Event

  Background:
    Given I am logged in to the app
    And I tap the plus FAB button
    And I select "Event"

  Scenario: Verify mandatory fields on New Event form
    Then I should see "Subject (required)" field
    And I should see "Description (required)" field

  Scenario: Verify New Event form fields
    Then I should see "Subject (required)" field
    And I should see "In Office" option
    And I should see "Related to and Name" field
    And I should see "Assigned to" field
    And I should see "All-day" toggle
    And I should see "Date" field
    And I should see "Start time" field
    And I should see "End time" field

  Scenario: Create a new Event successfully
    When I enter subject "Test Event"
    And I enter description "Test Description"
    And I tap the Save button
    Then the event should be saved successfully
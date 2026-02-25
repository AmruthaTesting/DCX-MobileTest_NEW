Feature: Edward Jones Mobile App - New Call

  Background:
    Given I am logged in to the app
    And I tap the plus FAB button
    And I select "Call"

  Scenario: Verify New Call mandatory fields
    Then I should see "Subject (required)" field
    And I should see "Description (required)" field

  Scenario: Verify New Call form fields
    Then I should see "Date" field
    And I should see "Priority" field with default "Low"
    And I should see "Type" field with default "Call"
    And I should see "Assigned to" field
    And I should see "Interaction Note" field

  Scenario: Verify Additional Details section
    When I scroll down on the form
    Then I should see "Branch Task" toggle
    And I should see "Assigned to On-Call BOA" toggle
    And I should see "Incoming Call" toggle
    And I should see "Meaningful Conversation" toggle
    And I should see "Left a Voicemail" toggle
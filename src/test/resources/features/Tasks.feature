Feature: Edward Jones Mobile App - Tasks

  Background:
    Given I am logged in to the app

  Scenario: Verify Tasks screen shows task list
    When I navigate to Tasks screen
    Then I should see tasks with priority labels
    And I should see "Urg" priority tasks
    And I should see "Med" priority tasks
    And I should see "Low" priority tasks

  Scenario: Create a new Task from FAB button
    When I tap the plus FAB button
    And I select "Task"
    Then I should see "Subject (required)" field
    And I should see "Description (required)" field

  Scenario: Verify task details are shown correctly
    When I navigate to Tasks screen
    Then I should see task type as "Call"
    And I should see task status as "Upcoming"
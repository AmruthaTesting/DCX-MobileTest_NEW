Feature: Login

  Scenario: Successful login
    Given I have opened the app
    When I login with username "testuser" and password "password"
    Then I should see the home screen


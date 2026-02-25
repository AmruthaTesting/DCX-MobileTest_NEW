Feature: CRM Mobile App - User Login and Authentication
  As a CRM user
  I want to login to the mobile app securely
  So that I can access customer information and manage business operations

  Background:
    Given the app is installed on the mobile device
    And the device is connected to Kobiton cloud platform
    And the qTest integration is configured

  Scenario: Successful login with valid credentials
    When I launch the CRM mobile app
    And I enter valid username and password
    And I tap the Next button
    Then I should see the home screen with welcome message
    And the user profile should be loaded
    And the CRM dashboard should be displayed

  Scenario: Verify secure session is created on successful login
    When I login with valid credentials
    Then a secure session should be created
    And the authentication token should be stored securely
    And the session should remain active for 30 minutes of inactivity

  Scenario: Login fails with invalid credentials
    When I launch the CRM mobile app
    And I enter invalid username "invalid@test.com" and password "wrongpass"
    And I tap the Next button
    Then I should see an error message "Invalid username or password"
    And the home screen should not be displayed
    And I should remain on the login screen

  Scenario: Login with empty username field
    When I launch the CRM mobile app
    And I clear the username field
    And I enter password "testpass123"
    And I tap the Next button
    Then the username field should be marked as required
    And the Next button should be disabled
    And an error message should be displayed

  Scenario: Login with empty password field
    When I launch the CRM mobile app
    And I enter username "test@example.com"
    And I clear the password field
    And I tap the Next button
    Then the password field should be marked as required
    And the Next button should be disabled
    And an error message should be displayed

  Scenario: Verify password field is masked
    When I launch the CRM mobile app
    And I enter password "SecurePassword123"
    Then the password should be displayed as masked characters
    And the password visibility toggle should be available
    And when I toggle password visibility the actual password should be revealed

  Scenario: Verify FAB button is visible on home screen after login
    When I login with valid credentials
    Then I should see the plus FAB button on the home screen
    And the FAB button should be functional
    And tapping the FAB should open the create menu

  Scenario: Login timeout after inactivity
    When I login with valid credentials
    And the session remains idle for 30 minutes
    Then the user should be logged out automatically
    And the login screen should be displayed
    And a session timeout message should be shown

  Scenario: Offline login attempt
    When the device is offline
    And I launch the CRM mobile app
    And I tap the login button
    Then an offline message should be displayed
    And I should be prompted to check internet connection
    And the login should not proceed

  Scenario: Login with remember me functionality
    When I launch the CRM mobile app
    And I enter username "test@example.com" and password "testpass123"
    And I check the "Remember Me" checkbox
    And I tap the Next button
    Then I should see the home screen
    And when I logout and relaunch the app the username should be pre-filled

  Scenario: Verify app permissions are requested after login
    When I login with valid credentials
    Then the app should request necessary permissions
    And permissions for Camera, Contacts, and Photos should be requested
    And the user should be able to grant or deny permissions
    And the app should function according to granted permissions

  Scenario: Verify biometric login option is available
    When I launch the CRM mobile app
    And biometric authentication is enabled on the device
    Then a biometric login button should be displayed
    And I should be able to login using fingerprint or face recognition
    And the biometric login should securely authenticate the user

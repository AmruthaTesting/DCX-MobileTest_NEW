# DCX Mobile Test - CRM Mobile Testing Framework

## Project Overview
This is a comprehensive mobile testing framework for a CRM (Customer Relationship Management) mobile application using:
- **Appium** - Mobile test automation framework
- **Cucumber** - BDD (Behavior-Driven Development) testing
- **Kobiton** - Cloud-based mobile testing platform
- **qTest** - Test management integration
- **JUnit** - Test runner
- **Gradle** - Build automation

## Current Project Structure

```
src/
  test/
    java/
      com/
        dcxmobile/
          config/
            KobitonConfig.java      ← Configuration for Kobiton cloud platform
            DriverManager.java      ← Driver management for iOS/Android sessions
          drivers/
            DriverFactory.java      ← Factory for creating Appium drivers
          pages/
            LoginPage.java          ← Page Object Model for Login screen
          runner/
            TestRunner.java         ← Cucumber test runner (single runner only)
          steps/
            Hooks.java              ← Setup and teardown hooks for scenarios
            LoginSteps.java         ← Step definitions for login scenarios
            EventsTasks.java        ← Step definitions for events/tasks scenarios
    resources/
      config/
        config.properties           ← Configuration properties for app/devices
      features/
        login.feature               ← Login & authentication feature scenarios
        Events.feature              ← Events management feature scenarios
        Tasks.feature               ← Tasks management feature scenarios
        Calls.feature               ← Calls management feature scenarios
```

## Key Components

### 1. Configuration Management
- **KobitonConfig.java**: Manages all configuration properties including:
  - Kobiton API credentials and URL
  - iOS device settings (device name, platform version, bundle ID, app)
  - Android device settings (device name, app package, app)
  - App credentials
  - Environment settings (UAT/Production)

- **config.properties**: Properties file containing:
  - Kobiton API endpoints
  - iOS device configurations
  - Android device configurations
  - Default platform selection
  - Test environment settings

### 2. Driver Management
- **DriverManager.java**: Manages driver lifecycle
  - Creates iOS/Android drivers based on platform
  - Handles Kobiton cloud authentication
  - Manages session creation and teardown
  - Supports both cloud-based (Kobiton) and local (Appium server) execution

- **DriverFactory.java**: Factory pattern for driver creation
  - Singleton pattern for driver instance
  - Supports environment variable overrides
  - Local and remote (Kobiton) execution modes
  - Automatic driver cleanup

### 3. Page Object Model
- **LoginPage.java**: Represents the login screen
  - Username field locator
  - Password field locator
  - Login button locator
  - Methods for entering credentials and submitting login form

### 4. Step Definitions
- **LoginSteps.java**: Cucumber steps for login functionality
  - App launch
  - Credential entry
  - Login button tap
  - Home screen verification
  - Error handling and test skipping

- **EventsTasks.java**: Cucumber steps for events and tasks management
  - Event/Task creation
  - Form validation
  - Mandatory field verification
  - Save operations

- **Hooks.java**: Setup and teardown hooks
  - Test scenario start logging
  - Test scenario end logging
  - Status reporting
  - Driver cleanup

### 5. Feature Files (BDD Scenarios)

#### login.feature
Comprehensive login and authentication testing with CRM focus:
- Successful login with valid credentials
- Secure session creation
- Invalid credential handling
- Empty field validation
- Password field masking
- FAB button visibility
- Session timeout
- Offline mode handling
- Remember me functionality
- App permissions
- Biometric authentication

#### Events.feature
Event management testing scenarios

#### Tasks.feature
Task management testing scenarios

#### Calls.feature
Call tracking and management scenarios

## Environment Setup

### Prerequisites
1. **JDK 11+** - Java Development Kit
2. **Gradle** - Build tool (included via wrapper)
3. **Appium Server** - For local testing
4. **Kobiton Account** - For cloud-based testing

### Installation

1. Clone the repository:
```bash
git clone <repository-url>
cd DCX-MobileTest
```

2. Install dependencies:
```bash
./gradlew clean build
```

### Configuration

#### 1. Kobiton Setup
Create a Kobiton account at https://kobiton.com

Set your Kobiton credentials as environment variables or system properties:
```bash
# Windows PowerShell
$env:KOBITON_USERNAME = "your_username"
$env:KOBITON_API_KEY = "your_api_key"

# Linux/Mac
export KOBITON_USERNAME="your_username"
export KOBITON_API_KEY="your_api_key"
```

#### 2. Update config.properties
Edit `src/test/resources/config/config.properties`:
- Update Kobiton API URL
- Configure iOS device details (device name, platform version, bundle ID)
- Configure Android device details (device name, app package, app activity)
- Set default platform (iOS/Android)
- Update app credentials if needed

#### 3. Application Upload
Upload your CRM mobile app to Kobiton:
1. Log in to Kobiton dashboard
2. Navigate to "App Management"
3. Upload your APK (Android) or IPA (iOS)
4. Copy the app ID and update config.properties

## Running Tests

### Run all tests
```bash
./gradlew test
```

### Run tests with Kobiton (cloud-based)
```bash
./gradlew test -Dkobiton.userName=<username> -Dkobiton.apiKey=<apikey> -Dplatform=iOS
```

### Run specific feature
```bash
./gradlew test -Dcucumber.filter.tags="@login"
```

### Run with custom configuration
```bash
./gradlew test \
  -Dplatform=Android \
  -Dappium.deviceName="Pixel 5" \
  -Dappium.appPackage="com.example.crm" \
  -Dappium.app="kobiton-store:YOUR_APP_ID"
```

## qTest Integration

### Integration Steps
1. Log in to qTest Manager
2. Create a test project for mobile testing
3. Get your qTest API token
4. Configure qTest settings in your CI/CD pipeline
5. Map test scenarios to qTest test cases

### Test Execution Reporting
After each test run, results are:
- Generated in JSON format (target/cucumber.json)
- Can be integrated with qTest using qTest Reporter plugin
- Include screenshots from Kobiton for failed tests

## Best Practices

1. **Page Object Model**: Keep UI elements in Page classes, not in steps
2. **Step Reusability**: Write generic steps that can be reused across features
3. **Error Handling**: Always clean up resources in @After hooks
4. **Explicit Waits**: Use explicit waits instead of Thread.sleep()
5. **Descriptive Scenarios**: Write scenarios that describe business behavior
6. **Test Data**: Externalize test data in property files or constants
7. **Screenshots**: Capture screenshots on failure for debugging
8. **Logging**: Log important actions for troubleshooting

## Troubleshooting

### Common Issues

1. **Driver creation fails**
   - Check Kobiton credentials are set correctly
   - Verify app ID in config.properties
   - Ensure device is available in Kobiton

2. **Element not found**
   - Verify element locators in Page classes
   - Check if element is visible/enabled
   - Add appropriate waits

3. **Session timeout**
   - Increase session timeout in Kobiton settings
   - Check network connectivity
   - Verify app stability

4. **Build failures**
   - Run `./gradlew clean`
   - Check Java version (11+)
   - Verify all dependencies are downloaded

## Dependencies

See `build.gradle` for complete list:
- Appium Java Client 8.6.0
- Cucumber Java 7.14.0
- Cucumber JUnit 7.14.0
- JUnit 4.13.2
- AssertJ 3.24.2
- SLF4J 2.0.9

## References

- [Appium Documentation](https://appium.io/docs/en/latest/)
- [Cucumber Documentation](https://cucumber.io/docs/cucumber/)
- [Kobiton Documentation](https://docs.kobiton.com/)
- [qTest Documentation](https://support.smartbear.com/qtest/)

## Support

For issues or questions, contact the QA team or open an issue in the repository.

## License

[Add your license information here]


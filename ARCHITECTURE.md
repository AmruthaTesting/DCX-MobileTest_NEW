# CRM Mobile Testing Architecture

## System Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                        DCX Mobile CRM Test Framework                        │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  ┌──────────────────────────────────────────────────────────────────────┐  │
│  │                      BDD Test Scenarios (Gherkin)                   │  │
│  │  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  ┌───────────┐ │  │
│  │  │   Login     │  │   Events    │  │   Tasks     │  │  Calls    │ │  │
│  │  │  Features   │  │  Features   │  │  Features   │  │  Features │ │  │
│  │  └─────────────┘  └─────────────┘  └─────────────┘  └───────────┘ │  │
│  └──────────────────────────────────────────────────────────────────────┘  │
│                                    │                                        │
│                                    ▼                                        │
│  ┌──────────────────────────────────────────────────────────────────────┐  │
│  │                    Cucumber Test Runner                             │  │
│  │                   (TestRunner.java)                                 │  │
│  │  - Scans features in src/test/resources/features                   │  │
│  │  - Glue path: com.dcxmobile.steps                                  │  │
│  │  - Generates JSON report: target/cucumber.json                     │  │
│  └──────────────────────────────────────────────────────────────────────┘  │
│                                    │                                        │
│                                    ▼                                        │
│  ┌──────────────────────────────────────────────────────────────────────┐  │
│  │                    Step Definitions (Glue Code)                     │  │
│  │  ┌─────────────────┐  ┌──────────────────┐  ┌────────────────────┐ │  │
│  │  │  LoginSteps.java│  │ EventsTasks.java │  │   Hooks.java       │ │  │
│  │  │                 │  │                  │  │  @Before/@After    │ │  │
│  │  │ Implements:     │  │ Implements:      │  │ Setup/Teardown     │ │  │
│  │  │ @Given/@When    │  │ @Given/@When     │  │                    │ │  │
│  │  │ @Then           │  │ @Then            │  │ Driver cleanup     │ │  │
│  │  └─────────────────┘  └──────────────────┘  └────────────────────┘ │  │
│  └──────────────────────────────────────────────────────────────────────┘  │
│                                    │                                        │
│                                    ▼                                        │
│  ┌──────────────────────────────────────────────────────────────────────┐  │
│  │                    Page Object Model Layer                          │  │
│  │  ┌──────────────────────────────────────────────────────────────┐   │  │
│  │  │  LoginPage.java                                              │   │  │
│  │  │  - usernameField locator                                     │   │  │
│  │  │  - passwordField locator                                     │   │  │
│  │  │  - loginButton locator                                       │   │  │
│  │  │  - Methods: enterUsername(), enterPassword(), tapLogin()     │   │  │
│  │  └──────────────────────────────────────────────────────────────┘   │  │
│  └──────────────────────────────────────────────────────────────────────┘  │
│                                    │                                        │
│                                    ▼                                        │
│  ┌──────────────────────────────────────────────────────────────────────┐  │
│  │                    Driver Management Layer                          │  │
│  │  ┌──────────────────────────┐  ┌────────────────────────────────┐   │  │
│  │  │   DriverFactory.java     │  │   DriverManager.java           │   │  │
│  │  │  - createDriver()        │  │  - getDriver()                 │   │  │
│  │  │  - getDriver()           │  │  - createIOSDriver()           │   │  │
│  │  │  - quitDriver()          │  │  - createAndroidDriver()       │   │  │
│  │  │  - Local/Remote modes    │  │  - quitDriver()                │   │  │
│  │  └──────────────────────────┘  └────────────────────────────────┘   │  │
│  └──────────────────────────────────────────────────────────────────────┘  │
│                                    │                                        │
│                                    ▼                                        │
│  ┌──────────────────────────────────────────────────────────────────────┐  │
│  │                  Configuration Layer                                │  │
│  │  ┌──────────────────────────────────────────────────────────────┐   │  │
│  │  │  KobitonConfig.java                                          │   │  │
│  │  │  - Loads config.properties                                   │   │  │
│  │  │  - getUsername() / getApiKey() - From system properties      │   │  │
│  │  │  - getIOSDeviceName() / getIOSApp()                          │   │  │
│  │  │  - getAndroidDeviceName() / getAndroidApp()                  │   │  │
│  │  │  - getPlatform() / getApiUrl()                               │   │  │
│  │  └──────────────────────────────────────────────────────────────┘   │  │
│  └──────────────────────────────────────────────────────────────────────┘  │
│                                    │                                        │
│                                    ▼                                        │
│  ┌──────────────────────────────────────────────────────────────────────┐  │
│  │                  Mobile Device/Platform Layer                       │  │
│  │  ┌──────────────────┐  ┌──────────────────┐  ┌────────────────────┐ │  │
│  │  │  iOS Device      │  │  Android Device  │  │  Kobiton Cloud     │ │  │
│  │  │  (via Appium)    │  │  (via Appium)    │  │  (via qTest)       │ │  │
│  │  └──────────────────┘  └──────────────────┘  └────────────────────┘ │  │
│  └──────────────────────────────────────────────────────────────────────┘  │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

## Technology Stack

```
┌─────────────────────────────────────────────────────────┐
│                    TEST EXECUTION                      │
│  ┌────────────────────────────────────────────────────┐ │
│  │  Gradle Build System (gradle 7.4.2+)               │ │
│  │  └─ ./gradlew test                                 │ │
│  └────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────┐
│              BDD & TEST FRAMEWORK                      │
│  ┌────────────────────────────────────────────────────┐ │
│  │  Cucumber (v7.14.0)      │  JUnit (v4.13.2)       │ │
│  │  - Feature files          │  - Test runner         │ │
│  │  - Step definitions       │  - Assertions          │ │
│  │  - Hooks/Plugins          │  - Test execution      │ │
│  └────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────┐
│          MOBILE AUTOMATION FRAMEWORK                   │
│  ┌────────────────────────────────────────────────────┐ │
│  │  Appium (v8.6.0)        │  Selenium WebDriver      │ │
│  │  - Java Client           │  - Remote execution     │ │
│  │  - Android Driver        │  - Element interaction  │ │
│  │  - iOS Driver            │  - Waits/synchronization│ │
│  └────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────┐
│           TEST MANAGEMENT & CLOUD PLATFORM             │
│  ┌────────────────────────────────────────────────────┐ │
│  │  Kobiton (Cloud-based)  │  qTest (Test Mgmt)      │ │
│  │  - Device cloud          │  - Test case mgmt      │ │
│  │  - Session recording     │  - Result reporting    │ │
│  │  - App management        │  - CI/CD integration   │ │
│  └────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────┐
│         LOGGING & REPORTING                            │
│  ┌────────────────────────────────────────────────────┐ │
│  │  SLF4J (v2.0.9)  │  Cucumber JSON Reports         │ │
│  │  - Console logs  │  - target/cucumber.json        │ │
│  │  - File logs     │  - qTest reporter              │ │
│  └────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────┘
```

## Data Flow Diagram

```
┌─────────────────────────┐
│  Feature File           │
│  (Gherkin Syntax)       │
│  - login.feature        │
│  - Events.feature       │
│  - Tasks.feature        │
│  - Calls.feature        │
└────────────┬────────────┘
             │
             ▼
┌─────────────────────────────────────┐
│  Cucumber Parser                    │
│  - Parse scenarios                  │
│  - Extract Given/When/Then steps    │
│  - Build test execution plan        │
└────────────┬────────────────────────┘
             │
             ▼
┌─────────────────────────────────────┐
│  Step Implementation Matching       │
│  - Match steps to step definitions  │
│  - Resolve step methods             │
│  - Bind parameters                  │
└────────────┬────────────────────────┘
             │
             ▼
┌─────────────────────────────────────┐
│  @Before Hooks                      │
│  - Load configuration               │
│  - Print scenario start             │
└────────────┬────────────────────────┘
             │
             ▼
┌─────────────────────────────────────┐
│  Create AppiumDriver                │
│  - Load KobitonConfig               │
│  - Determine iOS or Android         │
│  - Create DriverFactory instance    │
│  - Connect to Kobiton or local      │
└────────────┬────────────────────────┘
             │
             ▼
┌─────────────────────────────────────┐
│  Execute Step Definitions           │
│  - @Given: Setup preconditions      │
│  - @When: Perform actions           │
│  - @Then: Assert expectations       │
│  - Page Object interactions         │
└────────────┬────────────────────────┘
             │
             ▼
┌─────────────────────────────────────┐
│  @After Hooks                       │
│  - Quit driver session              │
│  - Print scenario result            │
│  - Cleanup resources                │
└────────────┬────────────────────────┘
             │
             ▼
┌─────────────────────────────────────┐
│  Generate Reports                   │
│  - Cucumber JSON report             │
│  - Console output                   │
│  - Upload to qTest                  │
└────────────┬────────────────────────┘
             │
             ▼
┌─────────────────────────────────────┐
│  Test Results                       │
│  - PASS/FAIL status                 │
│  - Screenshots/videos from Kobiton  │
│  - qTest integration                │
└─────────────────────────────────────┘
```

## Environment Configuration

```
Local Testing
├─ Appium Server (localhost:4723)
├─ Local Device/Emulator
├─ config.properties
└─ Run with: ./gradlew test -Dremote=false

Cloud Testing (Kobiton)
├─ Kobiton API (api.kobiton.com)
├─ Cloud Device Farm
├─ KobitonConfig.java
├─ System Properties:
│  ├─ kobiton.userName
│  ├─ kobiton.apiKey
│  └─ platform (iOS/Android)
└─ Run with: ./gradlew test -Dkobiton.userName=... -Dkobiton.apiKey=...

Test Management (qTest)
├─ qTest Manager
├─ Test Projects
├─ Test Cases
├─ Cucumber JSON Report
└─ Integration: POST results to qTest API
```

## File Relationships

```
TestRunner.java
├─ Scans: src/test/resources/features/*.feature
├─ Glue path: com.dcxmobile.steps
└─ Runs steps from:
    ├─ LoginSteps.java
    ├─ EventsTasks.java
    └─ Hooks.java (via @Before/@After)

LoginSteps.java / EventsTasks.java
├─ Uses: LoginPage.java (Page Object Model)
├─ Uses: DriverFactory.getDriver()
└─ Called by: Cucumber test runner

Hooks.java
├─ Uses: DriverManager.quitDriver()
├─ Executes: Before each scenario
└─ Executes: After each scenario

DriverFactory.java / DriverManager.java
├─ Uses: KobitonConfig for settings
├─ Returns: AppiumDriver instance
└─ Manages: Driver lifecycle

KobitonConfig.java
├─ Loads: src/test/resources/config/config.properties
├─ Reads: System properties/environment variables
└─ Provides: Configuration to DriverManager
```

## CRM Mobile App Testing Focus Areas

```
Login & Authentication
├─ Valid credentials
├─ Invalid credentials
├─ Session management
├─ Biometric authentication
├─ Remember me
├─ Password security
└─ Offline handling

Events Management (CRM)
├─ Event creation
├─ Event editing
├─ Event deletion
├─ Event scheduling
├─ Attendee management
└─ Event notifications

Tasks Management (CRM)
├─ Task creation
├─ Task assignment
├─ Task status updates
├─ Task priorities
├─ Task reminders
└─ Task completion

Calls Management (CRM)
├─ Call logging
├─ Call history
├─ Call notes
├─ Call scheduling
├─ Contact integration
└─ Call recordings

Cross-Functional
├─ Performance testing
├─ Battery consumption
├─ Network connectivity
├─ Data synchronization
├─ App permissions
└─ Security testing
```

## Integration Points

```
Kobiton Platform
├─ Device management
├─ Session recording
├─ Screenshots on failure
├─ App upload & versioning
└─ Session logs

qTest Manager
├─ Test case mapping
├─ Result reporting
├─ Test execution history
├─ Defect tracking
└─ Metrics & analytics

CI/CD Pipeline
├─ Build triggers
├─ Test execution
├─ Result collection
├─ Report generation
└─ Notification delivery
```

---

**Note**: This architecture supports both local (Appium Server) and cloud-based (Kobiton) mobile testing with comprehensive CRM functionality coverage and integration with qTest for test management.


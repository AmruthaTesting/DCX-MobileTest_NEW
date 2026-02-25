# DCX Mobile CRM Testing - Quick Reference Guide

## 🚀 Quick Start

### 1. Initial Setup (One-time)
```bash
# Clone repository
git clone <repo-url>
cd DCX-MobileTest

# Build project
./gradlew clean build

# Verify structure
./gradlew compileTestJava  # Should complete with BUILD SUCCESSFUL
```

### 2. Configure Credentials

Set these environment variables:

**For Kobiton (Cloud Testing):**
```powershell
# Windows PowerShell
$env:KOBITON_USERNAME = "your_username"
$env:KOBITON_API_KEY = "your_api_key"

# Or set in system environment variables
```

**For qTest (Test Management):**
```powershell
$env:QTEST_PROJECT_ID = "your_project_id"
$env:QTEST_API_TOKEN = "your_api_token"
```

### 3. Run Tests

```bash
# Run all tests
./gradlew test

# Run iOS tests
./gradlew test -Dplatform=iOS

# Run Android tests
./gradlew test -Dplatform=Android

# Run with Kobiton
./gradlew test -Dkobiton.userName=<user> -Dkobiton.apiKey=<key> -Dplatform=iOS

# Run specific feature
./gradlew test -Dcucumber.filter.tags="@login"
```

## 📁 Project Structure

```
DCX-MobileTest/
├── src/test/java/com/dcxmobile/
│   ├── config/                    ← Kobiton & Driver Configuration
│   │   ├── KobitonConfig.java     ← Load/manage Kobiton settings
│   │   └── DriverManager.java     ← Create iOS/Android drivers
│   ├── drivers/
│   │   └── DriverFactory.java     ← Singleton driver factory
│   ├── pages/
│   │   └── LoginPage.java         ← Page Object for Login screen
│   ├── runner/
│   │   └── TestRunner.java        ← Cucumber test runner
│   └── steps/
│       ├── Hooks.java             ← @Before/@After test hooks
│       ├── LoginSteps.java        ← Login step definitions
│       └── EventsTasks.java       ← Event/Task step definitions
├── src/test/resources/
│   ├── config/
│   │   └── config.properties      ← Device & app configuration
│   └── features/
│       ├── login.feature          ← Login & auth scenarios (12 scenarios)
│       ├── Events.feature         ← Event management
│       ├── Tasks.feature          ← Task management
│       └── Calls.feature          ← Call tracking
├── build.gradle                    ← Gradle build configuration
├── README.md
├── SETUP_GUIDE.md                  ← Complete setup instructions
├── MIGRATION_SUMMARY.md            ← What was changed
├── ARCHITECTURE.md                 ← System architecture diagrams
└── QTEST_INTEGRATION.md            ← qTest integration guide
```

## 📋 File Purposes

| File | Purpose |
|------|---------|
| **KobitonConfig.java** | Loads `config.properties` and system properties for Kobiton/device config |
| **DriverManager.java** | Creates AppiumDriver (iOS/Android) for cloud (Kobiton) or local Appium |
| **DriverFactory.java** | Manages driver lifecycle; implements singleton pattern |
| **LoginPage.java** | Page Object with login UI element locators and interaction methods |
| **TestRunner.java** | Single Cucumber runner that scans all feature files in `features/` |
| **Hooks.java** | Setup/teardown with @Before/@After; logs scenario start/end; quits driver |
| **LoginSteps.java** | Implements login scenario steps; uses LoginPage and DriverFactory |
| **EventsTasks.java** | Implements event/task scenario steps |
| **config.properties** | Device names, app packages, platform, URLs, credentials |

## 🎯 Key Features

✅ **BDD with Cucumber** - Write tests in human-readable Gherkin syntax  
✅ **Kobiton Integration** - Cloud-based device testing (iOS & Android)  
✅ **qTest Integration** - Test management and reporting  
✅ **Page Object Model** - Maintainable, scalable UI automation  
✅ **Appium Support** - Both local and cloud-based device testing  
✅ **CRM Focus** - Tests for Login, Events, Tasks, and Calls modules  
✅ **Comprehensive Scenarios** - 12 login scenarios covering security, validation, offline, biometric  

## 🧪 Test Scenarios

### Login Feature (12 scenarios)
1. ✅ Successful login with valid credentials
2. ✅ Secure session creation
3. ✅ Invalid credentials handling
4. ✅ Empty username validation
5. ✅ Empty password validation
6. ✅ Password field masking
7. ✅ FAB button visibility
8. ✅ Session timeout after inactivity
9. ✅ Offline login attempt
10. ✅ Remember me functionality
11. ✅ App permissions request
12. ✅ Biometric login option

### Other Modules
- **Events.feature** - Event management (create, edit, delete, attend)
- **Tasks.feature** - Task management (create, assign, update status)
- **Calls.feature** - Call tracking and logging

## 🛠 Common Commands

```bash
# Compile only
./gradlew compileTestJava

# Run all tests
./gradlew test

# Run with verbose output
./gradlew test --info

# Generate HTML test report
# (Report location: target/cucumber-report.html)

# Clean build artifacts
./gradlew clean

# Run specific scenario tag
./gradlew test -Dcucumber.filter.tags="@login and @critical"

# Run with custom properties
./gradlew test \
  -Dplatform=iOS \
  -Dkobiton.userName=user \
  -Dkobiton.apiKey=key
```

## 📊 Test Execution Flow

```
1. Run Command
   ↓
2. TestRunner (Cucumber) loads feature files
   ↓
3. Parse Gherkin scenarios
   ↓
4. @Before Hook executes
   ├─ Log scenario start
   └─ Initialize test
   ↓
5. DriverManager.getDriver()
   ├─ Read KobitonConfig
   ├─ Create AppiumDriver (iOS/Android)
   └─ Connect to Kobiton or local Appium
   ↓
6. Execute @Given, @When, @Then steps
   ├─ Use LoginPage/Page Objects
   ├─ Interact with app elements
   └─ Assert results
   ↓
7. @After Hook executes
   ├─ Log scenario result
   └─ Quit driver & cleanup
   ↓
8. Generate Reports
   ├─ Cucumber JSON (target/cucumber.json)
   ├─ HTML report (target/cucumber-report.html)
   └─ Upload to qTest (optional)
```

## 🔐 Configuration Priority (Highest to Lowest)

1. **System Properties** (via `-D` flag)
   ```bash
   ./gradlew test -Dplatform=iOS
   ```

2. **Environment Variables**
   ```bash
   $env:PLATFORM = "iOS"
   ```

3. **config.properties** (defaults in file)
   ```properties
   platform=iOS
   ```

## 🐛 Troubleshooting

| Problem | Solution |
|---------|----------|
| Build fails | Run `./gradlew clean` |
| Driver not created | Check Kobiton credentials in system properties |
| Element not found | Verify locators in LoginPage.java |
| Tests skip | Check `Assume.assumeFalse()` conditions |
| No qTest upload | Set `QTEST_PROJECT_ID` and `QTEST_API_TOKEN` env vars |

## 📚 Documentation Files

| File | Contents |
|------|----------|
| **SETUP_GUIDE.md** | Complete setup, Kobiton config, Appium server setup |
| **MIGRATION_SUMMARY.md** | What was restructured, verification checklist |
| **ARCHITECTURE.md** | System diagrams, tech stack, data flow, integration points |
| **QTEST_INTEGRATION.md** | qTest setup, CI/CD examples (Jenkins/GitHub Actions) |
| **README.md** | Original project info |

## 🔗 Important Links

- **Appium**: https://appium.io/docs/en/latest/
- **Cucumber**: https://cucumber.io/docs/cucumber/
- **Kobiton**: https://docs.kobiton.com/
- **qTest**: https://support.smartbear.com/qtest/

## 💡 Best Practices

1. **Always use Page Objects** - Put UI selectors in Page classes, not steps
2. **Keep steps generic** - Write reusable step definitions
3. **Tag scenarios** - Use tags for organization: `@login @critical @ios`
4. **Meaningful names** - Write descriptive scenario/step names
5. **Clean up resources** - Always quit drivers in @After hooks
6. **Use data-driven tests** - Parameterize test data in scenarios
7. **Monitor Kobiton dashboard** - Check session recordings for failures
8. **Regular qTest reviews** - Track metrics and improve coverage

## 🚦 Next Steps

1. **Set up Kobiton account** and get API credentials
2. **Upload your mobile app** to Kobiton app management
3. **Update config.properties** with device/app details
4. **Implement remaining steps** in LoginSteps.java
5. **Create Page Objects** for Events, Tasks, Calls screens
6. **Implement step definitions** for other features
7. **Set up CI/CD pipeline** (Jenkins/GitHub Actions)
8. **Configure qTest project** and map test cases
9. **Run first test execution** and verify results
10. **Monitor dashboards** - Kobiton sessions, qTest reports

## 📞 Support Resources

- **Project Documentation**: See SETUP_GUIDE.md, ARCHITECTURE.md
- **Appium Help**: Official documentation + Stack Overflow
- **Kobiton Support**: support@kobiton.com
- **qTest Support**: support@smartbear.com

---

**Framework Version**: 1.0.0  
**Last Updated**: February 25, 2026  
**Status**: ✅ Ready for Development and Testing  



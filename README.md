DCX-MobileTest - Mobile test starter with Appium, Cucumber, Kobiton and qTest

Overview

This project contains a minimal Cucumber + Appium Java test scaffold to run mobile tests locally or on Kobiton device cloud, and export Cucumber JSON for integration with qTest.

Quick setup

1. Set environment variables (locally or in CI):
   - PLATFORM (android|ios) - default: android
   - REMOTE (true|false) - whether to use Kobiton
   - DEVICE_NAME - device name when using Kobiton
   - PLATFORM_VERSION - device platform version
   - APP_PATH - local app path (.apk or .ipa) for local runs
   - KOBITON_USERNAME, KOBITON_API_KEY, KOBITON_APP_ID, KOBITON_SERVER
   - QTEST_TOKEN, QTEST_PROJECT_ID, QTEST_CYCLE_ID

2. Run tests locally with Appium server running:

   ./gradlew test -DPLATFORM=android -DREMOTE=false

3. Run tests on Kobiton (example):

   REMOTE=true PLATFORM=android KOBITON_USERNAME=<user> KOBITON_API_KEY=<key> KOBITON_APP_ID=<kobiton-app-id> ./gradlew test

What this scaffold includes

- Cucumber runner: src/test/java/com/dcxmobile/runner/CucumberTestRunner.java
- Driver factory: src/test/java/com/dcxmobile/drivers/DriverFactory.java
- Sample Page Object: src/test/java/com/dcxmobile/pages/LoginPage.java
- Step definitions: src/test/java/com/dcxmobile/steps/LoginSteps.java
- Feature: src/test/resources/features/login.feature
- Cucumber JSON output: target/cucumber.json

Next steps

- Update selectors in LoginPage to match your CRM app.
- Add more features and map them to qTest test cases.
- Implement a qTest uploader script to POST target/cucumber.json to your qTest project.

Security

Do not commit any API keys. Use CI secret storage or OS environment variables.


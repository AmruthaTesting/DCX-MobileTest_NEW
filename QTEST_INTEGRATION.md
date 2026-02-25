# qTest Integration Guide for DCX Mobile CRM Testing

## Overview

This guide provides step-by-step instructions to integrate your Cucumber mobile tests with qTest Manager for comprehensive test management, reporting, and CI/CD integration.

## Prerequisites

- SmartBear qTest account (https://qtest.smartbear.com)
- qTest API Token
- Cucumber test framework configured (as per SETUP_GUIDE.md)
- Gradle build system
- CI/CD system (Jenkins, GitHub Actions, Azure Pipelines, etc.)

## Step 1: Create qTest Project Structure

### 1.1 Create Test Project
1. Log in to qTest Manager
2. Click "Create New Project"
3. Project Name: `DCX Mobile CRM Tests`
4. Project Type: `Functional Testing`
5. Click "Create"

### 1.2 Create Module Structure
```
DCX Mobile CRM Tests/
├── Login & Authentication
│   ├── Valid Credentials
│   ├── Invalid Credentials
│   ├── Session Management
│   ├── Biometric Authentication
│   └── Offline Handling
├── Events Management
│   ├── Event Creation
│   ├── Event Editing
│   ├── Event Deletion
│   └── Attendee Management
├── Tasks Management
│   ├── Task Creation
│   ├── Task Assignment
│   ├── Task Status Updates
│   └── Task Completion
└── Calls Management
    ├── Call Logging
    ├── Call History
    └── Call Notes
```

### 1.3 Create Test Cases
Map each Cucumber scenario to a qTest test case:

| Cucumber Scenario | qTest Test Case ID | Test Case Name |
|------|------|------|
| Successful login with valid credentials | TC-001 | Login - Valid Credentials |
| Verify secure session is created | TC-002 | Login - Session Security |
| Login fails with invalid credentials | TC-003 | Login - Invalid Credentials |
| Login with empty username field | TC-004 | Login - Empty Username |
| Login with empty password field | TC-005 | Login - Empty Password |
| Verify password field is masked | TC-006 | Login - Password Masking |
| Verify FAB button is visible | TC-007 | Login - FAB Visibility |
| Login timeout after inactivity | TC-008 | Login - Session Timeout |
| Offline login attempt | TC-009 | Login - Offline Mode |
| Login with remember me | TC-010 | Login - Remember Me |
| App permissions after login | TC-011 | Login - App Permissions |
| Biometric login option | TC-012 | Login - Biometric Auth |

## Step 2: Get qTest API Token

1. In qTest Manager, go to **Settings** → **API Token**
2. Click **Generate Token**
3. Copy your API Token (save securely)
4. Note your **Project ID** from the project URL: `https://qtest.smartbear.com/projects/{PROJECT_ID}`

## Step 3: Configure qTest Reporting Plugin

### 3.1 Add qTest Plugin to build.gradle

```gradle
plugins {
    id 'java'
}

group = 'com.edwardjones.mobile'
version = '1.0-SNAPSHOT'

repositories {
    mavenCentral()
}

dependencies {
    // ... existing dependencies ...
    
    // qTest Integration
    testImplementation 'com.qasymphony:appium-java-client:8.6.0'
    testImplementation 'io.rest-assured:rest-assured:5.3.1'
}

test {
    useJUnit()
    systemProperties System.getProperties()
    
    // qTest integration
    doFirst {
        systemProperty 'qtest.projectId', System.getenv('QTEST_PROJECT_ID')
        systemProperty 'qtest.apiToken', System.getenv('QTEST_API_TOKEN')
        systemProperty 'qtest.resultName', "Mobile CRM Tests - ${new Date().format('yyyy-MM-dd HH:mm:ss')}"
    }
}
```

### 3.2 Add qTest Reporter Plugin to Cucumber

Update `TestRunner.java`:

```java
package com.dcxmobile.runner;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features",
    glue = {"com.dcxmobile.steps"},
    plugin = {
        "pretty",
        "json:target/cucumber.json",
        "html:target/cucumber-report.html",
        "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
    },
    monochrome = true,
    dryRun = false,
    strict = false
)
public class TestRunner {
    // Runner for Cucumber tests with qTest integration
}
```

## Step 4: Create qTest Result Uploader Script

Create `scripts/upload-to-qtest.sh` (for Linux/Mac) or `scripts/upload-to-qtest.ps1` (for Windows):

### For PowerShell (Windows)

Create file: `scripts/upload-to-qtest.ps1`

```powershell
param(
    [string]$projectId = $env:QTEST_PROJECT_ID,
    [string]$apiToken = $env:QTEST_API_TOKEN,
    [string]$reportFile = "target/cucumber.json",
    [string]$testCycleName = "Mobile CRM Tests - $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')"
)

# qTest API endpoint
$qTestUrl = "https://api.qasymphony.com/api/v3.0/projects/$projectId/test-runs"

# Read JSON report
$jsonContent = Get-Content $reportFile | ConvertFrom-Json

# Prepare qTest result object
$qTestResults = @{
    "name" = $testCycleName
    "type" = "Automated"
    "environment" = "Mobile"
    "platform" = $env:PLATFORM ?? "iOS"
    "build" = "1.0.0"
    "testResults" = @()
}

# Map Cucumber results to qTest format
foreach ($feature in $jsonContent) {
    foreach ($scenario in $feature.elements) {
        $status = "PASS"
        
        foreach ($step in $scenario.steps) {
            if ($step.result.status -eq "failed") {
                $status = "FAIL"
                break
            }
        }
        
        $qTestResults.testResults += @{
            "name" = $scenario.name
            "status" = $status
            "module" = $feature.name
            "duration" = 1000
        }
    }
}

# Convert to JSON
$jsonBody = $qTestResults | ConvertTo-Json -Depth 10

# Upload to qTest
$headers = @{
    "Authorization" = "Bearer $apiToken"
    "Content-Type" = "application/json"
}

try {
    $response = Invoke-RestMethod -Uri $qTestUrl -Method Post -Headers $headers -Body $jsonBody
    Write-Host "✅ Results uploaded to qTest successfully!"
    Write-Host "Test Run ID: $($response.id)"
    return 0
} catch {
    Write-Host "❌ Error uploading to qTest: $_"
    return 1
}
```

### For Bash (Linux/Mac)

Create file: `scripts/upload-to-qtest.sh`

```bash
#!/bin/bash

PROJECT_ID=${QTEST_PROJECT_ID}
API_TOKEN=${QTEST_API_TOKEN}
REPORT_FILE=${1:-target/cucumber.json}
TEST_CYCLE_NAME="Mobile CRM Tests - $(date '+%Y-%m-%d %H:%M:%S')"

# qTest API endpoint
QTEST_URL="https://api.qasymphony.com/api/v3.0/projects/${PROJECT_ID}/test-runs"

# Read and transform Cucumber JSON to qTest format
python3 << 'EOF'
import json
import sys
from datetime import datetime

# Load Cucumber report
with open('$REPORT_FILE', 'r') as f:
    cucumber_results = json.load(f)

# Transform to qTest format
qtest_results = {
    "name": "$TEST_CYCLE_NAME",
    "type": "Automated",
    "environment": "Mobile",
    "platform": os.getenv('PLATFORM', 'iOS'),
    "build": "1.0.0",
    "testResults": []
}

for feature in cucumber_results:
    for scenario in feature.get('elements', []):
        status = "PASS"
        for step in scenario.get('steps', []):
            if step.get('result', {}).get('status') == 'failed':
                status = "FAIL"
                break
        
        qtest_results['testResults'].append({
            "name": scenario.get('name'),
            "status": status,
            "module": feature.get('name'),
            "duration": 1000
        })

print(json.dumps(qtest_results, indent=2))
EOF

# Upload to qTest
curl -X POST "$QTEST_URL" \
  -H "Authorization: Bearer $API_TOKEN" \
  -H "Content-Type: application/json" \
  -d @qtest_results.json

echo "✅ Results uploaded to qTest"
```

## Step 5: Environment Variables Configuration

Set these environment variables in your CI/CD system:

```bash
# qTest Configuration
QTEST_PROJECT_ID=<your-project-id>
QTEST_API_TOKEN=<your-api-token>

# Kobiton Configuration
KOBITON_USERNAME=<kobiton-username>
KOBITON_API_KEY=<kobiton-api-key>

# App Configuration
PLATFORM=iOS
APP_ENVIRONMENT=UAT
```

### GitHub Actions Example

Create `.github/workflows/mobile-tests.yml`:

```yaml
name: DCX Mobile CRM Tests

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v3
    
    - name: Set up JDK
      uses: actions/setup-java@v3
      with:
        java-version: '11'
    
    - name: Build and Test
      run: |
        ./gradlew clean test \
          -Dkobiton.userName=${{ secrets.KOBITON_USERNAME }} \
          -Dkobiton.apiKey=${{ secrets.KOBITON_API_KEY }} \
          -Dplatform=iOS
      env:
        QTEST_PROJECT_ID: ${{ secrets.QTEST_PROJECT_ID }}
        QTEST_API_TOKEN: ${{ secrets.QTEST_API_TOKEN }}
    
    - name: Upload Results to qTest
      if: always()
      run: |
        ./scripts/upload-to-qtest.ps1 \
          -projectId ${{ secrets.QTEST_PROJECT_ID }} \
          -apiToken ${{ secrets.QTEST_API_TOKEN }}
```

### Jenkins Example

Create `Jenkinsfile`:

```groovy
pipeline {
    agent any
    
    environment {
        QTEST_PROJECT_ID = credentials('qtest-project-id')
        QTEST_API_TOKEN = credentials('qtest-api-token')
        KOBITON_USERNAME = credentials('kobiton-username')
        KOBITON_API_KEY = credentials('kobiton-api-key')
    }
    
    stages {
        stage('Build') {
            steps {
                sh './gradlew clean build'
            }
        }
        
        stage('Test') {
            steps {
                sh '''
                    ./gradlew test \
                        -Dkobiton.userName=${KOBITON_USERNAME} \
                        -Dkobiton.apiKey=${KOBITON_API_KEY} \
                        -Dplatform=iOS
                '''
            }
        }
        
        stage('Upload to qTest') {
            steps {
                script {
                    sh '''
                        ./scripts/upload-to-qtest.sh \
                            target/cucumber.json
                    '''
                }
            }
            post {
                always {
                    // Archive test reports
                    junit 'target/**/TEST-*.xml'
                    publishHTML([
                        reportDir: 'target',
                        reportFiles: 'cucumber-report.html',
                        reportName: 'Cucumber Report'
                    ])
                }
            }
        }
    }
}
```

## Step 6: Test Execution and Reporting

### Run Tests Locally with qTest Integration

```bash
# Set environment variables
$env:QTEST_PROJECT_ID = "your-project-id"
$env:QTEST_API_TOKEN = "your-api-token"
$env:KOBITON_USERNAME = "your-kobiton-username"
$env:KOBITON_API_KEY = "your-kobiton-api-key"

# Run tests
./gradlew test -Dplatform=iOS

# Upload results to qTest
./scripts/upload-to-qtest.ps1
```

### View Results in qTest

1. Go to qTest Manager dashboard
2. Navigate to **Releases** → **Test Runs**
3. Find your test run by name/date
4. View detailed results:
   - Test case status
   - Execution timeline
   - Pass/fail metrics
   - Screenshots from Kobiton
   - Execution logs

## Step 7: Advanced qTest Features

### Link Test Cases to Defects

```java
// In your step definitions, log defect information
if (testFailed) {
    String defectId = qTestClient.createDefect(
        "Login button not responding",
        "CRITICAL",
        "DCX-Mobile",
        "Development"
    );
    System.out.println("Defect created: " + defectId);
}
```

### Track Metrics and Trends

In qTest Manager:
1. Go to **Dashboard** → **Reports**
2. Create custom reports for:
   - Test execution trends
   - Pass/fail rates by module
   - Device/platform coverage
   - Defect distribution
   - Team productivity metrics

### Integration with Defect Tracking

Link qTest with your issue tracking system:
1. Go to **Settings** → **Integrations**
2. Add Jira/Azure DevOps/GitHub Issues
3. Configure auto-creation of defects
4. Map statuses and priorities

## Step 8: Best Practices for qTest Integration

1. **Consistent Naming**: Use same names in Cucumber and qTest
2. **Tagging Strategy**: Tag scenarios for better organization
   ```gherkin
   @smoke @ios @login @critical
   Scenario: Successful login with valid credentials
   ```

3. **Test Data Management**: Store test data in qTest
4. **Result Analysis**: Regularly review qTest reports
5. **Continuous Improvement**: Track metrics and improve test coverage
6. **Version Control**: Version your test cases in qTest

## Step 9: Troubleshooting

### Common Issues

| Issue | Solution |
|-------|----------|
| API Token invalid | Regenerate token in qTest Settings |
| Project ID not found | Verify Project ID from project URL |
| Results not uploading | Check network connectivity and API endpoint |
| Missing test run data | Ensure Cucumber JSON report is generated |
| Duplicate test runs | Check CI/CD pipeline for duplicate triggers |

## References

- [qTest API Documentation](https://api.qasymphony.com/docs/v3.0/index.html)
- [Cucumber-qTest Integration](https://docs.qameta.allure.io/)
- [SmartBear qTest Documentation](https://support.smartbear.com/qtest/)

## Support

For integration issues, contact:
- qTest Support: support@smartbear.com
- Project Team: [your-team-email]

---

**Last Updated**: February 25, 2026
**Framework Version**: 1.0.0


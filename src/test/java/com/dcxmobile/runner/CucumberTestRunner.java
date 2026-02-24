package com.dcxmobile.runner;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features",
    glue = {"com.dcxmobile.steps"},
    plugin = {"pretty", "json:target/cucumber.json"},
    monochrome = true
)
public class CucumberTestRunner {
    // Runner for Cucumber tests
}


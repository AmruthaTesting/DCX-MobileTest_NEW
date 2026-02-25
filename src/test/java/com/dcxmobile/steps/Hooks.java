package com.dcxmobile.steps;

import com.dcxmobile.config.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class Hooks {

    @Before
    public void setUp(Scenario scenario) {
        System.out.println("=============================");
        System.out.println("Starting: " + scenario.getName());
        System.out.println("=============================");
    }

    @After
    public void tearDown(Scenario scenario) {
        System.out.println("=============================");
        System.out.println("Finished: " + scenario.getName());
        System.out.println("Status:   " + scenario.getStatus());
        System.out.println("=============================");
        DriverManager.quitDriver();
    }
}
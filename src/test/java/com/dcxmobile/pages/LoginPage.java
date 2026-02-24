package com.dcxmobile.pages;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class LoginPage {
    private AppiumDriver driver;

    private By usernameField = By.id("username");
    private By passwordField = By.id("password");
    private By loginButton = By.id("loginButton");

    public LoginPage(AppiumDriver driver) {
        this.driver = driver;
    }

    public void enterUsername(String user) {
        WebElement e = (WebElement) driver.findElement(usernameField);
        e.clear();
        e.sendKeys(user);
    }

    public void enterPassword(String pass) {
        WebElement e = (WebElement) driver.findElement(passwordField);
        e.clear();
        e.sendKeys(pass);
    }

    public void tapLogin() {
        driver.findElement(loginButton).click();
    }
}


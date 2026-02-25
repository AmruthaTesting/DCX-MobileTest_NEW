package com.dcxmobile.config;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import java.net.URL;

public class DriverManager {

    private static AppiumDriver driver;

    public static AppiumDriver getDriver() throws Exception {
        if (driver == null) {
            String platform = KobitonConfig.getPlatform();
            if (platform.equalsIgnoreCase("iOS")) {
                driver = createIOSDriver();
            } else {
                driver = createAndroidDriver();
            }
        }
        return driver;
    }

    private static IOSDriver createIOSDriver() throws Exception {
        String url = "https://"
                + KobitonConfig.getUsername() + ":"
                + KobitonConfig.getApiKey() + "@"
                + "api.kobiton.com/wd/hub";

        XCUITestOptions options = new XCUITestOptions();
        options.setDeviceName(KobitonConfig.getIOSDeviceName());
        options.setPlatformVersion(KobitonConfig.getIOSPlatformVersion());
        options.setBundleId(KobitonConfig.getIOSBundleId());
        options.setApp(KobitonConfig.getIOSApp());
        options.setCapability("sessionName",        "EJ Mobile iOS Test");
        options.setCapability("captureScreenshots",  true);
        options.setCapability("kobiton:sessionName","EJ iOS Cucumber Test");

        System.out.println("Starting iOS session on Kobiton...");
        return new IOSDriver(new URL(url), options);
    }

    private static AndroidDriver createAndroidDriver() throws Exception {
        String url = "https://"
                + KobitonConfig.getUsername() + ":"
                + KobitonConfig.getApiKey() + "@"
                + "api.kobiton.com/wd/hub";

        UiAutomator2Options options = new UiAutomator2Options();
        options.setDeviceName(KobitonConfig.getAndroidDeviceName());
        options.setAppPackage(KobitonConfig.getAndroidAppPackage());
        options.setApp(KobitonConfig.getAndroidApp());
        options.setCapability("sessionName",        "EJ Mobile Android Test");
        options.setCapability("captureScreenshots",  true);

        System.out.println("Starting Android session on Kobiton...");
        return new AndroidDriver(new URL(url), options);
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
            System.out.println("✅ Driver session closed.");
        }
    }
}


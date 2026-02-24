package com.dcxmobile.drivers;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.MutableCapabilities;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

public class DriverFactory {
    private static AppiumDriver driver;

    public static AppiumDriver getDriver() {
        return driver;
    }

    public static AppiumDriver createDriver(String platform, boolean remote) throws MalformedURLException {
        if (driver != null) {
            return driver;
        }

        MutableCapabilities caps = new MutableCapabilities();
        DesiredCapabilities dcaps = new DesiredCapabilities();

        if (platform.equalsIgnoreCase("android")) {
            dcaps.setCapability("platformName", "Android");
            dcaps.setCapability("automationName", "UiAutomator2");
            dcaps.setCapability("deviceName", System.getenv().getOrDefault("DEVICE_NAME", "Android Device"));
            // Provide app under test as path or appPackage/appActivity
            String app = System.getenv("APP_PATH");
            if (app != null && !app.isEmpty()) {
                dcaps.setCapability("app", app);
            }
        } else if (platform.equalsIgnoreCase("ios")) {
            dcaps.setCapability("platformName", "iOS");
            dcaps.setCapability("automationName", "XCUITest");
            dcaps.setCapability("deviceName", System.getenv().getOrDefault("DEVICE_NAME", "iPhone Simulator"));
            String app = System.getenv("APP_PATH");
            if (app != null && !app.isEmpty()) {
                dcaps.setCapability("app", app);
            }
        }

        if (remote) {
            // Configure kobiton options
            Map<String, Object> kobitonOptions = new HashMap<>();
            kobitonOptions.put("sessionName", System.getenv().getOrDefault("KOBITON_SESSION_NAME", "DCX Mobile Test"));
            kobitonOptions.put("sessionDescription", "Automated tests from Cucumber/JUnit");
            String kobitonApp = System.getenv("KOBITON_APP_ID");
            if (kobitonApp != null && !kobitonApp.isEmpty()) {
                kobitonOptions.put("app", kobitonApp);
            }
            String deviceName = System.getenv("DEVICE_NAME");
            if (deviceName != null && !deviceName.isEmpty()) {
                kobitonOptions.put("deviceName", deviceName);
            }
            String platformVersion = System.getenv("PLATFORM_VERSION");
            if (platformVersion != null && !platformVersion.isEmpty()) {
                kobitonOptions.put("platformVersion", platformVersion);
            }

            dcaps.setCapability("kobiton:options", kobitonOptions);

            String kobitonUser = System.getenv("KOBITON_USERNAME");
            String kobitonApiKey = System.getenv("KOBITON_API_KEY");
            String kobitonServer = System.getenv().getOrDefault("KOBITON_SERVER", "api.kobiton.com");
            String remoteUrl = String.format("https://%s:%s@%s/wd/hub", kobitonUser, kobitonApiKey, kobitonServer);

            if (platform.equalsIgnoreCase("android")) {
                driver = new AndroidDriver(new URL(remoteUrl), dcaps);
            } else {
                driver = new IOSDriver(new URL(remoteUrl), dcaps);
            }
        } else {
            URL local = new URL(System.getenv().getOrDefault("APPIUM_SERVER_URL", "http://127.0.0.1:4723/wd/hub"));
            if (platform.equalsIgnoreCase("android")) {
                driver = new AndroidDriver(local, dcaps);
            } else {
                driver = new IOSDriver(local, dcaps);
            }
        }

        return driver;
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}


package com.dcxmobile.config;

import java.io.FileInputStream;
import java.util.Properties;

public class KobitonConfig {

    private static Properties props = new Properties();

    static {
        try {
            props.load(new FileInputStream(
                    "src/test/resources/config/config.properties"));
        } catch (Exception e) {
            throw new RuntimeException("Cannot load config.properties: "
                    + e.getMessage());
        }
    }

    // Kobiton credentials - passed via run config, NOT stored in code
    public static String getUsername() {
        return System.getProperty("kobiton.userName");
    }

    public static String getApiKey() {
        return System.getProperty("kobiton.apiKey");
    }

    public static String getApiUrl() {
        return props.getProperty("kobiton.apiUrl");
    }

    public static String getPlatform() {
        return System.getProperty("platform",
                props.getProperty("platform", "iOS"));
    }

    // iOS
    public static String getIOSDeviceName() {
        return System.getProperty("appium.deviceName",
                props.getProperty("ios.deviceName"));
    }

    public static String getIOSPlatformVersion() {
        return System.getProperty("appium.platformVersion",
                props.getProperty("ios.platformVersion"));
    }

    public static String getIOSBundleId() {
        return System.getProperty("appium.bundleId",
                props.getProperty("ios.bundleId"));
    }

    public static String getIOSApp() {
        return System.getProperty("appium.app",
                props.getProperty("ios.app"));
    }

    // Android
    public static String getAndroidDeviceName() {
        return System.getProperty("appium.deviceName",
                props.getProperty("android.deviceName"));
    }

    public static String getAndroidAppPackage() {
        return System.getProperty("appium.appPackage",
                props.getProperty("android.appPackage"));
    }

    public static String getAndroidApp() {
        return System.getProperty("appium.app",
                props.getProperty("android.app"));
    }

    // App credentials
    public static String getAppUsername() {
        return System.getProperty("app.username");
    }

    public static String getAppPassword() {
        return System.getProperty("app.password");
    }
}


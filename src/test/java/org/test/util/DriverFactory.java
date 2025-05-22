package org.test.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.AbstractDriverOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariOptions;

/**
 * Singleton Driver factory class for Thread safe driver
 */
public class DriverFactory {

	private static final Logger logger = LogManager.getLogger(DriverFactory.class);
	private static ThreadLocal<WebDriver> driverThread = new ThreadLocal<WebDriver>();
	private static DriverFactory driverInstance = null;

	private DriverFactory() { // singleton
		logger.debug(" *** Driver Factory Instantiated!");
	}

	/**
	 * DriverFactory Instance only if it is null - to maintain Singleton
	 * 
	 * @return
	 */
	public static DriverFactory getDriverInstance() {
		if (driverInstance == null) {
			synchronized (DriverFactory.class) {
				driverInstance = new DriverFactory();
			}
		}
		return driverInstance;
	}

	/**
	 * Return a new Webdriver instance that is Thread-safe using ThreadLocal
	 * 
	 * @param browser
	 * @return
	 */
	public WebDriver getWebDriver(String browser) {
		logger.debug("Getting Driver for browser: " + browser);
		if (browser.equalsIgnoreCase("chrome")) {
			ChromeOptions opt = new ChromeOptions();
			// opt.addArguments("--headless=new");
			opt.addArguments("--disable-popup-blocking");

			HashMap<String, String> map = new HashMap<String, String>();
			map.put("download.default_directory", Constants.downloadPath);
			opt.setExperimentalOption("prefs", map);

			driverThread.set(new ChromeDriver(opt));
		} else if (browser.equalsIgnoreCase("firefox")) {
			FirefoxOptions options = new FirefoxOptions();
			// options.addArguments("--headless");
			// or options.setHeadless(true);
			driverThread.set(new FirefoxDriver(options));
		} else {
			EdgeOptions options = new EdgeOptions();
			// options.addArguments("--headless");
			// or options.setHeadless(true);
			driverThread.set(new EdgeDriver(options));
		}
		return driverThread.get();
	}

	@SuppressWarnings("unchecked")
	public WebDriver getGridWebDriver(String browser, String platform) {
		DesiredCapabilities caps = new DesiredCapabilities();
		AbstractDriverOptions<?> driverOpts = null;
		logger.debug("Creating remote grid driver for :" + browser);
		// setting the platform
		if (platform.equalsIgnoreCase("Mac")) {
			caps.setPlatform(Platform.MAC);
			logger.debug("setting platform as : " + Platform.MAC);
		} else {
			caps.setPlatform(Platform.WIN11);
			logger.debug("setting platform as : " + Platform.WIN11);
		}
		// setting the browser
		// caps.setBrowserName(browser);
		// caps.setCapability("browserName", browser);

		if (browser.equalsIgnoreCase("chrome")) {
			driverOpts = new ChromeOptions();
//			ChromeOptions opt = new ChromeOptions();
			// opt.addArguments("--headless=new");
			// options.addArguments("--incognito");
//			opt.addArguments("--disable-popup-blocking");

			HashMap<String, String> map = new HashMap<String, String>();
			map.put("download.default_directory", Constants.downloadPath);
			((ChromiumOptions<ChromeOptions>) driverOpts).setExperimentalOption("prefs", map);
			((ChromiumOptions<ChromeOptions>) driverOpts).addArguments("--disable-popup-blocking");
//			driverOpts.setBrowserVersion("132.0");
		} else if (browser.equalsIgnoreCase("safari")) {
			driverOpts = new SafariOptions();
		} else if (browser.equalsIgnoreCase("firefox")) {
			driverOpts = new FirefoxOptions();
		} else if (browser.equalsIgnoreCase("edge")) {
			driverOpts = new EdgeOptions();
		} else if (browser.equalsIgnoreCase("ie")) {
			driverOpts = new InternetExplorerOptions();
		} else if (browser.equalsIgnoreCase("opera")) {
			logger.error("Error! Opera browser Not supported in Selenium 4!!");
		}

		try {
			// driverThread.set(new RemoteWebDriver(new URL(Constants.selGridURL), caps));
			driverThread.set(new RemoteWebDriver(new URL(Constants.selGridURL), driverOpts));
			logger.debug("Created RemoteWebDriver connection to: " + Constants.selGridURL);
		} catch (MalformedURLException e) {
			logger.error("Exception connecting to Remote Server:" + e.getMessage());
		}
		return driverThread.get();
	}

	public WebDriver getLTRemoteWebDriver(String browser) {
		logger.debug("Getting LambdaTest Remote Driver for browser: " + browser);
		AbstractDriverOptions<?> browserOptions = null;
		// map for setting Capabilities - LT:LambdaTest
		HashMap<String, Object> ltOptions = new HashMap<String, Object>();
		ltOptions.put("username", Constants.username);
		ltOptions.put("accessKey", Constants.accesskey);
		ltOptions.put("build", "TestAutomation");
		ltOptions.put("project", "AutomationTestApp");
		ltOptions.put("selenium_version", "4.0.0");
		ltOptions.put("w3c", true);

		if (browser.equalsIgnoreCase("chrome")) {
			browserOptions = new ChromeOptions();
			browserOptions.setPlatformName("Windows 10");
			browserOptions.setBrowserVersion("132.0");
			browserOptions.setCapability("LT:Options", ltOptions);
		} else if (browser.equalsIgnoreCase("firefox")) {
			browserOptions = new FirefoxOptions();
			browserOptions.setPlatformName("Windows 10");
			browserOptions.setBrowserVersion("134.0");
			browserOptions.setCapability("LT:Options", ltOptions);
		} else if (browser.equalsIgnoreCase("safari")) {
			browserOptions = new SafariOptions();
			browserOptions.setPlatformName("macOS Ventura");
			browserOptions.setBrowserVersion("16");
			browserOptions.setCapability("LT:Options", ltOptions);
		} else if (browser.equalsIgnoreCase("edge")) {
			browserOptions = new EdgeOptions();
			browserOptions.setPlatformName("Windows 10");
			browserOptions.setBrowserVersion("132.0");
			browserOptions.setCapability("LT:Options", ltOptions);
		} else if (browser.equalsIgnoreCase("ie")) {
			browserOptions = new InternetExplorerOptions();
			browserOptions.setPlatformName("Windows 10");
			browserOptions.setBrowserVersion("11");
			browserOptions.setCapability("LT:Options", ltOptions);
		} else if (browser.equalsIgnoreCase("opera")) {
			logger.error("Error! Opera browser Not supported in Selenium 4!!");
			// Opera is "officially" no longer supported in Selenium because there is not a
			// compatible driver for it. The driver now requires things that Opera
			// does not support and operadriver does not appear to be updated with latest
			// w3c support.
			/*
			 * browserOptions = new OperaOptions();
			 * browserOptions.setPlatformName("Windows 10");
			 * browserOptions.setBrowserVersion("97");
			 * browserOptions.setCapability("LT:Options", ltOptions);
			 */
		}

		try {
			String url = "https://" + Constants.username + ":" + Constants.accesskey + Constants.gridURL;
			logger.debug("Remote URL to connect: " + url);
			driverThread.set(new RemoteWebDriver(new URL(url), browserOptions));
		} catch (MalformedURLException e) {
			logger.error("Exception connecting to Remote Server:" + e.getMessage());
		}
		return driverThread.get();
	}

	/**
	 * Use this method only if getWebDriver(String) has already been invoked before.
	 * 
	 * @return WebDriver
	 */
	public synchronized WebDriver getDriver() {
		return driverThread.get();
	}

	public void tearDown() {
		logger.debug("driver value is: " + driverThread.get());
		driverThread.get().quit();
		driverThread.remove();
	}

}

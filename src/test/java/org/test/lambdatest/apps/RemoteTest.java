package org.test.lambdatest.apps;

import static org.testng.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.AbstractDriverOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.test.util.Constants;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class RemoteTest {

	private static final Logger logger = LogManager.getLogger(RemoteTest.class);
	RemoteWebDriver driver = null;

	@BeforeClass
	@Parameters({ "browser" })
	public void setUp(String browser) {
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
		} else if (browser.equalsIgnoreCase("edge")) {
			browserOptions = new InternetExplorerOptions();
			browserOptions.setPlatformName("Windows 10");
			browserOptions.setBrowserVersion("11");
			browserOptions.setCapability("LT:Options", ltOptions);
		} else if (browser.equalsIgnoreCase("opera")) {
			logger.error("Error! Opera browser Not supported in Selenium 4!!");
			//Opera is "officially" no longer supported in Selenium because there is not a compatible driver for it. The chromedriver now requires things that Opera does not support and operadriver does not appear to be updated with latest w3c support. 
			/*browserOptions = new OperaOptions();
			browserOptions.setPlatformName("Windows 10");
			browserOptions.setBrowserVersion("97");
			browserOptions.setCapability("LT:Options", ltOptions);*/
		}

		try {
			String url = "https://" + Constants.username + ":" + Constants.accesskey + Constants.gridURL;
			logger.debug("Remote URL to connect: " + url);
			driver = new RemoteWebDriver(new URL(url), browserOptions);
		} catch (MalformedURLException e) {
			logger.error("Exception connecting to Remote Server:" + e.getMessage());
		}
	}

	@Test(priority = 1)
	public void test() {
		driver.get("https://lambdatest.github.io/sample-todo-app/");

		// Let's mark done first two items in the list.
		driver.findElement(By.name("li1")).click();
		driver.findElement(By.name("li2")).click();

		// Let's add an item in the list.
		driver.findElement(By.id("sampletodotext")).sendKeys("Yey, Let's add it to list");
		driver.findElement(By.id("addbutton")).click();

		// Let's check that the item we added is added in the list.
		String enteredText = driver.findElement(By.xpath("/html/body/div/div/div/ul/li[6]/span")).getText();
		assertTrue(enteredText.equals("Yey, Let's add it to list"));
	}

	@AfterClass
	private void tearDown() {
		if (driver != null) {
			((JavascriptExecutor) driver).executeScript("lambda-status=" + true);
			driver.quit(); // really important statement for preventing your test execution from a timeout.
		}
	}

}

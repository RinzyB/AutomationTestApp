package org.test.heroku.app;

import static org.testng.Assert.assertTrue;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.test.util.Constants;
import org.test.util.DriverManager;
import org.testng.Reporter;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class FileUpDownLoad extends DriverManager {

	private static final Logger logger = LogManager.getLogger(FileUpDownLoad.class);
	private WebDriver driver;

	@BeforeTest
	@Parameters({ "browser", "instances" })
	public void test(String browser, int instances) {
		logger.debug("Running test for browser: " + browser + " and instances: " + instances);
		for (int i = 0; i < instances; i++) {
			ThreadLocal<WebDriver> driverThread = getDriver(browser, false);
			driver = driverThread.get();
			driver.get(Constants.fileupdownloadURL);
		}
	}

	@Test(priority = 1, enabled = false)
	public void testFileUpload() {
		WebElement addFile = driver.findElement(By.xpath("//input[@type='file']"));
		String filename = "C:\\\\Users\\\\rinzi\\\\eclipse-workspace\\\\HealthCareTestApp\\\\src\\\\main\\\\resources\\\\fileupload.png";
		addFile.sendKeys(filename);
		/**
		 * If testing in remote desktop, then use the setFileDetector() method, the
		 * RemoteWebDriver acknowledges that we are uploading files for Selenium testing
		 * over either a local or remote machine.
		 * 
		 * try { DesiredCapabilities cap=new DesiredCapabilities(); RemoteWebDriver
		 * rdriver = new RemoteWebDriver(new URL(""), cap); rdriver.setFileDetector(new
		 * LocalFileDetector()); } catch (MalformedURLException e) {
		 * e.printStackTrace(); }
		 */

		driver.findElement(By.xpath("//button[@type='submit']")).click();
		WebElement del = driver.findElement(By.xpath("(//input[@name='delete'])[1]"));
		logger.debug(logId() + " : Is delete button visible? " + del.isDisplayed());
		assertTrue(del.isDisplayed());
		del.click();
		// driver.findElement(By.xpath("(//input[@name='delete'])[2]")).click();
		driver.findElement(By.xpath("//button[@type='button']")).click();

		Reporter.log("Successfully Uploaded File and deleted!");
	}

	@Test(priority = 2)
	public void testFileDownload() {
		String downloadfileName = "chromedriver_win32.zip";
		driver.get("https://chromedriver.storage.googleapis.com/index.html?path=114.0.5735.90/");
		driver.findElement(By.linkText(downloadfileName)).click(); // the default download directory is set in
																	// ChromeOptions, so the location popup will not appear-
																	// download.default_directory
		// wait for the file to complete download
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// verify if file downloaded
		File file = new File(Constants.downloadPath + downloadfileName);
		assertTrue(file.exists());
	}

	@AfterTest
	public void done() {
		teardown();
	}

}

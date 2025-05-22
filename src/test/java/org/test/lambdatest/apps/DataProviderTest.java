package org.test.lambdatest.apps;

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.test.util.Constants;
import org.test.util.DriverFactory;
import org.test.util.DriverManager;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DataProviderTest extends DriverManager {

	private static final Logger logger = LogManager.getLogger(DataProviderTest.class);
	
	@FindBy(id="id") 
	WebElement id;
	
	public DataProviderTest () {
		PageFactory.initElements(driver, this);
	}

	@DataProvider(parallel = false)
	public String[][] sumData() {
		String[][] values = { { "1", "9" }, { "8", "2" }, { "7", "3" }, { "6", "4" }, { "5", "5" }, { "9", "1" } };
		return values;
	}

	@Test(priority = 1, dataProvider = "sumData")
	public void testSum(String sum1, String sum2) {
		driver.get(Constants.lambdaTestSimpleForm);  // using driver from DriverManager since not parallel run
		driver.findElement(By.id("sum1")).sendKeys(sum1);
		driver.findElement(By.id("sum2")).sendKeys(sum2);
		driver.findElement(By.xpath("(//button[@type=\"button\"])[2]")).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#addmessage")));

		File file = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(file, new File(Constants.screenshotsFolder + "\\sum_" + sum1 + ".png"));
			// FileHandler.copy(file, new File(Constants.screenshotsFolder + "\\sum_" + sum1
			// + ".png")); --> does not replace file if exists
			logger.debug(Thread.currentThread().getId() + " : SS taken for: " + sum1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		WebElement sum = driver.findElement(By.cssSelector("#addmessage"));
		logger.debug(Thread.currentThread().getId() + " : Run: " + sum1 + " + " + sum2 + " = " + sum.getText());
		assertEquals(sum.getText(), "10");
	}

	@DataProvider(parallel = true)
	public String[][] sumParallelData() {
		String[][] values = { { "1", "9" }, { "8", "2" }, { "7", "3" }, { "6", "4" }, { "5", "5" }, { "9", "1" } };
		return values;
	}

	@Test(priority = 2, dataProvider = "sumParallelData")
	public void testParallelSum(String sum1, String sum2) {
		WebDriver driver = DriverFactory.getDriverInstance().getWebDriver("chrome");
		driver.get(Constants.lambdaTestSimpleForm);
		driver.findElement(By.id("sum1")).sendKeys(sum1);
		driver.findElement(By.id("sum2")).sendKeys(sum2);
		driver.findElement(By.xpath("(//button[@type=\"button\"])[2]")).click();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#addmessage")));

		File file = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(file, new File(Constants.screenshotsFolder + "\\sum_" + sum1 + ".png"));
			// FileHandler.copy(file, new File(Constants.screenshotsFolder + "\\sum_" + sum1
			// + ".png")); --> does not replace file if exists
			logger.debug(Thread.currentThread().getId() + " : SS taken for: " + sum1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		WebElement sum = driver.findElement(By.cssSelector("#addmessage"));
		logger.debug(Thread.currentThread().getId() + " : Run: " + sum1 + " + " + sum2 + " = " + sum.getText());
		assertEquals(sum.getText(), "10");
		DriverFactory.getDriverInstance().tearDown(); // tearDown for each parallel run
	}

}

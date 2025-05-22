package org.test.healthcare.cura;

import static org.testng.Assert.assertEquals;
import org.test.util.*;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.test.util.Constants;
import org.test.util.DriverManager;
import org.testng.Reporter;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class HomePageTest extends DriverManager {

	private static final Logger logger = LogManager.getLogger(HomePageTest.class);
	private WebDriver driver;
	private ReadWriteDataFile dataFile = null;

	@BeforeTest
	@Parameters({ "browser", "instances" })
	public void test(String browser, int instances) {
		// System.setProperty("log4j2.debug", "true");
		LoggerContext context = (LoggerContext) LogManager.getContext(false);
		Configuration config = context.getConfiguration();
		logger.debug("Loaded Log4j configuration file: " + config.getConfigurationSource().getLocation());

		logger.debug("Running test for browser: " + browser + " and instances: " + instances);
		for (int i = 0; i < instances; i++) {
			ThreadLocal<WebDriver> driverThread = getDriver(browser, false);
			driver = driverThread.get();
			driver.get(Constants.curaURL);
		}
		dataFile = new ReadWriteDataFile(Constants.excelDataFile, Constants.curaDataSheetName);
	}

	@Test(priority = 1)
	public void testHeaderMsg() {
		String headertxt = driver.findElement(By.xpath("//h1")).getText();
		logger.debug(logId() + " : Text in Header is: " + headertxt);
		assertEquals(headertxt, "CURA Healthcare Service");
		assertEquals(driver.findElement(By.xpath("//h3")).getText(), "We Care About Your Health");
		Reporter.log("Header retrieved from Page was: " + headertxt);
	}

	@Test(priority = 2)
	public void testApptButton() {
		WebElement elem = driver.findElement(By.cssSelector("#btn-make-appointment"));
		assertEquals(elem.getText(), "Make Appointment");
		logger.debug(logId() + " : Make Appointment Button");
		Reporter.log("Found Make Appointment!");
	}

	@Test(priority = 3)
	public void testMenu() {
		WebElement menu = driver.findElement(By.xpath("//a[@id='menu-toggle']"));
		WebElement menuList = driver.findElement(By.xpath("//ul[@class='sidebar-nav']"));
		assertFalse(menuList.isDisplayed());
		menu.click();
		assertTrue(menuList.isDisplayed());
		logger.debug(logId() + " :menuList.isDisplayed() is " + menuList.isDisplayed());
		// logger.debug(logId() + " : Menu List : " + menuList.getText());
		List<WebElement> lists = menuList.findElements(By.tagName("li"));
		for (WebElement list : lists) {
			logger.debug(logId() + " : List Item: " + list.getText());
			if (list.getText().equalsIgnoreCase("Login")) {
				list.click();
			}
		}
	}

	@Test(priority = 4)
	public void testLogin() {
		WebElement user = driver.findElement(By.xpath("//input[@id='txt-username']"));
		user.sendKeys("John Doe");
		driver.findElement(By.cssSelector("#txt-password")).sendKeys("ThisIsNotAPassword");
		driver.findElement(By.xpath("//button[@id='btn-login']")).submit();
		boolean apptDisp = driver.findElement(By.cssSelector("#appointment")).isDisplayed();
		logger.debug(logId() + " : Login was: " + apptDisp);
		assertTrue(apptDisp, "Login Success!!");
	}

	@Test(priority = 5)
	public void testMakeAppt() {
		
		logger.debug("Excel First Data: " + dataFile.readFromFile(1, 0));

		WebElement facility = driver.findElement(By.xpath("//select[@id='combo_facility']"));
		Select selFacility = new Select(facility);
		selFacility.selectByValue(dataFile.readFromFile(1, 0));
		logger.debug(logId() + " : selected value is: " + selFacility.getFirstSelectedOption().getText());
		if (dataFile.readFromFile(1, 1).equalsIgnoreCase("Yes")) {
			driver.findElement(By.cssSelector("#chk_hospotal_readmission")).click();
		}
		if (dataFile.readFromFile(1, 2).equalsIgnoreCase("Medicaid")) {
			driver.findElement(By.cssSelector("#radio_program_medicaid")).click();
		} else if (dataFile.readFromFile(1, 2).equalsIgnoreCase("Medicare")) {
			driver.findElement(By.cssSelector("#radio_program_medicare")).click();
		} else if (dataFile.readFromFile(1, 2).equalsIgnoreCase("None")) {
			driver.findElement(By.cssSelector("#radio_program_none")).click();
		}
		selectCalendarDate(dataFile);
		WebElement comment = driver.findElement(By.cssSelector("#txt_comment"));
		comment.click();
		comment.sendKeys(dataFile.readFromFile(1, 6));
		logger.debug(logId() + " : comment: " + comment.getAttribute("value"));
		driver.findElement(By.cssSelector("#btn-book-appointment")).submit();
		String apptMsg = driver.findElement(By.xpath("(//div[@class='container'])[1]")).getText();
		logger.debug(logId() + " : Appointment msg: " + apptMsg);
		// logger.debug(logId() + " : HTML code: "+
		// driver.findElement(By.xpath("(//div[@class='container'])[1]")).getAttribute("innerHTML"));
		dataFile.writeInFile(2, 7, apptMsg);
	}

	private void selectCalendarDate(ReadWriteDataFile ex) {
		WebElement date = driver.findElement(By.cssSelector("#txt_visit_date"));
		date.click();
		WebElement month = driver
				.findElement(By.cssSelector("div[class='datepicker-days'] th[class='datepicker-switch']"));
		String mon = ex.readFromFile(1, 3);
		logger.debug(
				logId() + " : Current selected month is: " + month.getText() + " and month from data file is: " + mon);
		while (!month.getText().trim().equalsIgnoreCase(mon)) {
			driver.findElement(By.cssSelector("div[class='datepicker-days'] th[class='next']")).click();
			logger.debug(logId() + " : new selected month is: " + month.getText());
		}
		WebElement day = driver.findElement(By.cssSelector("tbody tr:nth-child(3) td:nth-child(3)"));
		logger.debug(logId() + " : Day is: " + day.getText());
		day.click();

		WebElement div = driver.findElement(By.xpath(
				"//div[@class='datepicker datepicker-dropdown dropdown-menu datepicker-orient-left datepicker-orient-top']"));
		logger.debug(logId() + " : Again Calendar popup displayed is:" + div.isDisplayed());
		/*
		 * JavascriptExecutor js = (JavascriptExecutor) driver;
		 * js.executeScript("arguments[0].style.display='none';", div);
		 * logger.debug(logId() +
		 * " : Again Calendar popup after javascript displayed is:" +
		 * div.isDisplayed());
		 */
		logger.debug(logId() + " :Date Selected: " + date.getAttribute("value"));
		assertEquals(date.getAttribute("value"), ex.readFromFile(1, 5));
	}

	@AfterTest
	public void done() {
		dataFile.closeFile();
		teardown();
	}

}

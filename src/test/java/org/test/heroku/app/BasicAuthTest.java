package org.test.heroku.app;

import static org.testng.Assert.assertEquals;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.test.util.Constants;
import org.test.util.DriverManager;
import org.testng.Reporter;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class BasicAuthTest extends DriverManager {

	private static final Logger logger = LogManager.getLogger(BasicAuthTest.class);
	private WebDriver driver;

	@BeforeTest
	@Parameters({ "browser", "instances" })
	public void test(String browser, int instances) {
		logger.debug("Running test for browser: " + browser + " and instances: " + instances);
		for (int i = 0; i < instances; i++) {
			ThreadLocal<WebDriver> driverThread = getDriver(browser, false);
			driver = driverThread.get();

		}
	}

	@Test
	public void testAuth() {
		String authMsg = null;
		try {
			ResultSet rs = fetchDataFromDB(Constants.query);
			logger.debug(logId() + " Id is :" + rs.getString(1));
			driver.get("https://" + rs.getString(2) + ":" + rs.getString(3) + Constants.authURL);
			authMsg = driver.findElement(By.xpath("//p")).getText();
			logger.debug(logId() + " : Post Auth msg is: " + authMsg);
		} catch (SQLException e) {
			logger.error(logId() + " : Exception occured in SQL execution : \n" + getExceptionDetails(e));
		}

		assertEquals(authMsg.trim(), "Congratulations! You must have the proper credentials.");
		Reporter.log("Successfully Authenticated!");
	}

	@AfterTest
	public void done() {
		teardown();
	}

}

package org.test.handson;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class LoginTest {

	private static final Logger logger = LogManager.getLogger(LoginTest.class);

	private LoginPage loginPg;

	@BeforeClass
	@Parameters("browser")
	public void setUp(String browser) {
		loginPg = new LoginPage(browser);
	}

	@Test(priority = 1)
	public void verifyHomePageTitle() {
		Assert.assertEquals(loginPg.getPageTitle(), "Dashboard - MyApp");
	}

	@Test(priority = 2)
	public void testLoginButton() {
		logger.debug("Testing Login..");
		loginPg.selectCountry("USA");
		loginPg.login("TestUser", "TestUser");
		Assert.assertEquals(loginPg.getLoginMsg(), "Login failed! Please ensure the username and password are valid.");
	}

	@Test(priority = 3)
	public void testDelete() {
		loginPg.handleAlertAfterDelete();
	}

	@AfterClass
	public void tearDown() {
		loginPg.tearDown();
	}

}

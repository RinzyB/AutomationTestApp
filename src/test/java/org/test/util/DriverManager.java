package org.test.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.test.listeners.FailureListener;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;

@Listeners({ FailureListener.class })
public class DriverManager {

	private static final Logger logger = LogManager.getLogger(DriverManager.class);
	private static ThreadLocal<WebDriver> driverThread = new ThreadLocal<WebDriver>();
	public WebDriver driver;
	public Actions action = null;
	private Connection conn = null;

	/**
	 * Initializing the driver and actions
	 * 
	 * @param browser
	 */
	@BeforeClass(alwaysRun = true)
	@Parameters({ "browser", "platform" })
	public void initializeDriver(String browser, String platform) {
		logger.debug("Getting driver for browser:" + browser);
		driver = DriverFactory.getDriverInstance().getWebDriver(browser);
//		driver = DriverFactory.getDriverInstance().getGridWebDriver(browser, platform);
//		driver = DriverFactory.getDriverInstance().getLTRemoteWebDriver(browser);
		action = new Actions(driver);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		driver.manage().window().maximize();
	}

	/**
	 * Method to close down all connections which the program test finishes. To be
	 * invoked post each test execution using @AfterClass
	 * 
	 */
	@AfterClass(alwaysRun = true)
	public void closedown() {
		logger.debug("driver value is: " + driver);
		DriverFactory.getDriverInstance().tearDown();
		// db
		try {
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			logger.error("Exception occured while closing SQL connection :" + getExceptionDetails(e));
		}
		logger.debug("TearDown completed!");
	}

	public String logId() {
		return "ThreadId: " + Thread.currentThread().getId();
	}

	/**
	 * Method to fetch the ResultSet from the DB. This method connects to the DB,
	 * executes the passed Query and returns the ResultSet.
	 * 
	 * @param query
	 * @return
	 */
	public ResultSet fetchDataFromDB(String query) {
		ResultSet rs = null;
		try {
			// jdbc:mysql://ipaddress:portnumber/db_name
			conn = java.sql.DriverManager.getConnection(Constants.dbURL, Constants.dbUser, Constants.dbPass);
			logger.debug(logId() + " : Connection established successfully!!");
			Statement stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			rs.next();
			logger.debug(logId() + " : Executed Query: " + query);
		} catch (SQLException e) {
			logger.error(logId() + " : Exception occured in SQL execution : \n" + getExceptionDetails(e));
		}
		return rs;
	}

	/**
	 * StackTrace Builder - prints the message from the Exception ex
	 * 
	 */
	public String getExceptionDetails(Exception ex) {
		// get the stack trace
		StringBuilder stackTrace = new StringBuilder();
		stackTrace.append(ex.getMessage() + " \n StackTrace Details: \n");
		for (StackTraceElement element : ex.getStackTrace()) {
			stackTrace.append(element.toString()).append("\n");
		}
		return stackTrace.toString();
	}

	@Deprecated
	@Parameters({ "browser", "instances" })
	public ThreadLocal<WebDriver> getDriver(String browser, boolean isRecord) {
		initWebDriver(browser);
		driverThread.set(driver);
		driverThread.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		driverThread.get().manage().window().maximize();
		logger.debug("Getting threadlocal driver for browser:" + browser);
		driver = driverThread.get();
		return driverThread;
	}

	@Deprecated
	private void initWebDriver(String browser) {
		// WebDriver driver;
		if (browser.equalsIgnoreCase("chrome")) {
			ChromeOptions opt = new ChromeOptions();
			// opt.addArguments("--headless=new");
			// options.addArguments("--incognito");
			opt.addArguments("--disable-popup-blocking");

			HashMap<String, String> map = new HashMap<String, String>();
			map.put("download.default_directory", Constants.downloadPath);
			opt.setExperimentalOption("prefs", map);

			driver = new ChromeDriver(opt);
		} else if (browser.equalsIgnoreCase("firefox")) {
			driver = new FirefoxDriver();
		} else {
			driver = new EdgeDriver();
		}
	}

	@Deprecated
	public void teardown() {
		DriverFactory.getDriverInstance().tearDown();
		driverThread.get().quit();
		driverThread.remove();
		// db
		try {
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			logger.error(logId() + " : Exception occured while closing SQL connection :" + getExceptionDetails(e));
		}
	}

}

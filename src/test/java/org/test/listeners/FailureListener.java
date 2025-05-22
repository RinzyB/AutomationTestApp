package org.test.listeners;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.FileHandler;
import org.test.util.Constants;
import org.test.util.DriverFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

public class FailureListener implements ITestListener {

	private static final Logger logger = LogManager.getLogger(FailureListener.class);

	private void appLog(ITestResult result, String msg) {
		logger.debug(" Method: " + result.getMethod().getMethodName() + " Listener: " + msg);
	}

	@Override
	public void onTestStart(ITestResult result) {
		appLog(result, "Test Started...");
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		appLog(result, "Test Success!!");
	}

	/**
	 * On TestCase failure, take screenshot and print the log message
	 */
	@Override
	public void onTestFailure(ITestResult result) {
		appLog(result, "Listener : Test Failed!");
		String screenshotPath = Constants.screenshotsFolder + result.getMethod().getMethodName() + ".png";
		WebDriver driver = DriverFactory.getDriverInstance().getDriver();
		if (driver != null && driver instanceof WebDriver) {
			File file = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

			try {
				FileHandler.copy(file, new File(screenshotPath));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Reporter.log("Saved Screenshot for: " + result.getMethod().getMethodName());
		Reporter.log("<br><img src='" + screenshotPath + "' height='200' width='300'> Screenshot for failure in : "
				+ result.getMethod().getMethodName() + "</img><br>");
		appLog(result, "Attached Screenshot and LogMsg!");
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		appLog(result, "Test Skipped...");
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		appLog(result, "Test Failed But within success...");
	}

	@Override
	public void onTestFailedWithTimeout(ITestResult result) {
		appLog(result, "Test Failed withTimeout..");
	}

	@Override
	public void onStart(ITestContext context) {
		// TODO Auto-generated method stub
		ITestListener.super.onStart(context);
	}

	@Override
	public void onFinish(ITestContext context) {
		// TODO Auto-generated method stub
		ITestListener.super.onFinish(context);
	}

}

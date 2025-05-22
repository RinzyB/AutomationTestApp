package org.test.listeners;

import static io.qameta.allure.model.Status.BROKEN;
import static io.qameta.allure.model.Status.FAILED;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.test.util.DriverFactory;

import io.qameta.allure.Attachment;
import io.qameta.allure.listener.TestLifecycleListener;
import io.qameta.allure.model.TestResult;

public class AllureTestListener implements TestLifecycleListener {
	private static final Logger logger = LogManager.getLogger(AllureTestListener.class);

	@Attachment(value = "Page Screenshot", type = "image/png")
	public byte[] saveScreenshot(WebDriver driver) {
		logger.debug("Listener : Taking Screenshot... ");
		return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
	}

	@Attachment(value = "{0}", type = "text/plain")
	public static String saveTextLog(String message) {
		logger.debug("AllureTestListener : Saving Text msg to attach to report: " + message);
		return message;
	}

	@Override
	public void beforeTestStop(TestResult result) {
		if (FAILED == result.getStatus() || BROKEN == result.getStatus()) {
			logger.debug(" Method: " + result.getName() + " Listener : Test Failed!");
			WebDriver driver = DriverFactory.getDriverInstance().getDriver();
			if (driver != null && driver instanceof WebDriver)
				saveScreenshot(driver);

			saveTextLog("Saved Screenshot for testcase: " + result.getName());
			//Allure.addAttachment("test", "test");
		}
	}

}

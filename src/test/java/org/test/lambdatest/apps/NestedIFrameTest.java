package org.test.lambdatest.apps;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.test.util.Constants;
import org.test.util.DriverManager;
import org.testng.Reporter;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.Story;

public class NestedIFrameTest extends DriverManager {

	private static final Logger logger = LogManager.getLogger(NestedIFrameTest.class);

	@Test(priority = 1, enabled = true, groups = "Sanity", invocationCount = 1)
	@Description("Verify IFrames")
	@Epic("EP005")
	@Feature("IFrames Feature")
	@Story("S5: Check IFrames Functionality")
	@Step("Verify IFrames feature")
	@Severity(SeverityLevel.NORMAL)
	public void testIFrameByName() {
		driver.get(Constants.lambdaTestNestedIFrames);

		driver.switchTo().frame("frame-top");
		String text = driver.findElement(By.tagName("p")).getText();
		logger.debug("Message on First Frame: " + text);

		driver.switchTo().defaultContent(); // going back to default main page

		SoftAssert soft = new SoftAssert();
		soft.assertEquals(text, "Top"); // correct is Top

		Reporter.log("Executed testcase : testIFrameByName");
		logger.debug("This is the msg after assert stmt ******");
		soft.assertAll();
	}

	@Test(priority = 2, enabled = true, dependsOnMethods = { "testIFrameByName" }, groups = {"Smoke","Sanity"})
	@Description("Verify Nested Frames")
	@Epic("EP005")
	@Feature("Nested IFrames Feature")
	@Story("S6: Check IFrames Functionality")
	@Step("Verify Nested IFrames feature")
	@Severity(SeverityLevel.CRITICAL)
	public void testNestedFrame() {
		driver.get(Constants.lambdaTestNestedIFrames);

//		driver.switchTo().frame("frame-bottom"); // bottom Frame
		driver.switchTo().frame(1); // bottom Frame index
		List<WebElement> frames = driver.findElements(By.tagName("frame"));
		logger.debug("Total Frames Found inside: " + frames.size());
		driver.switchTo().frame(2); // 2nd frame inside nested frame
		logger.debug("Message in Nested Frame: " + driver.findElement(By.tagName("p")).getText());
		driver.switchTo().defaultContent();
		assertEquals(frames.size(), 3); // correct is 3

		Reporter.log("Successful!");
	}
}

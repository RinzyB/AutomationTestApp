package org.test.lambdatest.apps;

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.Color;
import org.test.util.Constants;
import org.test.util.DriverManager;
import org.testng.Reporter;
import org.testng.annotations.Test;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.Story;

public class DragAndDrop extends DriverManager {

	private static final Logger logger = LogManager.getLogger(DragAndDrop.class);

	@Test(priority = 1, enabled = true, groups = "Smoke")
	@Description("Verify Drag and Drop Elements")
	@Epic("EP001")
	@Feature("DragDrop Feature")
	@Story("S1: Check DragDrop Functionality")
	@Step("Verify Drag and Drop feature")
	@Severity(SeverityLevel.NORMAL)
	public void testDragDrop() {
		driver.get(Constants.lambdaTestDragDrop);
		WebElement drop = driver.findElement(By.id("mydropzone"));
		// List<WebElement> dropItems =
		// driver.findElements(By.xpath("//div[@id='droppedlist']/span"));
		logger.debug("Number of Items dropped at start : "
				+ driver.findElements(By.xpath("//div[@id='droppedlist']/span")).size());
		List<WebElement> dragItems = driver.findElements(By.xpath("//div[@id='todrag']/span"));
		for (WebElement elem : dragItems) {
			action.dragAndDrop(elem, drop).perform();
		}
		List<WebElement> dropItems = driver.findElements(By.xpath("//div[@id='droppedlist']/span"));
		logger.debug("Number of Items dropped after : " + dropItems.size());
		assertEquals(dropItems.size(), 2); // correct is 2
		Reporter.log("Successful!");
	}

	@Test(priority = 2, enabled = true, groups = "Sanity")
	@Description("Verify Drag and Drop 2 Elements")
	@Epic("EP001")
	@Feature("DragDrop2 Feature")
	@Story("S2: Check DragDrop Functionality")
	@Step("Verify Drag and Drop feature")
	@Severity(SeverityLevel.MINOR)
	public void testDragDropBy() {
		driver.get(Constants.lambdaTestDragDrop);
		WebElement moveElem = driver.findElement(By.id("draggable"));
		WebElement dropElem = driver.findElement(By.id("droppable"));
		logger.debug("Element Location at start : " + moveElem.getLocation().getX() + " , "
				+ moveElem.getLocation().getY());
		logger.debug("Droppable text is : " + dropElem.getText());
		try {
//			action.moveToElement(moveElem).perform();
//			action.dragAndDropBy(moveElem, 150, 20).perform(); // works in chrome
			action.dragAndDropBy(moveElem, 170, 12).perform();

			File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(screenshotFile, new File(Constants.screenshotsFolder + "\\DragDropByResult.png"));
			// FileHandler.copy(screenshotFile, screenshotFile);

			logger.debug("Element Location after drag : " + moveElem.getLocation().getX() + " , "
					+ moveElem.getLocation().getY());
			logger.debug("Droppable text after drag is : " + dropElem.getText());
			assertEquals(dropElem.getText(), "Dropped!");
		} catch (IOException e) {
			logger.error("Exception occured :" + getExceptionDetails(e));
			e.printStackTrace();
		}

		Reporter.log("Successful!");
	}

	@Test(priority = 3, enabled = true, groups = {"Smoke","Sanity"})
	@Description("Verify Mouse Hover")
	@Epic("EP001")
	@Feature("Hover Feature")
	@Story("S3: Check Hover Functionality")
	@Step("Verify Hover feature")
	@Severity(SeverityLevel.CRITICAL)
	public void testMouseHover() {
		driver.get(Constants.lambdaTestHover);
		WebElement hover = driver.findElement(By.xpath("(//div[@class=\"flex ml-15\"])[1]/div[1]"));
		// Color initCol=Color.fromString(hover.getCssValue("color"));
		logger.debug("Element Color before hovering is : "
				+ Color.fromString(hover.getCssValue("color")).asHex());
		action.moveToElement(hover).click().perform();
//		action.click(hover).perform(); // directly click on element without movetoelement: new in sel 4
		logger.debug("Element Color after hovering is : "
				+ Color.fromString(hover.getCssValue("color")).asHex());
		assertEquals(Color.fromString(hover.getCssValue("color")).asHex(), "#6ac27e");
	}
}

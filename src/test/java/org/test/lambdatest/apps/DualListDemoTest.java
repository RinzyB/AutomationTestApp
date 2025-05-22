package org.test.lambdatest.apps;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.test.util.Constants;
import org.test.util.DriverManager;
import org.testng.annotations.Test;

public class DualListDemoTest extends DriverManager {

	private final static Logger logger = LogManager.getLogger(DualListDemoTest.class);

	@Test(priority = 1, enabled = true, groups = "Regression")
	public void testSearch() {
		driver.get(Constants.lambdaTestDualList);
		WebElement srchElem = driver.findElement(By.xpath("(//input[@name='SearchDualList'])[1]"));
		List<WebElement> firstList = driver.findElements(By.xpath("//div[@class='well text-right']/ul/li"));
		printList(firstList);
		srchElem.sendKeys("i");
		printList(firstList);
	}

	private void printList(List<WebElement> list) {
		logger.debug("Visible Elems are: ");
		for (WebElement item : list) {
			if (!item.getAttribute("style").equals("display: none;")) {
				logger.debug(item.getText());
			}
		}
	}

	@Test(priority = 2, enabled = true, groups = "Regression")
	public void testListMovement() {
		driver.get(Constants.lambdaTestDualList);
		List<WebElement> secondList = driver.findElements(By.xpath("//div[@class='well']/ul/li"));
		logger.debug("Second List size before: " + secondList.size());
		printList(secondList);

		List<WebElement> firstList = driver.findElements(By.xpath("//div[@class='well text-right']/ul/li"));
		for (WebElement item : firstList) {
			if (!item.getAttribute("style").equals("display: none;")) {
				logger.debug(item.getText());
				if (item.getText().equalsIgnoreCase("Danville") || item.getText().equalsIgnoreCase("Mynamaki")) {
					item.click();
				}
			}
		}
		driver.findElement(By.xpath("//button[text()=' >']")).click();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<WebElement> newSecondList = driver.findElements(By.xpath("//div[@class='well']/ul/li"));
		printList(newSecondList);
		logger.debug("Second List size after: " + newSecondList.size());
		assertEquals(newSecondList.size(), 5);
	}
}

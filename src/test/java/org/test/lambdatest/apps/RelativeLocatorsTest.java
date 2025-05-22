package org.test.lambdatest.apps;

import static org.testng.Assert.assertEquals;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.locators.RelativeLocator;
import org.test.util.Constants;
import org.test.util.DriverManager;
import org.testng.annotations.Test;

public class RelativeLocatorsTest extends DriverManager {

	private static final Logger logger = LogManager.getLogger(RelativeLocatorsTest.class);
	private WebDriver driver;

	public RelativeLocatorsTest() {
		driver.get(Constants.practiceForm);
	}

	@Test (priority = 1, invocationCount=1)
	public void testAbove() {
		WebElement elem = driver.findElement(By.cssSelector("a[href='login.php']"));
		WebElement findElem = driver.findElement(RelativeLocator.with(By.tagName("a")).above(elem));
		logger.debug("Found above element is: " + findElem.getText());
		assertEquals(findElem.getText(), "Practice Form");
	}
	
	@Test (priority = 2)
	public void testBelow() {
		WebElement elem = driver.findElement(By.cssSelector("a[href='login.php']"));
		WebElement findElem = driver.findElement(RelativeLocator.with(By.tagName("a")).below(elem));
		logger.debug("Found below element is: " + findElem.getText());
		assertEquals(findElem.getText(), "Register");
	}
	
	@Test (priority = 3)
	public void testLeftOf() {
		WebElement elem = driver.findElement(By.xpath("//*[@id='name']"));
		WebElement findElem = driver.findElement(RelativeLocator.with(By.tagName("label")).toLeftOf(elem));
		logger.debug("Found leftof element is: " + findElem.getText());
		assertEquals(findElem.getText(), "Name:");
	}
	
	@Test (priority = 4)
	public void testRightOf() {
		WebElement elem = driver.findElement(By.xpath("/html/body/div/header/div[1]/a"));
		WebElement findElem = driver.findElement(RelativeLocator.with(By.tagName("h1")).toRightOf(elem));
		logger.debug("Found rightof element is: " + findElem.getText());
		assertEquals(findElem.getText(), "Selenium - Automation Practice Form");
	}
	
	@Test (priority = 5)
	public void testNear() {
		WebElement elem = driver.findElement(By.xpath("//*[@id='practiceForm']/div[7]/div/div/div[1]/label"));
		WebElement findElem = driver.findElement(RelativeLocator.with(By.tagName("input")).near(elem));
		findElem.click();
		logger.debug("Found near() element is: " + findElem.isSelected());
		assertEquals(findElem.isSelected(),true);
	}
	
	@Test (priority = 6)
	public void testChaining() {
		WebElement elem = driver.findElement(By.xpath("//*[@id='practiceForm']/div[1]/label"));
		WebElement elem2 = driver.findElement(By.xpath("//*[@id='practiceForm']/div[2]/label"));
		WebElement findElem = driver.findElement(RelativeLocator.with(By.tagName("input")).above(elem2).toRightOf(elem));
		findElem.sendKeys("TestChaining");
		logger.debug("Found chaining element is: " + findElem.getAttribute("value"));
		assertEquals(findElem.getAttribute("value"), "TestChaining");
	}
	
}

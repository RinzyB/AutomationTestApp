package org.test.cheapoair;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.test.util.Constants;
import org.test.util.DriverManager;
import org.testng.annotations.Test;

public class FlightBookingTest extends DriverManager {

	@Test(priority = 1, enabled = true)
	public void testDragDrop() {
		driver.get(Constants.cheapoair);
		WebElement from = driver.findElement(By.id("fs_originCity_0"));
		from.clear();
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", from); // explicitly clearing the field as it does not work in Chrome, works fine in Firefox
		from.sendKeys("San Francisco");
	}
}

package org.test.lambdatest.apps;

import static org.testng.Assert.assertEquals;

import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.test.util.Constants;
import org.test.util.DriverManager;
import org.testng.Reporter;
import org.testng.annotations.Test;

public class WindowHandle extends DriverManager {

	private static final Logger logger = LogManager.getLogger(WindowHandle.class);

	@Test(priority = 1, enabled = false)
	public void testChildWindow() {
		driver.get(Constants.lambdaTestWindow);
		logger.debug(logId() + " Parent Title is: " + driver.getTitle());
		driver.findElement(By.linkText("Like us On Facebook")).click();
		String parentHandle = driver.getWindowHandle();
		Set<String> whandles = driver.getWindowHandles();
		// List<String> handles = new ArrayList<String>(whandles);
		// handles.get(1); // to get only child window - facebook link
		for (String handle : whandles) { // contains all the windows (the parent and children)
			driver.switchTo().window(handle);
			logger.debug(logId() + " Title is -- " + driver.getTitle());
			// driver.close(); // closing only the current window
		}
		driver.switchTo().window(parentHandle);
		logger.debug(logId() + " Back to parent window, Parent Title is: " + driver.getTitle());
		assertEquals(driver.getTitle(), "Selenium Grid Online | Run Selenium Test On Cloud");
		Reporter.log("Successful!");
	}

	@Test(priority = 2)
	public void testFrames() {
		driver.get(Constants.lambdaTestiFrame);
		logger.debug(logId() + " Parent Header is: " + driver.findElement(By.xpath("//h1")).getText());
		//driver.switchTo().frame(1); // switchTo frame using the iframe number or
		// element
		
		driver.switchTo().frame(driver.findElement(By.xpath("//iframe[@id='iFrame2']")));
		logger.debug(logId() + " iFrame Header is: " + driver.findElement(By.xpath("//h1")).getText());
	}

}

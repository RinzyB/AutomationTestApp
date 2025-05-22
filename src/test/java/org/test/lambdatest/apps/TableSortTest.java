package org.test.lambdatest.apps;

import static org.testng.Assert.assertEquals;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.test.util.Constants;
import org.test.util.DriverManager;
import org.testng.annotations.Test;

public class TableSortTest extends DriverManager {

	private static final Logger logger = LogManager.getLogger(TableSortTest.class);

	@Test(priority = 1)
	public void sortByNameTest() {
		driver.get(Constants.lambdaTestTableSort);
		WebElement name = driver.findElement(By.xpath("//tbody/tr[1]/td[1]"));
		logger.debug("First Name in the table is : " + name.getText());
		driver.findElement(By.xpath("//th[1]")).click();
		WebElement sortedName = driver.findElement(By.xpath("//tbody/tr[1]/td[1]"));
		logger.debug("First Name in the table after Name sort is : " + sortedName.getText());
		assertEquals(sortedName.getText(), "T. Fitzpatrick");
	}

	@Test(priority = 2)
	public void goNextTest() {
		logger.debug("Testing the Next button..");
		while (!driver.findElement(By.id("example_next")).getAttribute("class").contains("disabled")) { //<a class="paginate_button next disabled" ...
			driver.findElement(By.id("example_next")).click();
			logger.debug("Number of rows in this page is:" + driver.findElements(By.xpath("//tbody/tr")).size());
		}
	}

	@Test(priority = 3)
	public void sortByPositionTest() {
		WebElement pos = driver.findElement(By.xpath("//tbody/tr[1]/td[2]")); // 2nd column
		logger.debug("First Position in the table is : " + pos.getText());
		driver.findElement(By.xpath("//th[2]")).click(); // 2nd header column
		WebElement sortedPos = driver.findElement(By.xpath("//tbody/tr[1]/td[2]"));
		logger.debug("First Name in the table after Name sort is : " + sortedPos.getText());
		assertEquals(sortedPos.getText(), "Accountant");
	}

	@Test(priority = 4)
	public void searchTest() {
		driver.findElement(By.xpath("//input[@type='search']")).sendKeys("san");
		assertEquals(driver.findElements(By.xpath("//tbody/tr")).size(), 4);
	}

}

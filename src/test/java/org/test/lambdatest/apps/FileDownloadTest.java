package org.test.lambdatest.apps;

import static org.testng.Assert.assertTrue;

import java.io.File;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.test.util.Constants;
import org.test.util.DriverManager;
import org.testng.annotations.Test;

public class FileDownloadTest extends DriverManager {

	@Test (priority=1, enabled=true, groups = "Regression")
	public void testFileDownload() {
		driver.get(Constants.lambdaTestFileDownload);
		WebElement text = driver.findElement(By.id("textbox"));
		text.sendKeys("Testing File Download Feature in LambdaTest....\n Test 1, 2 , 3...");
		driver.findElement(By.xpath("//button[@id='create']")).click();
		driver.findElement(By.xpath("//a[@id='link-to-download']")).click();
		assertTrue((new File(Constants.downloadPath+"Lambdainfo.txt")).exists());
	}
}

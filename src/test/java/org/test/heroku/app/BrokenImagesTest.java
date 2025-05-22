package org.test.heroku.app;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.test.util.Constants;
import org.test.util.DriverManager;
import org.testng.Reporter;
import org.testng.annotations.Test;

public class BrokenImagesTest extends DriverManager {

	private static final Logger logger = LogManager.getLogger(BrokenImagesTest.class);

	@Test(priority = 1, enabled = true)
	public void testImageAppr1() {
		driver.get(Constants.brokenImageURL);
		int imgCnt = 0;
		List<WebElement> imgs = driver.findElements(By.xpath("//img"));
		logger.debug(logId() + " : Number of images found: " + imgs.size());
		for (WebElement img : imgs) {
			logger.debug(logId() + " : Is the image displayed? " + img.isDisplayed() + " and its width is: "
					+ img.getAttribute("naturalWidth"));
			if (img.getAttribute("naturalWidth").equals("0")) {
				logger.debug(logId() + " : The image : " + img.getAttribute("outerHTML") + " is BROKEN!");
				imgCnt++;
			} else {
				logger.debug(logId() + " : The image : " + img.getAttribute("outerHTML") + " is NOT BROKEN.");
			}
		}
		assertEquals(imgCnt, 2);
		Reporter.log("Verified all Images.");
	}

	@Test(priority = 2, enabled = true)
	public void testImageAppr2() {
		int imgCnt = 0;
		List<WebElement> imgs = driver.findElements(By.xpath("//img"));
		logger.debug(logId() + " : Number of images found: " + imgs.size());

		CloseableHttpClient client = HttpClients.createDefault();

		for (WebElement img : imgs) {
			HttpGet req = new HttpGet(img.getAttribute("src"));
			try {
				int code = client.execute(req, response -> {
					return response.getCode();
				});
				logger.debug(logId() + " : Is the image displayed? " + img.isDisplayed()
						+ " and its httpResponse code is : " + code);
				if (code != 200) {
					logger.debug(logId() + " : The image : " + img.getAttribute("outerHTML") + " is BROKEN !");
					imgCnt++;
				} else {
					logger.debug(logId() + " : The image : " + img.getAttribute("outerHTML") + " is NOT BROKEN.");
				}
			} catch (IOException e) {
				logger.error(logId() + " : Exception occured in HTTP request execution for Image. :" + e.getMessage());
			}
		}
		assertEquals(imgCnt, 2);
		Reporter.log("Verified all Images with approach 2.");
	}

}

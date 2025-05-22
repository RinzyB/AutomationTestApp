package org.test.handson;

import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

	private static final Logger logger = LogManager.getLogger(LoginPage.class);

	// Page Factory - OR (Object Repository)

	@FindBy(name = "username")
	WebElement username;

	@FindBy(name = "password")
	WebElement pwd;

	@FindBy(id = "btn-login")
	WebElement loginBtn;

	@FindBy(id = "country")
	WebElement country;

	@FindBy(id = "delete")
	WebElement delete;

	private WebDriver driver;

	public LoginPage(String browser) {
		if (browser.equalsIgnoreCase("chrome")) {
			driver = new ChromeDriver();
		} else if (browser.equalsIgnoreCase("firefox")) {
			driver = new FirefoxDriver();
		}
		driver.manage().window().maximize();
		PageFactory.initElements(driver, this);
		driver.get("https://katalon-demo-cura.herokuapp.com/profile.php#login");
	}

	public void login(String user, String pass) {
		logger.debug("Testing Login..");
		username.sendKeys(user);
		pwd.sendKeys(pass);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.visibilityOf(loginBtn));
		loginBtn.click();
	}

	public String getPageTitle() {
		return driver.getTitle();
	}

	public String getLoginMsg() {
		WebElement loginMsg = driver.findElement(By.xpath("//h2/following-sibling::p[2]")); // element dynamically generated after login click
		logger.debug("login message is: " + loginMsg.getText());
		return loginMsg.getText();
	}

	public void selectCountry(String countryName) {
		// Locate the dropdown and select the value by visible text
		Select dropdown = new Select(country);
		dropdown.selectByVisibleText(countryName);
		logger.debug("Selected value is: " + dropdown.getFirstSelectedOption());
	}

	public void handleAlertAfterDelete() {
		// Write code to handle and accept JavaScript alert. After clicking a delete
		// button, a confirmation alert appears. Accept the alert.
		delete.click();
		Alert alrt = driver.switchTo().alert();
		logger.debug("Alert msg is: " + alrt.getText());
		alrt.accept();
	}

	public void tearDown() {
		driver.quit();
	}
}

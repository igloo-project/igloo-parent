package test.web.selenium;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

//@ContextConfiguration(classes = BasicApplicationWebappTestCommonConfig.class)
public class FirefoxDriverTest extends AbstractSeleniumTestCase {

	@Override
	public void initialization() throws Exception {
		super.initialization();
		
		// @mpiva : voir à quoi ça peut servir
//		DesiredCapabilities capabilities = DesiredCapabilities.firefox();
//		capabilities.setCapability("marionette", true);
//		FirefoxOptions options = new FirefoxOptions(capabilities);
//		driver = new FirefoxDriver(options);
		
		driver = new FirefoxDriver();
	}

	@Test
	public void kobalt() {
		driver.get("https://www.kobalt.fr");
	}

	@Test
	public void login() {
		driver.get(rootUrl);
		
		WebElement login = driver.findElement(By.id("username"));
		login.sendKeys("username");
		WebElement password = driver.findElement(By.id("password"));
		password.sendKeys(USER_PASSWORD);
		password.submit();
	}

	@Test(expected = NoSuchElementException.class)
	public void missingElement() {
		driver.get(rootUrl);
		
		driver.findElement(By.id("wrongid"));
	}

}

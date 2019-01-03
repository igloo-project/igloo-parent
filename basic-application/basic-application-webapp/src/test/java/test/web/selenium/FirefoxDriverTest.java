package test.web.selenium;

import static test.web.property.SeleniumPropertyIds.XVFB_DISPLAY;

import java.util.List;
import java.util.function.Function;

import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class FirefoxDriverTest extends AbstractSeleniumTestCase {

	@Override
	public void init() throws ServiceException, SecurityServiceException  {
		super.init();
		FirefoxOptions fo = new FirefoxOptions();
		fo.addArguments("--display=" + propertyService.get(XVFB_DISPLAY));
		driver = new FirefoxDriver(fo);
	}

	@Test
	public void kobalt() {
		driver.get("https://www.kobalt.fr");
	}

	@Test
	public void loginAdministrator() {
		login(administrator);
	}

	@Test(expected = NoSuchElementException.class)
	public void missingElement() {
		driver.get(rootUrl);
		
		driver.findElement(By.id("wrongid"));
	}

	@Test
	public void subMenuWithLinkTextAccess() throws InterruptedException {
		login(administrator);
		
		getElementWithTimeout(By.linkText("Administration"), 10).click();
		driver.findElement(By.linkText("Annonces")).click();
	}

	@Test
	public void dropDownSingleChoice() {
		login(administrator);
		
		getElementWithTimeout(By.linkText("Administration"), 10).click();
		driver.findElement(By.linkText("Utilisateurs")).click();
		
		WebElement userGroup = getElementWithTimeout(By.name("userGroup"), 10).findElement(By.tagName("span"));
		userGroup.click();
		
		waitUntil(10, ExpectedConditions.numberOfElementsToBe(By.className("select2-results__option"), 2));
		List<WebElement> results = driver.findElements(By.className("select2-results__option"));
		results.get(0).click();
		
		driver.findElement(By.name("filter")).click();
	}

	@Test
	public void ajaxDropDownSingleChoice() {
		login(administrator);
		
		getElementWithTimeout(By.linkText("Administration"), 10).click();
		driver.findElement(By.linkText("Utilisateurs")).click();
		
		WebElement quickAccess = getElementWithTimeout(By.name("quickAccess"), 10).findElement(By.tagName("span"));
		quickAccess.click();
		
		WebElement searchBox = getElementWithTimeout(By.className("select2-search__field"), 5);
		searchBox.sendKeys("basi");
		
		waitUntil(10, ExpectedConditions.numberOfElementsToBe(By.className("select2-results__option"), 2));
		List<WebElement> results = driver.findElements(By.className("select2-results__option"));
		results.get(0).click();
		
		String expectedUrl = rootUrl + "administration/basic-user/" + basicUser.getId() + "/";
		waitUntil(10, ExpectedConditions.urlMatches(expectedUrl + "[?]*[-a-zA-Z0-9+&@#/%=~_]*"));
	}

	@Test
	public void accessDataTableBuilderElement() {
		login(administrator);
		
		getElementWithTimeout(By.linkText("Administration"), 10).click();
		driver.findElement(By.linkText("Utilisateurs")).click();
		
		List<WebElement> usernames = getElementsWithTimeout(By.name("username"), 10);
		usernames.get(1).click();
		
		String expectedUrl = rootUrl + "administration/basic-user/" + basicUser2.getId() + "/";
		waitUntil(10, ExpectedConditions.urlMatches(expectedUrl + "[?]*[-a-zA-Z0-9+&@#/%=~_]*"));
	}

	private void login(User user) {
		driver.get(rootUrl);
		
		WebElement login = driver.findElement(By.id("username"));
		login.sendKeys(user.getUsername());
		WebElement password = driver.findElement(By.id("password"));
		password.sendKeys(USER_PASSWORD);
		password.submit();
	}

	private List<WebElement> getElementsWithTimeout(By by, long timeout) {
		return new WebDriverWait(driver, timeout).until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
	}

	private WebElement getElementWithTimeout(By by, long timeout) {
		return new WebDriverWait(driver, timeout).until(ExpectedConditions.presenceOfElementLocated(by));
	}

	private void waitUntil(int timeout, Function<? super WebDriver, ?> function) {
		new WebDriverWait(driver, timeout).until(function);
	}

}

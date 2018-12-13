package test.web.selenium;

import org.apache.wicket.protocol.http.WicketFilter;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

public class FirefoxDriverTest {

	private WebDriver driver;

	private String rootUrl = "http://localhost:8090/basic-application/";

	private Server server;

	@Before
	public void initialization() throws Exception {
		System.setProperty("webdriver.gecko.driver", "/home/mpiva/Documents/apps/geckodriver-v0.23.0-linux64/geckodriver");
		
		launchJettyServer();
	}

	private void launchJettyServer() throws Exception {
		server = new Server();
		ServerConnector connector = new ServerConnector(server);
		connector.setHost("localhost");
		connector.setPort(8090);
		server.setConnectors(new Connector [] {connector});
		
//		ServletHandler springServletHandler = new ServletHandler();
		
//		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
//		new ExtendedApplicationContextInitializer().initialize(context);
//		context.register(BasicApplicationWebappTestCommonConfig.class);
//		
//		WicketServlet wicketServlet = new WicketServlet();
//		wicketServlet.
//		
//		ServletHolder servletHolder = new ServletHolder(new WicketServlet());
//		servletHolder.setInitParameter("applicationClassName", application.getClass().getName());
//		
//		ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
//		servletContextHandler.setContextPath("/");
//		servletContextHandler.addServlet(servletHolder, "/*");
		
//		server.setHandler(servletContextHandler);
		
		WebAppContext webapp = new WebAppContext();
		webapp.setResourceBase("src/main/java");
		webapp.setContextPath("/");
		webapp.setInitParameter(WicketFilter.FILTER_MAPPING_PARAM, "/*");
		webapp.setDefaultsDescriptor("src/test/java/test/web/selenium/web.xml");
		
		server.setHandler(webapp);
		
		server.start();
	}

	@After
	public void destroy() throws Exception {
		if (driver != null) {
			driver.quit();
		}
		if (server != null && server.isRunning()) {
			server.stop();
		}
	}

	@Test
	public void kobalt() {
		DesiredCapabilities capabilities = DesiredCapabilities.firefox();
		capabilities.setCapability("marionette", true);
		FirefoxOptions options = new FirefoxOptions(capabilities);
		driver = new FirefoxDriver(options);
		
		driver.get("https://www.kobalt.fr");
	}

	@Test
	public void basicApplication() {
		driver = new FirefoxDriver();
		
		driver.get(rootUrl);
		
		WebElement login = driver.findElement(By.id("username"));
		login.sendKeys("admin");
		WebElement password = driver.findElement(By.id("password"));
		password.sendKeys("kobalt");
		password.submit();
	}

	@Test(expected = NoSuchElementException.class)
	public void missingElement() {
		driver = new FirefoxDriver();
		
		driver.get(rootUrl);
		
		driver.findElement(By.id("wrongid"));
	}
}

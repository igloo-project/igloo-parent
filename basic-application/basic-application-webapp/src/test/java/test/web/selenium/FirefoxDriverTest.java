package test.web.selenium;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.webapp.WebAppContext;
import org.iglooproject.basicapp.core.business.user.service.IUserService;
import org.iglooproject.spring.util.context.ApplicationContextUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import test.web.spring.JettyTestExecutionListener;

/**
 * Injection pour initialiser la base de données --> 19/12/2018 : apparemment ne servirai pas ???
 */
//@ContextConfiguration(classes = BasicApplicationWebappTestCommonConfig.class)
/**
 * Utilisé pour initialisé le serveur Jetty avant l'initialisation du contexte Spring et qu'il soit encapsulé
 */
@TestExecutionListeners(listeners = { JettyTestExecutionListener.class })
public class FirefoxDriverTest {

	/**
	 * Use this instead of SpringJUnit4ClassRunner, so that implementors can choose their own runner
	 */
	@ClassRule
	public static final SpringClassRule SCR = new SpringClassRule();
	/**
	 * Use this instead of SpringJUnit4ClassRunner, so that implementors can choose their own runner
	 */
	@Rule
	public final SpringMethodRule springMethodRule = new SpringMethodRule();

	private WebDriver driver;

	private String rootUrl = "http://localhost:8090/basic-application/";

	private Server server;

	@Autowired
	private IUserService userService;

	@Before
	public void initialization() throws Exception {
		System.setProperty("webdriver.gecko.driver", "/home/mpiva/Documents/apps/geckodriver-v0.23.0-linux64/geckodriver");
		
		// Nécessaire pour l'injection des bean
		ApplicationContextUtils.getInstance().getContext().getAutowireCapableBeanFactory().autowireBean(this);
		
		// Working --> l'EntityManager est bien lancé
//		User user = new User();
//		user.setUsername("admin");
//		user.setFirstName("admin");
//		user.setLastName("admin");
//		userService.create(user);
//		userService.setPasswords(user, "kobalt");
	}

	@Test
	public void databaseInitialized() {
		userService.list(); //Si ne marche pas throw une exception
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
		webapp.setDefaultsDescriptor("src/test/java/test/web/selenium/web.xml");
		
//		FilterHolder filterHolder = new FilterHolder(WicketFilter.class);
//		filterHolder.setInitParameter(WicketFilter.FILTER_MAPPING_PARAM, "/*");
//		filterHolder.setInitParameter("applicationClassName", BasicApplicationApplication.class.getName());
//		webapp.addFilter(filterHolder, "/*", null);
		
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
	public void login() {
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

package test.web.selenium;

import org.eclipse.jetty.server.Server;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.business.user.service.IUserGroupService;
import org.iglooproject.basicapp.core.business.user.service.IUserService;
import org.iglooproject.jpa.security.business.authority.model.Authority;
import org.iglooproject.jpa.security.business.authority.service.IAuthorityService;
import org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants;
import org.iglooproject.jpa.util.EntityManagerUtils;
import org.iglooproject.spring.util.context.ApplicationContextUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import test.web.spring.EntityManagerSeleniumExecutionListener;
import test.web.spring.JettyTestExecutionListener;
/**
 * Injection pour initialiser la base de données --> 19/12/2018 : apparemment ne servirai pas ???
 */
//@ContextConfiguration(classes = BasicApplicationWebappTestCommonConfig.class)
/**
 * Utilisé pour initialisé le serveur Jetty avant l'initialisation du contexte Spring et qu'il soit encapsulé
 */
@TestExecutionListeners(listeners = { JettyTestExecutionListener.class, EntityManagerSeleniumExecutionListener.class })
public class AbstractSeleniumTestCase {

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

	protected static final String USER_PASSWORD = "kobalt";

	protected String rootUrl = "http://localhost:8090/";

	protected WebDriver driver;

	protected Server server;

	@Autowired
	protected IUserService userService;

	@Autowired
	protected IUserGroupService userGroupService;

	@Autowired
	protected IAuthorityService authorityService;

	@Autowired
	protected EntityManagerUtils entityManagerUtils;

	@Before
	public void initialization() throws Exception {
		// Nécessaire pour l'injection des bean
		ApplicationContextUtils.getInstance().getContext().getAutowireCapableBeanFactory().autowireBean(this);
		
		System.setProperty("webdriver.gecko.driver", "/home/mpiva/Documents/apps/geckodriver-v0.23.0-linux64/geckodriver");
		
		if (userService.list().isEmpty()) {
			authorityService.create(new Authority(CoreAuthorityConstants.ROLE_AUTHENTICATED));
			User user = new User();
			user.setUsername("username");
			user.setFirstName("firstname");
			user.setLastName("lastname");
			userService.create(user);
			userService.setPasswords(user, USER_PASSWORD);
		}
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

}

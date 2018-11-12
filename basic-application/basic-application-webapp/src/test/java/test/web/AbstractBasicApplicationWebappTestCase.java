package test.web;

import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.tester.WicketTester;
import org.iglooproject.basicapp.core.business.history.service.IHistoryLogService;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.business.user.service.IUserService;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.security.business.authority.model.Authority;
import org.iglooproject.jpa.security.business.authority.service.IAuthorityService;
import org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants;
import org.iglooproject.jpa.security.service.IAuthenticationService;
import org.iglooproject.spring.property.dao.IMutablePropertyDao;
import org.iglooproject.test.wicket.more.AbstractWicketMoreTestCase;
import org.iglooproject.wicket.more.AbstractCoreSession;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import test.web.config.spring.BasicApplicationWebappTestCommonConfig;

@ContextConfiguration(classes = BasicApplicationWebappTestCommonConfig.class)
public abstract class AbstractBasicApplicationWebappTestCase extends AbstractWicketMoreTestCase {

	@Autowired
	protected IUserService userService;

	@Autowired
	protected IAuthorityService authorityService;

	@Autowired
	protected IAuthenticationService authenticationService;

	@Autowired
	protected IHistoryLogService historyLogService;

	@Autowired
	private IMutablePropertyDao mutablePropertyDao;

	@Autowired
	WebApplication application;

	@Before
	public void setUp() throws ServiceException, SecurityServiceException {
		initAuthorities();
		setWicketTester(new WicketTester(application));
	}

	@Override
	protected void cleanAll() throws ServiceException, SecurityServiceException {
		entityManagerClear();
		
		cleanEntities(userService);
		cleanEntities(authorityService);
		cleanEntities(historyLogService);
		
		mutablePropertyDao.cleanInTransaction();
		
		authenticationService.signOut();
	}

	protected User createUser(String username, String firstname, String lastname, String password)
		throws ServiceException, SecurityServiceException {
		return createUser(username, firstname, lastname, password, CoreAuthorityConstants.ROLE_AUTHENTICATED);
	}

	protected User createUser(String username, String firstname, String lastname, String password, String authority)
		throws ServiceException, SecurityServiceException {
		User user = new User();
		user.setUsername(username);
		user.setFirstName(firstname);
		user.setLastName(lastname);
		user.addAuthority(authorityService.getByName(authority));
		
		userService.create(user);
		userService.setPasswords(user, password);
		
		return user;
	}

	protected void createAndAuthenticateUser(String username, String firstname, String lastname, String password, String authority)
		throws ServiceException, SecurityServiceException {
		createUser(username, firstname, lastname, password, authority);
		
		AbstractCoreSession<?> session = AbstractCoreSession.get();
		User loggedInUser = null;
		
		session.signIn(username, password);
		loggedInUser = (User) session.getUser();
		userService.onSignIn(loggedInUser);
	}

	protected void createAndAuthenticateUser(String authority) throws ServiceException, SecurityServiceException {
		String username = "admin";
		String firstname = "Kobalt";
		String lastname = "Kobalt";
		String password = "kobalt69";
		createAndAuthenticateUser(username, firstname, lastname, password, authority);
	}

	private void initAuthorities() throws ServiceException, SecurityServiceException {
		authorityService.create(new Authority(CoreAuthorityConstants.ROLE_ADMIN));
		authorityService.create(new Authority(CoreAuthorityConstants.ROLE_AUTHENTICATED));
	}
}

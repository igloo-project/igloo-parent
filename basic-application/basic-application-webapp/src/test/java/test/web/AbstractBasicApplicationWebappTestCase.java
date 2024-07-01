package test.web;

import org.apache.wicket.Localizer;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.protocol.http.WebApplication;
import org.iglooproject.basicapp.core.business.user.model.BasicUser;
import org.iglooproject.basicapp.core.business.user.model.TechnicalUser;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.business.user.model.UserGroup;
import org.iglooproject.basicapp.core.business.user.service.IUserGroupService;
import org.iglooproject.basicapp.core.business.user.service.IUserService;
import org.iglooproject.basicapp.core.security.service.IBasicApplicationAuthenticationService;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.spring.property.dao.IMutablePropertyDao;
import org.iglooproject.test.wicket.core.AbstractWicketTestCase;
import org.iglooproject.wicket.more.AbstractCoreSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.Sql;

import test.core.CleanDatabaseTestExecutionListener;

@TestExecutionListeners(
	value = {CleanDatabaseTestExecutionListener.class},
	mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS // Retains default TestExecutionListeners.
)
@Sql(scripts = "/scripts/init-wicket.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public abstract class AbstractBasicApplicationWebappTestCase extends AbstractWicketTestCase<BasicApplicationWicketTester> {

	protected static final String USER_PASSWORD = "USER_PASSWORD";

	protected UserGroup users;

	protected UserGroup administrators;

	protected BasicUser basicUser;

	protected TechnicalUser administrator;

	@Autowired
	protected IUserService userService;

	@Autowired
	protected IUserGroupService userGroupService;

	@Autowired
	protected IBasicApplicationAuthenticationService authenticationService;

	@Autowired
	protected PasswordEncoder passwordEncoder;

	@Autowired
	private IMutablePropertyDao mutablePropertyDao;

	@Autowired
	private WebApplication application;

	@Override
	@BeforeEach
	public void init() throws ServiceException {
		cleanAll();
		setWicketTester(new BasicApplicationWicketTester(application));
		initUserGroups();
		initUsers();
	}

	@Override
	@AfterEach
	public void close() {
		// nothing to do
	}

	@Override
	protected void cleanAll() throws ServiceException{
		entityManagerClear();

		mutablePropertyDao.cleanInTransaction();

		authenticationService.signOut();

		emptyIndexes();
	}

	private void initUserGroups() {
		users = userGroupService.getByName("Users");
		administrators = userGroupService.getByName("Administrators");
	}

	private void initUsers() {
		basicUser = (BasicUser) userService.getByUsername("basicUser");
		administrator = (TechnicalUser) userService.getByUsername("administrator");
	}

	protected void authenticateUser(User user) throws ServiceException, SecurityServiceException {
		if (AuthenticatedWebSession.exists()) {
			AuthenticatedWebSession.get().invalidate();
		}

		AbstractCoreSession<?> session = AbstractCoreSession.get();
		User loggedInUser = null;

		session.signIn(user.getUsername(), USER_PASSWORD);
		loggedInUser = (User) session.getUser();
		userService.onSignIn(loggedInUser);
	}

	// Depends on the tester.getSession.getLocale()
	protected static String localize(String key) {
		return Localizer.get().getString(key, null);
	}
}

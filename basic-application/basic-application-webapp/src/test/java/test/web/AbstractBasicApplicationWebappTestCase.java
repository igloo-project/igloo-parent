package test.web;

import java.util.Set;

import org.apache.wicket.Localizer;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.tester.WicketTester;
import org.assertj.core.util.Sets;
import org.iglooproject.basicapp.core.business.history.service.IHistoryLogService;
import org.iglooproject.basicapp.core.business.user.model.BasicUser;
import org.iglooproject.basicapp.core.business.user.model.TechnicalUser;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.business.user.model.UserGroup;
import org.iglooproject.basicapp.core.business.user.service.IUserGroupService;
import org.iglooproject.basicapp.core.business.user.service.IUserService;
import org.iglooproject.basicapp.web.application.common.typedescriptor.user.UserTypeDescriptor;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;

import test.web.config.spring.BasicApplicationWebappTestCommonConfig;

@ContextConfiguration(classes = BasicApplicationWebappTestCommonConfig.class)
public abstract class AbstractBasicApplicationWebappTestCase extends AbstractWicketMoreTestCase {

	@Autowired
	protected IUserService userService;

	@Autowired
	protected IUserGroupService userGroupService;

	@Autowired
	protected IAuthorityService authorityService;

	@Autowired
	protected IAuthenticationService authenticationService;

	@Autowired
	protected IHistoryLogService historyLogService;

	@Autowired
	protected PasswordEncoder passwordEncoder;

	@Autowired
	private IMutablePropertyDao mutablePropertyDao;

	@Autowired
	private WebApplication application;

	@Before
	public void setUp() throws ServiceException, SecurityServiceException {
		initAuthorities();
		setWicketTester(new WicketTester(application));
	}

	@Override
	protected void cleanAll() throws ServiceException, SecurityServiceException {
		entityManagerClear();
		
		cleanEntities(userService);
		cleanEntities(userGroupService);
		cleanEntities(authorityService);
		cleanEntities(historyLogService);
		
		mutablePropertyDao.cleanInTransaction();
		
		authenticationService.signOut();
	}

	protected <U extends User> User createUser(String username, String firstname, String lastname, String password,
		UserTypeDescriptor<U> type, Set<UserGroup> userGroups, Set<String> authorities) throws ServiceException, SecurityServiceException {
		
		User user;
		if (UserTypeDescriptor.BASIC_USER.equals(type)) {
			user = new BasicUser();
		} else if (UserTypeDescriptor.TECHNICAL_USER.equals(type)) {
			user = new TechnicalUser();
		} else {
			user = new User();
		}
		
		user.setUsername(username);
		user.setFirstName(firstname);
		user.setLastName(lastname);
		if (authorities != null) {
			for (String authority : authorities) {
				user.addAuthority(authorityService.getByName(authority));
			}
		}
		userService.create(user);
		userService.setPasswords(user, password);
		
		if (userGroups != null) {
			for (UserGroup userGroup : userGroups) {
				userGroupService.addUser(userGroup, user);
			}
		}
		
		return user;
	}

	protected void authenticateUser(User user, String password) throws ServiceException, SecurityServiceException {
		AbstractCoreSession<?> session = AbstractCoreSession.get();
		User loggedInUser = null;
		
		session.signIn(user.getUsername(), password);
		loggedInUser = (User) session.getUser();
		userService.onSignIn(loggedInUser);
	}

	protected User createAndAuthenticateUser(String username, String firstname, String lastname, String password, String authority)
		throws ServiceException, SecurityServiceException {
		User user = createUser(username, firstname, lastname, password, null, null, Sets.newTreeSet(authority));
		authenticateUser(user, password);
		return user;
	}

	protected User createAndAuthenticateUser(String authority) throws ServiceException, SecurityServiceException {
		String username = "Username";
		String firstname = "Firstname";
		String lastname = "Lastname";
		String password = "Password";
		return createAndAuthenticateUser(username, firstname, lastname, password, authority);
	}

	private void initAuthorities() throws ServiceException, SecurityServiceException {
		authorityService.create(new Authority(CoreAuthorityConstants.ROLE_ADMIN));
		authorityService.create(new Authority(CoreAuthorityConstants.ROLE_AUTHENTICATED));
	}

	protected void initUserGroups() throws ServiceException, SecurityServiceException {
		userGroupService.create(new UserGroup("Users"));
		userGroupService.create(new UserGroup("Administrators"));
	}

	// Depends on the tester.getSession.getLocale()
	protected static String localize(String key) {
		return Localizer.get().getString(key, null);
	}

	protected String modalPath(String path) {
		return path + ":container:dialog";
	}

	protected String modalFormPath(String path) {
		return modalPath(path) + ":body:form";
	}

	protected String breadCrumbPath() {
		return "breadCrumb:breadCrumbElementListView";
	}

	protected String breadCrumbElementPath(int element) {
		return "breadCrumb:breadCrumbElementListView:" + element + ":breadCrumbElement";
	}
}

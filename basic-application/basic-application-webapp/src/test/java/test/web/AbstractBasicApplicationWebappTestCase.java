package test.web;

import java.util.Set;

import org.apache.wicket.Localizer;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.protocol.http.WebApplication;
import org.iglooproject.basicapp.core.business.history.service.IHistoryLogService;
import org.iglooproject.basicapp.core.business.user.model.BasicUser;
import org.iglooproject.basicapp.core.business.user.model.TechnicalUser;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.business.user.model.UserGroup;
import org.iglooproject.basicapp.core.business.user.service.IUserGroupService;
import org.iglooproject.basicapp.core.business.user.service.IUserService;
import org.iglooproject.basicapp.core.business.user.typedescriptor.UserTypeDescriptor;
import org.iglooproject.basicapp.core.security.model.BasicApplicationAuthorityConstants;
import org.iglooproject.basicapp.core.security.service.IBasicApplicationAuthenticationService;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.security.business.authority.model.Authority;
import org.iglooproject.jpa.security.business.authority.service.IAuthorityService;
import org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants;
import org.iglooproject.spring.property.dao.IMutablePropertyDao;
import org.iglooproject.test.wicket.core.AbstractWicketTestCase;
import org.iglooproject.wicket.more.AbstractCoreSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.google.common.collect.ImmutableSet;

import test.web.config.spring.BasicApplicationWebappTestCommonConfig;

@ContextConfiguration(classes = BasicApplicationWebappTestCommonConfig.class)
@TestPropertySource(properties = "igloo.profile=test")
@ExtendWith(SpringExtension.class)
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
	protected IAuthorityService authorityService;

	@Autowired
	protected IBasicApplicationAuthenticationService authenticationService;

	@Autowired
	protected IHistoryLogService historyLogService;

	@Autowired
	protected PasswordEncoder passwordEncoder;

	@Autowired
	private IMutablePropertyDao mutablePropertyDao;

	@Autowired
	private WebApplication application;

	@BeforeEach
	public void setUp() throws ServiceException, SecurityServiceException {
		initAuthorities();
		initUserGroups();
		initUsers();
		
		setWicketTester(new BasicApplicationWicketTester(application));
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
		
		//TODO: igloo-boot
//		emptyIndexes();
	}

	private void initAuthorities() throws ServiceException, SecurityServiceException {
		authorityService.create(new Authority(CoreAuthorityConstants.ROLE_ADMIN));
		authorityService.create(new Authority(CoreAuthorityConstants.ROLE_AUTHENTICATED));
	}

	private void initUserGroups() throws ServiceException, SecurityServiceException {
		users = new UserGroup("Users");
		administrators = new UserGroup("Administrators");
		
		userGroupService.create(users);
		userGroupService.create(administrators);
	}

	private void initUsers() throws ServiceException, SecurityServiceException {
		basicUser = createUser("basicUser", UserTypeDescriptor.BASIC_USER,
			ImmutableSet.of(BasicApplicationAuthorityConstants.ROLE_AUTHENTICATED), ImmutableSet.of(users));
		createUser("basicUser2", UserTypeDescriptor.BASIC_USER,
				ImmutableSet.of(BasicApplicationAuthorityConstants.ROLE_AUTHENTICATED), ImmutableSet.of(users));
		
		administrator = createUser("administrator", UserTypeDescriptor.TECHNICAL_USER,
			ImmutableSet.of(BasicApplicationAuthorityConstants.ROLE_ADMIN), ImmutableSet.of(administrators));
	}

	private <U extends User> U createUser(String username, UserTypeDescriptor<U> type, Set<String> authorities, Set<UserGroup> userGroups)
		throws ServiceException, SecurityServiceException {
		
		U user = type.getSupplier().get();
		user.setUsername(username);
		user.setFirstName(username);
		user.setLastName(username);
		if (authorities != null) {
			for (String authority : authorities) {
				user.addAuthority(authorityService.getByName(authority));
			}
		}
		
		userService.create(user);
		userService.setPasswords(user, USER_PASSWORD);
		
		if (userGroups != null) {
			for (UserGroup userGroup : userGroups) {
				userGroupService.addUser(userGroup, user);
			}
		}
		
		return user;
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

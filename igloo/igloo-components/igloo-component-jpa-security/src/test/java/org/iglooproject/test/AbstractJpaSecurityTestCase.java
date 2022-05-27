package org.iglooproject.test;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.security.business.authority.model.Authority;
import org.iglooproject.jpa.security.business.authority.service.IAuthorityService;
import org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants;
import org.iglooproject.jpa.security.business.user.model.IUser;
import org.iglooproject.jpa.security.service.IAuthenticationService;
import org.iglooproject.jpa.security.service.ISecurityService;
import org.iglooproject.test.jpa.junit.AbstractTestCase;
import org.iglooproject.test.jpa.security.business.person.model.MockUser;
import org.iglooproject.test.jpa.security.business.person.model.MockUserGroup;
import org.iglooproject.test.jpa.security.business.person.service.IMockUserGroupService;
import org.iglooproject.test.jpa.security.business.person.service.IMockUserService;
import org.iglooproject.test.jpa.security.config.spring.JpaSecurityTestConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = JpaSecurityTestConfig.class)
public abstract class AbstractJpaSecurityTestCase extends AbstractTestCase {

	public static final String DEFAULT_PASSWORD = "test";

	public static final String ROLE_GROUP_1 = "ROLE_GROUP_1";

	public static final String ROLE_GROUP_2 = "ROLE_GROUP_2";

	public static final String ROLE_GROUP_3 = "ROLE_GROUP_3";

	@Autowired
	protected IMockUserService mockUserService;

	@Autowired
	protected IMockUserGroupService mockUserGroupService;

	@Autowired
	protected IAuthorityService authorityService;

	@Autowired
	protected IAuthenticationService authenticationService;

	@Autowired
	protected ISecurityService securityService;

	@Autowired
	protected ProviderManager authenticationManager;

	@Autowired
	protected PasswordEncoder passwordEncoder;

	@BeforeEach
	@Override
	public void init() throws ServiceException, SecurityServiceException {
		super.init();
		
		createAuthority(CoreAuthorityConstants.ROLE_SYSTEM);
		createAuthority(CoreAuthorityConstants.ROLE_ADMIN);
		createAuthority(CoreAuthorityConstants.ROLE_AUTHENTICATED);
		createAuthority(CoreAuthorityConstants.ROLE_ANONYMOUS);
		
		createAuthority(ROLE_GROUP_1);
		createAuthority(ROLE_GROUP_2);
		createAuthority(ROLE_GROUP_3);
	}
	
	@AfterEach
	@Override
	public void close() throws ServiceException, SecurityServiceException {
		super.close();
	}

	@Override
	protected void cleanAll() throws ServiceException, SecurityServiceException {
		cleanEntities(mockUserService);
		cleanEntities(mockUserGroupService);
		cleanEntities(authorityService);
	}

	protected MockUser createMockUser(String username, String firstName, String lastName) throws ServiceException, SecurityServiceException {
		return createMockUser(username, firstName, lastName, "test@example.com");
	}
	
	protected MockUser createMockUser(String username, String firstName, String lastName, String email) throws ServiceException, SecurityServiceException {
		MockUser user = new MockUser();
		user.setUsername(username);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);
		
		user.addAuthority(authorityService.getByName(CoreAuthorityConstants.ROLE_AUTHENTICATED));
		
		mockUserService.create(user);
		mockUserService.setPasswords(user, DEFAULT_PASSWORD);
		
		return user;
	}
	
	protected MockUserGroup createMockUserGroup(String name) throws ServiceException, SecurityServiceException {
		MockUserGroup userGroup = new MockUserGroup();
		userGroup.setName(name);
		
		mockUserGroupService.create(userGroup);
		
		return userGroup;
	}
	
	protected Authority createAuthority(String name) throws ServiceException, SecurityServiceException {
		Authority authority = new Authority();
		authority.setName(name);
		
		authorityService.create(authority);
		
		return authority;
	}
	
	protected void authenticateAs(IUser user) {
		authenticateAs(new UsernamePasswordAuthenticationToken(user.getUsername(), DEFAULT_PASSWORD));
	}
	
	protected void authenticateAs(UsernamePasswordAuthenticationToken authenticationToken) {
		Authentication authentication = authenticationManager.authenticate(authenticationToken);
		
		SecurityContext secureContext = new SecurityContextImpl();
		secureContext.setAuthentication(authentication);
		SecurityContextHolder.setContext(secureContext);
	}
}

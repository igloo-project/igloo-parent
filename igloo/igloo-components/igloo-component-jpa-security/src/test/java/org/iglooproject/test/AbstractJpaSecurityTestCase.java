package org.iglooproject.test;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.junit.AbstractTestCase;
import org.iglooproject.jpa.security.business.authority.model.Authority;
import org.iglooproject.jpa.security.business.authority.service.IAuthorityService;
import org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants;
import org.iglooproject.jpa.security.business.person.model.IUser;
import org.iglooproject.jpa.security.service.IAuthenticationService;
import org.iglooproject.jpa.security.service.ISecurityService;
import org.iglooproject.test.jpa.security.business.person.model.MockUser;
import org.iglooproject.test.jpa.security.business.person.model.MockUserGroup;
import org.iglooproject.test.jpa.security.business.person.service.IMockUserGroupService;
import org.iglooproject.test.jpa.security.business.person.service.IMockUserService;
import org.iglooproject.test.jpa.security.config.spring.JpaSecurityTestConfig;
import org.junit.After;
import org.junit.Before;
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

	@Before
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
	
	@After
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

	protected MockUser createMockPerson(String username, String firstName, String lastName) throws ServiceException, SecurityServiceException {
		return createMockPerson(username, firstName, lastName, "test@example.com");
	}
	
	protected MockUser createMockPerson(String username, String firstName, String lastName, String email) throws ServiceException, SecurityServiceException {
		MockUser person = new MockUser();
		person.setUsername(username);
		person.setFirstName(firstName);
		person.setLastName(lastName);
		person.setEmail(email);
		
		person.addAuthority(authorityService.getByName(CoreAuthorityConstants.ROLE_AUTHENTICATED));
		
		mockUserService.create(person);
		mockUserService.setPasswords(person, DEFAULT_PASSWORD);
		
		return person;
	}
	
	protected MockUserGroup createMockPersonGroup(String name) throws ServiceException, SecurityServiceException {
		MockUserGroup personGroup = new MockUserGroup();
		personGroup.setName(name);
		
		mockUserGroupService.create(personGroup);
		
		return personGroup;
	}
	
	protected Authority createAuthority(String name) throws ServiceException, SecurityServiceException {
		Authority authority = new Authority();
		authority.setName(name);
		
		authorityService.create(authority);
		
		return authority;
	}
	
	protected void authenticateAs(IUser person) {
		authenticateAs(new UsernamePasswordAuthenticationToken(person.getUsername(), DEFAULT_PASSWORD));
	}
	
	protected void authenticateAs(UsernamePasswordAuthenticationToken authenticationToken) {
		Authentication authentication = authenticationManager.authenticate(authenticationToken);
		
		SecurityContext secureContext = new SecurityContextImpl();
		secureContext.setAuthentication(authentication);
		SecurityContextHolder.setContext(secureContext);
	}
}

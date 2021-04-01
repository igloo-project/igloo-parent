package org.iglooproject.test.jpa.security;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.Iterator;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants;
import org.iglooproject.test.AbstractJpaSecurityTestCase;
import org.iglooproject.test.jpa.security.business.person.model.MockUser;
import org.iglooproject.test.jpa.security.business.person.model.MockUserGroup;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public class TestCoreJpaUserDetailsService extends AbstractJpaSecurityTestCase {

	@Autowired
	private UserDetailsService coreJpaUserDetailsService;

	@SuppressWarnings("unchecked")
	@Test
	public void testLoadUserByUsername() throws ServiceException, SecurityServiceException {
		MockUserGroup adminGroup = createMockPersonGroup("adminGroup");
		adminGroup.addAuthority(authorityService.getByName(CoreAuthorityConstants.ROLE_ADMIN));
		
		MockUserGroup group1 = createMockPersonGroup("group1");
		group1.addAuthority(authorityService.getByName(ROLE_GROUP_1));
		
		MockUserGroup group2 = createMockPersonGroup("group2");
		group2.addAuthority(authorityService.getByName(ROLE_GROUP_2));
		
		mockUserGroupService.update(adminGroup);
		mockUserGroupService.update(group1);
		mockUserGroupService.update(group2);
		
		MockUser personAdmin = createMockPerson("admin", "admin", "admin");
		MockUser personGroup1 = createMockPerson("userGroup1", "userGroup1", "userGroup1");
		MockUser personGroup2 = createMockPerson("userGroup2", "userGroup2", "userGroup2");
		
		mockUserGroupService.addUser(adminGroup, personAdmin);
		mockUserGroupService.addUser(group1, personGroup1);
		mockUserGroupService.addUser(group2, personGroup2);
		
		Collection<GrantedAuthority> grantedAuthorities;
		Iterator<GrantedAuthority> iterator;
		UserDetails userDetails;
		
		// Admin person
		userDetails = coreJpaUserDetailsService.loadUserByUsername(personAdmin.getUsername());
		
		grantedAuthorities = (Collection<GrantedAuthority>) userDetails.getAuthorities();
		
		assertEquals(6, grantedAuthorities.size());
		
		iterator = grantedAuthorities.iterator();
		
		assertEquals(CoreAuthorityConstants.ROLE_ADMIN, iterator.next().getAuthority());
		assertEquals(CoreAuthorityConstants.ROLE_ANONYMOUS, iterator.next().getAuthority());
		assertEquals(CoreAuthorityConstants.ROLE_AUTHENTICATED, iterator.next().getAuthority());
		assertEquals(ROLE_GROUP_1, iterator.next().getAuthority());
		assertEquals(ROLE_GROUP_2, iterator.next().getAuthority());
		assertEquals(ROLE_GROUP_3, iterator.next().getAuthority());
		
		// Group1 person
		userDetails = coreJpaUserDetailsService.loadUserByUsername(personGroup1.getUsername());
		
		grantedAuthorities = (Collection<GrantedAuthority>) userDetails.getAuthorities();
		
		assertEquals(4, grantedAuthorities.size());
		
		iterator = grantedAuthorities.iterator();
		
		assertEquals(CoreAuthorityConstants.ROLE_ANONYMOUS, iterator.next().getAuthority());
		assertEquals(CoreAuthorityConstants.ROLE_AUTHENTICATED, iterator.next().getAuthority());
		assertEquals(ROLE_GROUP_1, iterator.next().getAuthority());
		assertEquals(ROLE_GROUP_3, iterator.next().getAuthority());
		
		// Group2 person
		userDetails = coreJpaUserDetailsService.loadUserByUsername(personGroup2.getUsername());
		
		grantedAuthorities = (Collection<GrantedAuthority>) userDetails.getAuthorities();
		
		assertEquals(3, grantedAuthorities.size());
		
		iterator = grantedAuthorities.iterator();
		
		assertEquals(CoreAuthorityConstants.ROLE_ANONYMOUS, iterator.next().getAuthority());
		assertEquals(CoreAuthorityConstants.ROLE_AUTHENTICATED, iterator.next().getAuthority());
		assertEquals(ROLE_GROUP_2, iterator.next().getAuthority());
		
		// Test reimplemented QueryDSL methods
		MockUser personInactive = createMockPerson("inactive", "inactive", "inactive");
		personInactive.setEnabled(false);
		mockUserService.update(personInactive);
		
		assertEquals(group1, mockUserGroupService.getByName("group1"));
		assertEquals(new Long(4), mockUserService.count());
		assertEquals(new Long(3), mockUserService.countEnabled());
	}
}

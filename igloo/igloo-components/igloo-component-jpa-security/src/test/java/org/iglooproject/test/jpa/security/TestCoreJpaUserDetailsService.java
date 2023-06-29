package org.iglooproject.test.jpa.security;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;
import java.util.Iterator;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants;
import org.iglooproject.test.AbstractJpaSecurityTestCase;
import org.iglooproject.test.jpa.security.business.person.model.MockUser;
import org.iglooproject.test.jpa.security.business.person.model.MockUserGroup;
import org.iglooproject.test.jpa.security.config.spring.SpringBootTestJpaSecurity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;

import igloo.security.ICoreUserDetailsService;
import igloo.security.UserDetails;

@SpringBootTestJpaSecurity
class TestCoreJpaUserDetailsService extends AbstractJpaSecurityTestCase {

	@Autowired
	private ICoreUserDetailsService coreJpaUserDetailsService;

	@SuppressWarnings("unchecked")
	@Test
	void testLoadUserByUsername() throws ServiceException, SecurityServiceException {
		MockUserGroup adminGroup = createMockUserGroup("adminGroup");
		adminGroup.addAuthority(authorityService.getByName(CoreAuthorityConstants.ROLE_ADMIN));
		
		MockUserGroup group1 = createMockUserGroup("group1");
		group1.addAuthority(authorityService.getByName(ROLE_GROUP_1));
		
		MockUserGroup group2 = createMockUserGroup("group2");
		group2.addAuthority(authorityService.getByName(ROLE_GROUP_2));
		
		mockUserGroupService.update(adminGroup);
		mockUserGroupService.update(group1);
		mockUserGroupService.update(group2);
		
		MockUser userAdmin = createMockUser("admin", "admin", "admin");
		MockUser userGroup1 = createMockUser("userGroup1", "userGroup1", "userGroup1");
		MockUser userGroup2 = createMockUser("userGroup2", "userGroup2", "userGroup2");
		
		mockUserGroupService.addUser(adminGroup, userAdmin);
		mockUserGroupService.addUser(group1, userGroup1);
		mockUserGroupService.addUser(group2, userGroup2);
		
		Collection<GrantedAuthority> grantedAuthorities;
		Iterator<GrantedAuthority> iterator;
		UserDetails userDetails;
		
		// Admin person
		userDetails = coreJpaUserDetailsService.loadUserByUsername(userAdmin.getUsername());
		
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
		userDetails = coreJpaUserDetailsService.loadUserByUsername(userGroup1.getUsername());
		
		grantedAuthorities = (Collection<GrantedAuthority>) userDetails.getAuthorities();
		
		assertEquals(4, grantedAuthorities.size());
		
		iterator = grantedAuthorities.iterator();
		
		assertEquals(CoreAuthorityConstants.ROLE_ANONYMOUS, iterator.next().getAuthority());
		assertEquals(CoreAuthorityConstants.ROLE_AUTHENTICATED, iterator.next().getAuthority());
		assertEquals(ROLE_GROUP_1, iterator.next().getAuthority());
		assertEquals(ROLE_GROUP_3, iterator.next().getAuthority());
		
		// Group2 person
		userDetails = coreJpaUserDetailsService.loadUserByUsername(userGroup2.getUsername());
		
		grantedAuthorities = (Collection<GrantedAuthority>) userDetails.getAuthorities();
		
		assertEquals(3, grantedAuthorities.size());
		
		iterator = grantedAuthorities.iterator();
		
		assertEquals(CoreAuthorityConstants.ROLE_ANONYMOUS, iterator.next().getAuthority());
		assertEquals(CoreAuthorityConstants.ROLE_AUTHENTICATED, iterator.next().getAuthority());
		assertEquals(ROLE_GROUP_2, iterator.next().getAuthority());
		
		// Test reimplemented QueryDSL methods
		MockUser userInactive = createMockUser("inactive", "inactive", "inactive");
		userInactive.setEnabled(false);
		mockUserService.update(userInactive);
		
		assertEquals(group1, mockUserGroupService.getByName("group1"));
		assertEquals(Long.valueOf(4), mockUserService.count());
		assertEquals(Long.valueOf(3), mockUserService.countEnabled());
	}
}

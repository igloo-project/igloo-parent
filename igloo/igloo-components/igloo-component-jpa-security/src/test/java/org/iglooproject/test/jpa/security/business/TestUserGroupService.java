package org.iglooproject.test.jpa.security.business;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.security.business.authority.model.Authority;
import org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants;
import org.iglooproject.test.AbstractJpaSecurityTestCase;
import org.iglooproject.test.jpa.security.business.person.model.MockUser;
import org.iglooproject.test.jpa.security.business.person.model.MockUserGroup;
import org.iglooproject.test.jpa.security.config.spring.SpringBootTestJpaSecurity;
import org.junit.jupiter.api.Test;

@SpringBootTestJpaSecurity
class TestUserGroupService extends AbstractJpaSecurityTestCase {

	@Test
	void testAuthorities() throws ServiceException, SecurityServiceException {
		MockUserGroup group1 = createMockUserGroup("group1");
		MockUserGroup group2 = createMockUserGroup("group2");
		
		Authority adminAuthority = authorityService.getByName(CoreAuthorityConstants.ROLE_ADMIN);
		Authority group1Authority = authorityService.getByName(ROLE_GROUP_1);

		group1.addAuthority(adminAuthority);
		group1.addAuthority(group1Authority);
		
		mockUserGroupService.update(group1);
		
		assertEquals(2, group1.getAuthorities().size());
		
		group2.addAuthority(adminAuthority);
		group2.addAuthority(group1Authority);
		
		mockUserGroupService.update(group2);
		
		assertEquals(2, group2.getAuthorities().size());
		
		mockUserGroupService.delete(group1);
		
		assertEquals(2, group2.getAuthorities().size());
		
		group2.removeAuthority(adminAuthority);
		
		mockUserGroupService.update(group2);
		
		assertEquals(1, group2.getAuthorities().size());
	}
	
	@Test
	void testMembers() throws ServiceException, SecurityServiceException {
		MockUserGroup group1 = createMockUserGroup("group1");
		
		MockUser user1 = createMockUser("user1", "user1", "user1");
		MockUser user2 = createMockUser("user2", "user2", "user2");

		mockUserGroupService.addUser(group1, user1);
		
		user1 = mockUserService.getByUsername(user1.getUsername());
		
		assertEquals(1, mockUserGroupService.listUsersByUserGroup(group1).size());
		assertEquals(1, user1.getGroups().size());
		
		mockUserGroupService.addUser(group1, user2);
		
		assertEquals(2, mockUserGroupService.listUsersByUserGroup(group1).size());
		
		mockUserGroupService.removeUser(group1, user2);
		
		assertEquals(1, mockUserGroupService.listUsersByUserGroup(group1).size());
	}
}

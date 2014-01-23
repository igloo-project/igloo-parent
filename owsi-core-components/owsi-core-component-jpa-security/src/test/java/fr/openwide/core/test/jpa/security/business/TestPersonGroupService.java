package fr.openwide.core.test.jpa.security.business;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.security.business.authority.model.Authority;
import fr.openwide.core.jpa.security.business.authority.util.CoreAuthorityConstants;
import fr.openwide.core.test.AbstractJpaSecurityTestCase;
import fr.openwide.core.test.jpa.security.business.person.model.MockPerson;
import fr.openwide.core.test.jpa.security.business.person.model.MockPersonGroup;

public class TestPersonGroupService extends AbstractJpaSecurityTestCase {

	@Test
	public void testAuthorities() throws ServiceException, SecurityServiceException {
		MockPersonGroup group1 = createMockPersonGroup("group1");
		MockPersonGroup group2 = createMockPersonGroup("group2");
		
		Authority adminAuthority = authorityService.getByName(CoreAuthorityConstants.ROLE_ADMIN);
		Authority group1Authority = authorityService.getByName(ROLE_GROUP_1);

		group1.addAuthority(adminAuthority);
		group1.addAuthority(group1Authority);
		
		mockPersonGroupService.update(group1);
		
		assertEquals(2, group1.getAuthorities().size());
		
		group2.addAuthority(adminAuthority);
		group2.addAuthority(group1Authority);
		
		mockPersonGroupService.update(group2);
		
		assertEquals(2, group2.getAuthorities().size());
		
		mockPersonGroupService.delete(group1);
		
		assertEquals(2, group2.getAuthorities().size());
		
		group2.removeAuthority(adminAuthority);
		
		mockPersonGroupService.update(group2);
		
		assertEquals(1, group2.getAuthorities().size());
	}
	
	@Test
	public void testMembers() throws ServiceException, SecurityServiceException {
		MockPersonGroup group1 = createMockPersonGroup("group1");
		
		MockPerson user1 = createMockPerson("user1", "user1", "user1");
		MockPerson user2 = createMockPerson("user2", "user2", "user2");
		
		group1.addPerson(user1);
		mockPersonGroupService.update(group1);
		
		user1 = mockPersonService.getByUserName(user1.getUserName());
		
		assertEquals(1, group1.getPersons().size());
		assertEquals(1, user1.getUserGroups().size());
		
		group1.addPerson(user2);
		
		assertEquals(2, group1.getPersons().size());
		
		group1.removePerson(user1);
		
		assertEquals(1, group1.getPersons().size());
	}
}

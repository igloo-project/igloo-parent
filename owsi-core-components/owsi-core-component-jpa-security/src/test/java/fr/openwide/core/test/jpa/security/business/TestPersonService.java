package fr.openwide.core.test.jpa.security.business;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.security.business.authority.model.Authority;
import fr.openwide.core.jpa.security.business.authority.util.CoreAuthorityConstants;
import fr.openwide.core.test.AbstractJpaSecurityTestCase;
import fr.openwide.core.test.jpa.security.business.person.model.MockPerson;

public class TestPersonService extends AbstractJpaSecurityTestCase {
	
	@Test
	public void testAuthorities() throws ServiceException, SecurityServiceException {
		MockPerson person1 = createMockPerson("login1", "firstName1", "lastName1");
		MockPerson person2 = createMockPerson("login2", "firstName2", "lastName2");
		
		Authority adminAuthority = authorityService.getByName(CoreAuthorityConstants.ROLE_ADMIN);
		Authority group1Authority = authorityService.getByName(ROLE_GROUP_1);
		
		person1.addAuthority(adminAuthority);
		person1.addAuthority(group1Authority);
		
		mockPersonService.update(person1);
		
		assertEquals(3, person1.getAuthorities().size());
		
		person2.addAuthority(adminAuthority);
		person2.addAuthority(group1Authority);
		
		mockPersonService.update(person2);
		
		assertEquals(3, person2.getAuthorities().size());
		
		mockPersonService.delete(person1);
		
		assertEquals(3, person2.getAuthorities().size());
		
		person2.removeAuthority(adminAuthority);
		
		mockPersonService.update(person2);
		
		assertEquals(2, person2.getAuthorities().size());
	}
}

package fr.openwide.core.test.jpa.security.business;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.List;

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
	
	@Test
	// TODO : déplacer dans core-jpa
	public void testSearch() throws ServiceException, SecurityServiceException {
		MockPerson person1 = createMockPerson("login1", "firstName1", "lastName1");
		MockPerson person2 = createMockPerson("login2", "firstName2", "lastName2");
		
		mockPersonService.flush();
		
		List<MockPerson> persons;
		
		persons = mockPersonService.search("login1");
		
		assertEquals(1, persons.size());
		assertEquals(person1, persons.get(0));
		
		persons = mockPersonService.search("firstName1");
		
		assertEquals(1, persons.size());
		assertEquals(person1, persons.get(0));
		
		persons = mockPersonService.search("lastName2");
		
		assertEquals(1, persons.size());
		assertEquals(person2, persons.get(0));
	}
	
	@Test
	// TODO : déplacer dans core-jpa
	public void testSearchAutocomplete() throws ServiceException, SecurityServiceException {
		MockPerson person1 = createMockPerson("login1t", "firstName1", "lastName1");
		MockPerson person2 = createMockPerson("login2te", "firstName2", "lastName2");
		MockPerson person3 = createMockPerson("login3test", "firstName31 firstName32", "last-name3");
		
		List<MockPerson> persons;
		
		persons = mockPersonService.searchAutocomplete("login1");
		
		assertEquals(1, persons.size());
		assertEquals(person1, persons.get(0));
		
		persons = mockPersonService.searchAutocomplete("login");
		
		assertEquals(3, persons.size());
		
		persons = mockPersonService.searchAutocomplete("firstName");
		
		assertEquals(3, persons.size());
		
		persons = mockPersonService.searchAutocomplete("lastName");
		Collections.sort(persons);
		
		assertEquals(2, persons.size());
		assertEquals(person1, persons.get(0));
		assertEquals(person2, persons.get(1));
		
		persons = mockPersonService.searchAutocomplete("Name2");
		
		/*
		 * Cette recherche retourne 0 résultats à cause de la configuration de
		 * l'analyzer fullText dans la classe Parameter.
		 */
		assertEquals(0, persons.size());
		
		persons = mockPersonService.searchAutocomplete("firstName31 firstName32");
		
		assertEquals(1, persons.size());
		assertEquals(person3, persons.get(0));
		
		persons = mockPersonService.searchAutocomplete("last-Na");
		Collections.sort(persons);
		
		assertEquals(1, persons.size());
		assertEquals(person3, persons.get(0));
		
		persons = mockPersonService.searchAutocomplete("last");
		Collections.sort(persons);
		
		assertEquals(3, persons.size());
		assertEquals(person1, persons.get(0));
		assertEquals(person2, persons.get(1));
		assertEquals(person3, persons.get(2));
	}
}

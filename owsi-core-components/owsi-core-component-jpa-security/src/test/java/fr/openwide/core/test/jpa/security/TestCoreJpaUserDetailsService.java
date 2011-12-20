package fr.openwide.core.test.jpa.security;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.security.business.authority.util.CoreAuthorityConstants;
import fr.openwide.core.test.AbstractJpaSecurityTestCase;
import fr.openwide.core.test.jpa.security.business.person.model.MockPerson;
import fr.openwide.core.test.jpa.security.business.person.model.MockPersonGroup;

public class TestCoreJpaUserDetailsService extends AbstractJpaSecurityTestCase {

	@Autowired
	private UserDetailsService coreJpaUserDetailsService;

	@SuppressWarnings("unchecked")
	@Test
	public void testLoadUserByUsername() throws ServiceException, SecurityServiceException {
		MockPersonGroup adminGroup = createMockPersonGroup("adminGroup");
		adminGroup.addAuthority(authorityService.getByName(CoreAuthorityConstants.ROLE_ADMIN));
		
		MockPersonGroup group1 = createMockPersonGroup("group1");
		group1.addAuthority(authorityService.getByName(ROLE_GROUP_1));
		
		MockPersonGroup group2 = createMockPersonGroup("group2");
		group2.addAuthority(authorityService.getByName(ROLE_GROUP_2));
		
		mockPersonGroupService.update(adminGroup);
		mockPersonGroupService.update(group1);
		mockPersonGroupService.update(group2);
		
		MockPerson personAdmin = createMockPerson("admin", "admin", "admin");
		MockPerson personGroup1 = createMockPerson("userGroup1", "userGroup1", "userGroup1");
		MockPerson personGroup2 = createMockPerson("userGroup2", "userGroup2", "userGroup2");
		
		mockPersonGroupService.addPerson(adminGroup, personAdmin);
		mockPersonGroupService.addPerson(group1, personGroup1);
		mockPersonGroupService.addPerson(group2, personGroup2);
		
		Collection<GrantedAuthority> grantedAuthorities;
		Iterator<GrantedAuthority> iterator;
		UserDetails userDetails;
		
		// Admin person
		userDetails = coreJpaUserDetailsService.loadUserByUsername(personAdmin.getUserName());
		
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
		userDetails = coreJpaUserDetailsService.loadUserByUsername(personGroup1.getUserName());
		
		grantedAuthorities = (Collection<GrantedAuthority>) userDetails.getAuthorities();
		
		assertEquals(4, grantedAuthorities.size());
		
		iterator = grantedAuthorities.iterator();
		
		assertEquals(CoreAuthorityConstants.ROLE_ANONYMOUS, iterator.next().getAuthority());
		assertEquals(CoreAuthorityConstants.ROLE_AUTHENTICATED, iterator.next().getAuthority());
		assertEquals(ROLE_GROUP_1, iterator.next().getAuthority());
		assertEquals(ROLE_GROUP_3, iterator.next().getAuthority());
		
		// Group2 person
		userDetails = coreJpaUserDetailsService.loadUserByUsername(personGroup2.getUserName());
		
		grantedAuthorities = (Collection<GrantedAuthority>) userDetails.getAuthorities();
		
		assertEquals(3, grantedAuthorities.size());
		
		iterator = grantedAuthorities.iterator();
		
		assertEquals(CoreAuthorityConstants.ROLE_ANONYMOUS, iterator.next().getAuthority());
		assertEquals(CoreAuthorityConstants.ROLE_AUTHENTICATED, iterator.next().getAuthority());
		assertEquals(ROLE_GROUP_2, iterator.next().getAuthority());
	}
}

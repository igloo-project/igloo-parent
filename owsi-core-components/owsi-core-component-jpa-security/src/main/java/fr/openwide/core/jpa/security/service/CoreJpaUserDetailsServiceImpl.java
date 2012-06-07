package fr.openwide.core.jpa.security.service;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import fr.openwide.core.jpa.security.business.authority.model.Authority;
import fr.openwide.core.jpa.security.business.person.model.IPerson;
import fr.openwide.core.jpa.security.business.person.model.IPersonGroup;
import fr.openwide.core.jpa.security.business.person.service.IPersonService;

public class CoreJpaUserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private IPersonService<? extends IPerson> personService; 
	
	@Autowired
	private RoleHierarchy roleHierarchy;

	@Resource(name = "authenticationUserNameComparison")
	private AuthenticationUserNameComparison authenticationUserNameComparison;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException, DataAccessException {
		IPerson person;
		if (AuthenticationUserNameComparison.CASE_INSENSITIVE.equals(authenticationUserNameComparison)) {
			person = personService.getByUserNameCaseInsensitive(userName);
		} else {
			person = personService.getByUserName(userName);
		}
		
		if (person == null) {
			throw new UsernameNotFoundException("CoreHibernateUserDetailsServiceImpl: User not found: " + userName);
		}
		
		Set<GrantedAuthority> grantedAuthorities = new HashSet<GrantedAuthority>();
		
		addAuthorities(grantedAuthorities, person.getAuthorities());
		
		for (IPersonGroup personGroup : person.getPersonGroups()) {
			addAuthorities(grantedAuthorities, personGroup.getAuthorities());
		}
		
		User userDetails = new User(person.getUserName(), person.getPasswordHash(), person.isActive(), true, true, true, 
				roleHierarchy.getReachableGrantedAuthorities(grantedAuthorities));
		
		return userDetails;
	}
	
	protected void addAuthorities(Set<GrantedAuthority> grantedAuthorities, Set<Authority> authorities) {
		for (Authority authority : authorities) {
			grantedAuthorities.add(new SimpleGrantedAuthority(authority.getName()));
		}
	}

}

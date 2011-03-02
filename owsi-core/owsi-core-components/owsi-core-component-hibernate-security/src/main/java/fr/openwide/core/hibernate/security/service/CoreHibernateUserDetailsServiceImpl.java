package fr.openwide.core.hibernate.security.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import fr.openwide.core.hibernate.security.business.authority.model.Authority;
import fr.openwide.core.hibernate.security.business.person.model.CorePerson;
import fr.openwide.core.hibernate.security.business.person.model.CorePersonGroup;
import fr.openwide.core.hibernate.security.business.person.service.PersonService;

public class CoreHibernateUserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private PersonService personService; 
	
	private RoleHierarchy roleHierarchy;

	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException, DataAccessException {
		CorePerson person = personService.getByUserName(userName);
		
		if (person == null) {
			throw new UsernameNotFoundException("CoreHibernateUserDetailsServiceImpl: User not found: " + userName);
		}
		
		Set<GrantedAuthority> grantedAuthorities = new HashSet<GrantedAuthority>();
		
		addAuthorities(grantedAuthorities, person.getAuthorities());
		
		for (CorePersonGroup personGroup : person.getGroups()) {
			addAuthorities(grantedAuthorities, personGroup.getAuthorities());
		}
		
		User userDetails = new User(userName, person.getMd5Password(), person.isActive(), true, true, true, 
				roleHierarchy.getReachableGrantedAuthorities(grantedAuthorities));
		
		return userDetails;
	}
	
	protected void addAuthorities(Set<GrantedAuthority> grantedAuthorities, Set<Authority> authorities) {
		for (Authority authority : authorities) {
			grantedAuthorities.add(new GrantedAuthorityImpl(authority.getName()));
		}
	}
	
	public RoleHierarchy getRoleHierarchy() {
		return roleHierarchy;
	}

	public void setRoleHierarchy(RoleHierarchy roleHierarchy) {
		this.roleHierarchy = roleHierarchy;
	}
}

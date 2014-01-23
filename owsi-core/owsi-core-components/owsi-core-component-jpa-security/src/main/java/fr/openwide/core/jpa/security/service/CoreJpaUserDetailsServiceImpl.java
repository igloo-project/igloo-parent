package fr.openwide.core.jpa.security.service;

import java.util.Set;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;

import com.google.common.collect.Sets;

import fr.openwide.core.jpa.security.business.authority.model.Authority;
import fr.openwide.core.jpa.security.business.person.model.IPerson;
import fr.openwide.core.jpa.security.business.person.model.IPersonGroup;
import fr.openwide.core.jpa.security.business.person.service.IPersonService;
import fr.openwide.core.jpa.security.hierarchy.IPermissionHierarchy;
import fr.openwide.core.jpa.security.model.CoreUserDetails;

public class CoreJpaUserDetailsServiceImpl implements UserDetailsService {
	
	private static final String EMPTY_PASSWORD_HASH = "* NO PASSWORD *";

	@Autowired
	private IPersonService<? extends IPerson> personService; 
	
	@Autowired
	private RoleHierarchy roleHierarchy;
	
	@Autowired
	private IPermissionHierarchy permissionHierarchy;

	private AuthenticationUserNameComparison authenticationUserNameComparison = AuthenticationUserNameComparison.CASE_SENSITIVE;

	public void setAuthenticationUserNameComparison(AuthenticationUserNameComparison authenticationUserNameComparison) {
		this.authenticationUserNameComparison = authenticationUserNameComparison;
	}
	
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
		
		Pair<Set<GrantedAuthority>, Set<Permission>> authoritiesAndPermissions = getAuthoritiesAndPermissions(person);
		
		// In any case, we can't pass an empty password hash to the CoreUserDetails
		
		CoreUserDetails userDetails = new CoreUserDetails(person.getUserName(),
				StringUtils.hasText(person.getPasswordHash()) ? person.getPasswordHash() : EMPTY_PASSWORD_HASH,
				person.isActive(), true, true, true, 
				roleHierarchy.getReachableGrantedAuthorities(authoritiesAndPermissions.getLeft()),
				permissionHierarchy.getAcceptablePermissions(authoritiesAndPermissions.getRight()));
		
		return userDetails;
	}
	
	protected Pair<Set<GrantedAuthority>, Set<Permission>> getAuthoritiesAndPermissions(IPerson person) {
		Set<GrantedAuthority> grantedAuthorities = Sets.newHashSet();
		Set<Permission> permissions = Sets.newHashSet();
		
		addAuthorities(grantedAuthorities, person.getAuthorities());
		permissions.addAll(person.getPermissions());
		
		for (IPersonGroup personGroup : person.getPersonGroups()) {
			addAuthorities(grantedAuthorities, personGroup.getAuthorities());
			permissions.addAll(personGroup.getPermissions());
		}
		
		return new ImmutablePair<Set<GrantedAuthority>, Set<Permission>>(grantedAuthorities, permissions);
	}
	
	protected void addAuthorities(Set<GrantedAuthority> grantedAuthorities, Set<Authority> authorities) {
		for (Authority authority : authorities) {
			grantedAuthorities.add(new SimpleGrantedAuthority(authority.getName()));
		}
	}

}

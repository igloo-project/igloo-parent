package org.iglooproject.jpa.security.service;

import java.util.Collection;
import java.util.Set;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.iglooproject.jpa.security.business.authority.model.Authority;
import org.iglooproject.jpa.security.business.authority.service.IAuthorityService;
import org.iglooproject.jpa.security.business.person.model.IGroupedUser;
import org.iglooproject.jpa.security.business.person.model.IUserGroup;
import org.iglooproject.jpa.security.business.person.service.ISecurityUserService;
import org.iglooproject.jpa.security.hierarchy.IPermissionHierarchy;
import org.iglooproject.jpa.security.model.CoreUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.acls.domain.PermissionFactory;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;

import com.google.common.collect.Sets;

public class CoreJpaUserDetailsServiceImpl implements UserDetailsService {

	public static final String EMPTY_PASSWORD_HASH = "*NO PASSWORD*";

	@Autowired
	private ISecurityUserService<?> userService; 
	
	@Autowired
	private RoleHierarchy roleHierarchy;
	
	@Autowired
	private IPermissionHierarchy permissionHierarchy;
	
	@Autowired
	private PermissionFactory permissionFactory;
	
	@Autowired
	private IAuthorityService authorityService;
	
	private AuthenticationUsernameComparison authenticationUsernameComparison = AuthenticationUsernameComparison.CASE_SENSITIVE;

	public void setAuthenticationUsernameComparison(AuthenticationUsernameComparison authenticationUsernameComparison) {
		this.authenticationUsernameComparison = authenticationUsernameComparison;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
		IGroupedUser<?> user = getUserByUsername(username);
		
		if (user == null) {
			throw new UsernameNotFoundException("CoreJpaUserDetailsServiceImpl: User not found: " + username);
		}
		
		Pair<Set<GrantedAuthority>, Set<Permission>> authoritiesAndPermissions = getAuthoritiesAndPermissions(user);
		Collection<? extends GrantedAuthority> expandedGrantedAuthorities = roleHierarchy.getReachableGrantedAuthorities(authoritiesAndPermissions.getLeft());
		addCustomPermissionsFromAuthorities(expandedGrantedAuthorities, authoritiesAndPermissions.getRight());
		addPermissionsFromAuthorities(expandedGrantedAuthorities, authoritiesAndPermissions.getRight());
		Collection<Permission> expandedReachablePermissions = permissionHierarchy.getReachablePermissions(authoritiesAndPermissions.getRight());
		
		return new CoreUserDetails(
			user.getUsername(),
			// In any case, we can't pass an empty password hash to the CoreUserDetails
			StringUtils.hasText(user.getPasswordHash()) ? user.getPasswordHash() : EMPTY_PASSWORD_HASH,
			user.isEnabled(),
			true,
			true,
			true, 
			expandedGrantedAuthorities,
			expandedReachablePermissions
		);
	}
	
	protected IGroupedUser<?> getUserByUsername(String username) {
		IGroupedUser<?> user;
		if (AuthenticationUsernameComparison.CASE_INSENSITIVE.equals(authenticationUsernameComparison)) {
			user = userService.getByUsernameCaseInsensitive(username);
		} else {
			user = userService.getByUsername(username);
		}
		return user;
	}
	
	protected void addCustomPermissionsFromAuthorities(Collection<? extends GrantedAuthority> expandedGrantedAuthorities, Set<Permission> permissions) {
		for (GrantedAuthority grantedAuthority : expandedGrantedAuthorities) {
			Authority authority = authorityService.getByName(grantedAuthority.getAuthority());
			if (authority == null) {
				continue;
			}
			addPermissions(permissions, authority.getCustomPermissionNames());
		}
	}
	
	/**
	 * Override this in order to define some permissions based on the (expanded) granted authorities.
	 */
	protected void addPermissionsFromAuthorities(Collection<? extends GrantedAuthority> expandedGrantedAuthorities, Set<Permission> permissions) {
		// Does nothing by default
	}

	protected Pair<Set<GrantedAuthority>, Set<Permission>> getAuthoritiesAndPermissions(IGroupedUser<?> user) {
		Set<GrantedAuthority> grantedAuthorities = Sets.newHashSet();
		Set<Permission> permissions = Sets.newHashSet();
		
		addAuthorities(grantedAuthorities, user.getAuthorities());
		permissions.addAll(user.getPermissions());
		
		for (IUserGroup personGroup : user.getGroups()) {
			addAuthorities(grantedAuthorities, personGroup.getAuthorities());
			permissions.addAll(personGroup.getPermissions());
		}
		
		return new ImmutablePair<>(grantedAuthorities, permissions);
	}
	
	protected void addAuthorities(Set<GrantedAuthority> grantedAuthorities, Set<Authority> authorities) {
		for (Authority authority : authorities) {
			grantedAuthorities.add(new SimpleGrantedAuthority(authority.getName()));
		}
	}
	
	protected void addPermissions(Set<Permission> permissions, Collection<String> permissionNames) {
		for (String permissionName : permissionNames) {
			permissions.add(permissionFactory.buildFromName(permissionName));
		}
	}

}

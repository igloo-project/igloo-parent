package fr.openwide.core.jpa.security.service;

import java.util.Collection;
import java.util.Set;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.acls.domain.PermissionFactory;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;

import com.google.common.collect.Sets;

import fr.openwide.core.jpa.security.business.authority.model.Authority;
import fr.openwide.core.jpa.security.business.authority.service.IAuthorityService;
import fr.openwide.core.jpa.security.business.person.model.IGroupedUser;
import fr.openwide.core.jpa.security.business.person.model.IUserGroup;
import fr.openwide.core.jpa.security.business.person.service.IGenericUserService;
import fr.openwide.core.jpa.security.hierarchy.IPermissionHierarchy;
import fr.openwide.core.jpa.security.model.CoreUserDetails;

public class CoreJpaUserDetailsServiceImpl implements UserDetailsService {
	
	private static final String EMPTY_PASSWORD_HASH = "* NO PASSWORD *";

	@Autowired
	private IGenericUserService<?> userService; 
	
	@Autowired
	private RoleHierarchy roleHierarchy;
	
	@Autowired
	private IPermissionHierarchy permissionHierarchy;
	
	@Autowired
	private PermissionFactory permissionFactory;
	
	@Autowired
	private IAuthorityService authorityService;
	
	private AuthenticationUserNameComparison authenticationUserNameComparison = AuthenticationUserNameComparison.CASE_SENSITIVE;

	public void setAuthenticationUserNameComparison(AuthenticationUserNameComparison authenticationUserNameComparison) {
		this.authenticationUserNameComparison = authenticationUserNameComparison;
	}
	
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException, DataAccessException {
		IGroupedUser<?> user = getUserByUsername(userName);
		
		if (user == null) {
			throw new UsernameNotFoundException("CoreHibernateUserDetailsServiceImpl: User not found: " + userName);
		}
		
		if (!user.isActive()) {
			throw new DisabledException("User is disabled");
		}
		
		Pair<Set<GrantedAuthority>, Set<Permission>> authoritiesAndPermissions = getAuthoritiesAndPermissions(user);
		Collection<? extends GrantedAuthority> expandedGrantedAuthorities = roleHierarchy.getReachableGrantedAuthorities(authoritiesAndPermissions.getLeft());
		addCustomPermissionsFromAuthorities(expandedGrantedAuthorities, authoritiesAndPermissions.getRight());
		addPermissionsFromAuthorities(expandedGrantedAuthorities, authoritiesAndPermissions.getRight());
		Collection<Permission> expandedReachablePermissions = permissionHierarchy.getReachablePermissions(authoritiesAndPermissions.getRight());
		
		// In any case, we can't pass an empty password hash to the CoreUserDetails
		
		CoreUserDetails userDetails = new CoreUserDetails(user.getUserName(),
				StringUtils.hasText(user.getPasswordHash()) ? user.getPasswordHash() : EMPTY_PASSWORD_HASH,
				user.isActive(), true, true, true, 
				expandedGrantedAuthorities, expandedReachablePermissions);
		
		return userDetails;
	}
	
	protected IGroupedUser<?> getUserByUsername(String userName) {
		IGroupedUser<?> user;
		if (AuthenticationUserNameComparison.CASE_INSENSITIVE.equals(authenticationUserNameComparison)) {
			user = userService.getByUserNameCaseInsensitive(userName);
		} else {
			user = userService.getByUserName(userName);
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
		
		return new ImmutablePair<Set<GrantedAuthority>, Set<Permission>>(grantedAuthorities, permissions);
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

package fr.openwide.core.jpa.security.service;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;

import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;

import fr.openwide.core.jpa.security.business.person.model.IUser;

public interface ISecurityService {
	
	boolean hasPermission(Authentication authentication, Permission requirePermission);
	
	boolean hasPermission(IUser person, Permission requirePermission);
	
	boolean hasPermission(Authentication authentication, Object securedObject,
			Permission requirePermission);
	
	boolean hasPermission(IUser person, Object securedObject,
			Permission requirePermission);
	
	boolean hasRole(Authentication authentication, String role);
	
	boolean hasRole(IUser person, String role);

	boolean hasSystemRole(Authentication authentication);
	
	boolean hasSystemRole(IUser person);
	
	boolean hasAdminRole(Authentication authentication);
	
	boolean hasAdminRole(IUser person);
	
	boolean hasAuthenticatedRole(Authentication authentication);
	
	boolean hasAuthenticatedRole(IUser person);
	
	boolean isAnonymousAuthority(String grantedAuthoritySid);

	List<? extends GrantedAuthority> getAuthorities(Authentication authentication);
	
	List<? extends GrantedAuthority> getAuthorities(IUser person);
	
	SecurityContext buildSecureContext(String userName);
	
	void clearAuthentication();
	
	<T> T runAsSystem(Callable<T> task);

	<T> T runAs(Callable<T> task, String userName, String... additionalAuthorities);

	Collection<? extends Permission> getPermissions(Authentication authentication);

	boolean isSuperUser(Authentication authentication);

}
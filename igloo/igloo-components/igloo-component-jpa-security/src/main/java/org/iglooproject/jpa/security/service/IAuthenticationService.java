package org.iglooproject.jpa.security.service;

import java.util.Collection;

import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public interface IAuthenticationService {

	String getUsername();
	
	boolean isLoggedIn();
	
	Collection<? extends GrantedAuthority> getAuthorities();

	boolean hasSystemRole();
	
	boolean hasAdminRole();
	
	boolean hasAuthenticatedRole();
	
	boolean hasRole(String role);
	
	public boolean hasPermission(Object securedObject, String permission);
	
	boolean hasPermission(Object securedObject, Permission permission);
	
	boolean hasPermission(String permission);
	
	boolean hasPermission(Permission permission);
	
	Collection<? extends Permission> getPermissions();
	
	boolean isAnonymousAuthority(String grantedAuthoritySid);
	
	void signOut();

	boolean isSuperUser();
	
	Authentication getAuthentication();
}

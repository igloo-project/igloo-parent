package fr.openwide.core.jpa.security.service;

import java.util.Collection;

import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.GrantedAuthority;

public interface IAuthenticationService {

	String getUserName();
	
	boolean isLoggedIn();
	
	Collection<? extends GrantedAuthority> getAuthorities();

	boolean hasSystemRole();
	
	boolean hasAdminRole();
	
	boolean hasAuthenticatedRole();
	
	boolean hasRole(String role);
	
	boolean hasPermission(Object securedObject, Permission permission);
	
	boolean hasPermission(Permission permission);
	
	Collection<? extends Permission> getPermissions();
	
	boolean isAnonymousAuthority(String grantedAuthoritySid);
	
	void signOut();

	boolean isSuperUser();
	
}

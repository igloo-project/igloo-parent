package fr.openwide.core.jpa.security.service;

import java.util.Collection;

import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.GrantedAuthority;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;

public interface IAuthenticationService {

	String getUserName();
	
	boolean isLoggedIn();
	
	Collection<GrantedAuthority> getAuthorities();

	boolean hasSystemRole();
	
	boolean hasAdminRole();
	
	boolean hasAuthenticatedRole();
	
	boolean hasRole(String role);
	
	boolean hasPermission(GenericEntity<?, ?> entity, Permission permission);
	
	boolean isAnonymousAuthority(String grantedAuthoritySid);
	
	void signOut();
	
}

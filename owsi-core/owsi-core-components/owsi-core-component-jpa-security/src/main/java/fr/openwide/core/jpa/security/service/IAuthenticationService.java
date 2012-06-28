package fr.openwide.core.jpa.security.service;

import java.util.List;

import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.GrantedAuthority;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;

public interface IAuthenticationService {

	String getUserName();
	
	boolean isLoggedIn();
	
	List<? extends GrantedAuthority> getAuthorities();

	boolean hasSystemRole();
	
	boolean hasAdminRole();
	
	boolean hasAuthenticatedRole();
	
	boolean hasRole(String role);
	
	boolean hasPermission(GenericEntity<?, ?> entity, Permission permission);
	
	boolean hasPermission(Permission permission);
	
	List<Permission> getPermissions();
	
	boolean isAnonymousAuthority(String grantedAuthoritySid);
	
	void signOut();
	
}

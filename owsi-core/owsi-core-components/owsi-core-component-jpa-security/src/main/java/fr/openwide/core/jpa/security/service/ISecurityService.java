package fr.openwide.core.jpa.security.service;

import java.util.List;
import java.util.concurrent.Callable;

import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.security.business.person.model.IPerson;

public interface ISecurityService {
	
	boolean hasPermission(Authentication authentication, Permission requirePermission);
	
	boolean hasPermission(IPerson person, Permission requirePermission);
	
	boolean hasPermission(Authentication authentication, GenericEntity<?, ?> securedObject,
			Permission requirePermission);
	
	boolean hasPermission(IPerson person, GenericEntity<?, ?> securedObject,
			Permission requirePermission);
	
	boolean hasRole(Authentication authentication, String role);
	
	boolean hasRole(IPerson person, String role);

	boolean hasSystemRole(Authentication authentication);
	
	boolean hasSystemRole(IPerson person);
	
	boolean hasAdminRole(Authentication authentication);
	
	boolean hasAdminRole(IPerson person);
	
	boolean hasAuthenticatedRole(Authentication authentication);
	
	boolean hasAuthenticatedRole(IPerson person);
	
	boolean isAnonymousAuthority(String grantedAuthoritySid);

	List<? extends GrantedAuthority> getAuthorities(Authentication authentication);
	
	List<? extends GrantedAuthority> getAuthorities(IPerson person);
	
	SecurityContext buildSecureContext(String userName);
	
	void clearAuthentication();
	
	<T> T runAsSystem(Callable<T> task);
	
	List<Permission> getPermissions(Authentication authentication);

	List<Permission> getPermissions(IPerson person);

}
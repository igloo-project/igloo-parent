package fr.openwide.core.jpa.security.service;

import java.util.Collection;

import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.security.business.person.model.IPerson;
import fr.openwide.core.jpa.security.runas.IRunAsTask;

public interface ISecurityService {
	
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

	Collection<? extends GrantedAuthority> getAuthorities(Authentication authentication);
	
	Collection<? extends GrantedAuthority> getAuthorities(IPerson authentication);
	
	SecurityContext buildSecureContext(String userName);
	
	<T> T runAsSystem(IRunAsTask<T> task);
	
}
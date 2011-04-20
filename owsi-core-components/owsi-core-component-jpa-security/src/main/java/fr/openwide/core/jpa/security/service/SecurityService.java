package fr.openwide.core.jpa.security.service;

import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.security.business.person.model.Person;
import fr.openwide.core.jpa.security.runas.RunAsTask;

public interface SecurityService {
	
	boolean hasPermission(Authentication authentication, GenericEntity<?, ?> securedObject,
			Permission requirePermission);
	
	boolean hasPermission(Person person, GenericEntity<?, ?> securedObject,
			Permission requirePermission);
	
	boolean hasRole(Authentication authentication, String role);
	
	boolean hasRole(Person person, String role);

	boolean hasSystemRole(Authentication authentication);
	
	boolean hasSystemRole(Person person);
	
	boolean hasAdminRole(Authentication authentication);
	
	boolean hasAdminRole(Person person);
	
	boolean hasAuthenticatedRole(Authentication authentication);
	
	boolean hasAuthenticatedRole(Person person);
	
	boolean isAnonymousAuthority(String grantedAuthoritySid);

	SecurityContext buildSecureContext(String userName);
	
	<T> T runAsSystem(RunAsTask<T> task);
	
}
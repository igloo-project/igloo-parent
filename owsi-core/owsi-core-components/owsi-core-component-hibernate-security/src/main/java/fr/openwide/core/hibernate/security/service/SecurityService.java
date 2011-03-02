package fr.openwide.core.hibernate.security.service;

import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

import fr.openwide.core.hibernate.business.generic.model.GenericEntity;
import fr.openwide.core.hibernate.security.acl.domain.User;
import fr.openwide.core.hibernate.security.runas.RunAsTask;

public interface SecurityService {
	
	boolean hasPermission(Authentication authentication, GenericEntity<?, ?> securedObject,
			Permission requirePermission);
	
	boolean hasPermission(User person, GenericEntity<?, ?> securedObject,
			Permission requirePermission);
	
	boolean hasRole(Authentication authentication, String role);
	
	boolean hasRole(User person, String role);

	boolean hasSystemRole(Authentication authentication);
	
	boolean hasSystemRole(User person);
	
	boolean hasAdminRole(Authentication authentication);
	
	boolean hasAdminRole(User person);
	
	boolean hasAuthenticatedRole(Authentication authentication);
	
	boolean hasAuthenticatedRole(User person);
	
	boolean isAnonymousAuthority(String grantedAuthoritySid);

	SecurityContext buildSecureContext(String userName);
	
	<T> T runAsSystem(RunAsTask<T> task);
	
}
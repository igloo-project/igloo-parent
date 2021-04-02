package org.iglooproject.jpa.security.service;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;

import org.iglooproject.jpa.security.business.person.model.IUser;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;

public interface ISecurityService extends IRunAsSystemService {

	boolean hasPermission(Authentication authentication, Permission requirePermission);

	boolean hasPermission(IUser user, Permission requirePermission);

	boolean hasPermission(Authentication authentication, Object securedObject, Permission requirePermission);

	boolean hasPermission(IUser user, Object securedObject, Permission requirePermission);

	boolean hasRole(Authentication authentication, String role);

	boolean hasRole(IUser user, String role);

	boolean hasSystemRole(Authentication authentication);

	boolean hasSystemRole(IUser user);

	boolean hasAdminRole(Authentication authentication);

	boolean hasAdminRole(IUser user);

	boolean hasAuthenticatedRole(Authentication authentication);

	boolean hasAuthenticatedRole(IUser user);

	boolean isAnonymousAuthority(String grantedAuthoritySid);

	List<GrantedAuthority> getAuthorities(Authentication authentication);

	List<GrantedAuthority> getAuthorities(IUser user);

	SecurityContext buildSecureContext(String username);

	void clearAuthentication();

	<T> T runAs(Callable<T> task, String username, String... additionalAuthorities);

	Collection<? extends Permission> getPermissions(Authentication authentication);

	boolean isSuperUser(Authentication authentication);

}

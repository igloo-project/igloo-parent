package fr.openwide.core.hibernate.security.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import fr.openwide.core.hibernate.business.generic.model.GenericEntity;
import fr.openwide.core.hibernate.security.business.authority.util.CoreAuthorityConstants;

public class CoreAuthenticationServiceImpl implements AuthenticationService {
	
	@Autowired
	private SecurityService securityService;
	
	@Override
	public String getUserName() {
		return AuthenticationUtil.getUserName();
	}
	
	@Override
	public boolean isLoggedIn() {
		return AuthenticationUtil.isLoggedIn();
	}
	
	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		return AuthenticationUtil.getAuthorities();
	}

	@Override
	public boolean hasSystemRole() {
		return securityService.hasSystemRole(AuthenticationUtil.getAuthentication());
	}
	
	@Override
	public boolean hasAdminRole() {
		return securityService.hasAdminRole(AuthenticationUtil.getAuthentication());
	}
	
	@Override
	public boolean hasAuthenticatedRole() {
		return securityService.hasAuthenticatedRole(AuthenticationUtil.getAuthentication());
	}
	
	@Override
	public boolean hasRole(String role) {
		return securityService.hasRole(AuthenticationUtil.getAuthentication(), role);
	}
	
	@Override
	public boolean hasPermission(GenericEntity<?, ?> entity, Permission permission) {
		return securityService.hasPermission(AuthenticationUtil.getAuthentication(), entity, permission);
	}
	
	@Override
	public boolean isAnonymousAuthority(String grantedAuthoritySid) {
		return CoreAuthorityConstants.ROLE_ANONYMOUS.equals(grantedAuthoritySid);
	}

	@Override
	public void signOut() {
		AuthenticationUtil.setAuthentication(null);
	}
	
	protected Authentication getAuthentication() {
		return AuthenticationUtil.getAuthentication();
	}

}
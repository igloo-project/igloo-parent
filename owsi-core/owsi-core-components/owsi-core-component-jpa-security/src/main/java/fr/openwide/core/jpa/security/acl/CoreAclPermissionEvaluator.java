package fr.openwide.core.jpa.security.acl;

import java.io.Serializable;

import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.core.Authentication;

import fr.openwide.core.jpa.security.service.ISecurityService;

public class CoreAclPermissionEvaluator extends AclPermissionEvaluator {
	
	private ISecurityService securityService;

	public CoreAclPermissionEvaluator(AclService aclService, ISecurityService securityService) {
		super(aclService);
	}

	@Override
	public boolean hasPermission(Authentication authentication, Object domainObject, Object permission) {
		return securityService.hasSystemRole(authentication) ||
				securityService.hasAdminRole(authentication) ||
				super.hasPermission(authentication, domainObject, permission);
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
			Object permission) {
		return securityService.hasSystemRole(authentication) ||
				securityService.hasAdminRole(authentication) ||
				super.hasPermission(authentication, targetId, targetType, permission);
	}

}
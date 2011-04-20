package fr.openwide.core.hibernate.security.acl;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.core.Authentication;

import fr.openwide.core.hibernate.security.service.SecurityService;

public class CoreAclPermissionEvaluator extends AclPermissionEvaluator {
	
	@Autowired
	private SecurityService securityService;

	public CoreAclPermissionEvaluator(AclService aclService) {
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
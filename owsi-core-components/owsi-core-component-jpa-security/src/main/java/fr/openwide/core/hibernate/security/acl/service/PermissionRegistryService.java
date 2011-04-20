package fr.openwide.core.hibernate.security.acl.service;

import org.springframework.security.acls.model.Permission;

public interface PermissionRegistryService {

	String getPermissionName(Permission permission);
	
	String getPermissionName(int permissionMask);
	
}

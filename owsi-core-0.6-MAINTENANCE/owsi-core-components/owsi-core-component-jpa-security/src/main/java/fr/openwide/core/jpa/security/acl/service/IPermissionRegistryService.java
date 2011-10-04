package fr.openwide.core.jpa.security.acl.service;

import org.springframework.security.acls.model.Permission;

public interface IPermissionRegistryService {

	String getPermissionName(Permission permission);
	
	String getPermissionName(int permissionMask);
	
}

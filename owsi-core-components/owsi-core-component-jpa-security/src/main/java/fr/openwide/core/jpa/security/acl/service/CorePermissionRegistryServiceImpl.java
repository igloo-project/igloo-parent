package fr.openwide.core.jpa.security.acl.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.PermissionFactory;
import org.springframework.security.acls.model.Permission;

import fr.openwide.core.jpa.security.acl.domain.CorePermissionConstants;

public class CorePermissionRegistryServiceImpl implements IPermissionRegistryService {
	
	@Autowired
	private PermissionFactory permissionFactory;
	
	private Map<Permission, String> permissions = new HashMap<Permission, String>();
	
	{
		registerPermission(BasePermission.READ, CorePermissionConstants.READ);
		registerPermission(BasePermission.WRITE, CorePermissionConstants.WRITE);
		registerPermission(BasePermission.ADMINISTRATION, CorePermissionConstants.ADMINISTRATION);
	}

	protected void registerPermission(Permission permission, String name) {
		this.permissions.put(permission, name);
	}

	@Override
	public String getPermissionName(Permission permission) {
		return permissions.get(permission);
	}
	
	@Override
	public String getPermissionName(int permissionMask) {
		return permissions.get(permissionFactory.buildFromMask(permissionMask));
	}
	
}
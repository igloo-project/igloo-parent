package fr.openwide.core.jpa.security.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.acls.domain.PermissionFactory;
import org.springframework.security.acls.model.Permission;
import org.springframework.util.Assert;

import fr.openwide.core.jpa.security.model.CoreNamedPermission;

/**
 * {@see org.springframework.security.acls.domain.DefaultPermissionFactory}
 */
public class NamedPermissionFactory implements PermissionFactory {

	private final Map<String, CoreNamedPermission> registeredPermissionsByName = new HashMap<String, CoreNamedPermission>();

	public NamedPermissionFactory() {
		registerPublicPermissions(CoreNamedPermission.class);
	}

	public NamedPermissionFactory(Class<? extends CoreNamedPermission> permissionClass) {
		registerPublicPermissions(permissionClass);
	}

	public NamedPermissionFactory(Collection<? extends CoreNamedPermission> permissions) {
		for (CoreNamedPermission permission : permissions) {
			registerPermission(permission, permission.getName());
		}
	}

	protected void registerPublicPermissions(Class<? extends CoreNamedPermission> clazz) {
		Assert.notNull(clazz, "Class required");
		Field[] fields = clazz.getFields();
		for (Field field : fields) {
			try {
				Object fieldValue = field.get(null);
				
				if (CoreNamedPermission.class.isAssignableFrom(fieldValue.getClass())) {
					// Found a NamedPermission static field
					CoreNamedPermission permission = (CoreNamedPermission) fieldValue;
					registerPermission(permission, permission.getName());
				}
			} catch (IllegalArgumentException e) {
				
			} catch (IllegalAccessException e) {
				
			}
		}
	}

	protected void registerPermission(CoreNamedPermission perm, String permissionName) {
		Assert.notNull(perm, "Permission required");
		Assert.hasText(permissionName, "Permission name required");
		// Ensure no existing Permission uses this code
		Assert.isTrue(!registeredPermissionsByName.containsKey(permissionName),
				"An existing Permission already provides name '" + permissionName + "'");
		// Register the new Permission
		registeredPermissionsByName.put(permissionName, perm);
	}

	@Override
	public Permission buildFromName(String name) {
		Permission p = registeredPermissionsByName.get(name);
		if (p == null) {
			throw new IllegalArgumentException("Unknown permission '" + name + "'");
		}
		return p;
	}

	@Override
	public List<Permission> buildFromNames(List<String> names) {
		if ((names == null) || (names.size() == 0)) {
			return Collections.emptyList();
		}
		List<Permission> permissions = new ArrayList<Permission>(names.size());
		for (String name : names) {
			permissions.add(buildFromName(name));
		}
		return permissions;
	}

	@Override
	public Permission buildFromMask(int mask) {
		throw new UnsupportedOperationException();
	}

}

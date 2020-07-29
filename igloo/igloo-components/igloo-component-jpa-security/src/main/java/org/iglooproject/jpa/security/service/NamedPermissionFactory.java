package org.iglooproject.jpa.security.service;

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

import org.iglooproject.jpa.security.model.NamedPermission;

/**
 * {@see org.springframework.security.acls.domain.DefaultPermissionFactory}
 */
public class NamedPermissionFactory implements PermissionFactory {

	private final Map<String, NamedPermission> registeredPermissionsByName = new HashMap<>();

	public NamedPermissionFactory() {
		registerPublicPermissions(NamedPermission.class);
	}

	@SuppressWarnings("unchecked")
	public NamedPermissionFactory(Class<? extends Permission> permissionClass) {
		if (!NamedPermission.class.isAssignableFrom(permissionClass)) {
			throw new IllegalStateException("NamedPermissionFactory only supports children of NamedPermission");
		}
		
		registerPublicPermissions((Class<? extends NamedPermission>) permissionClass);
	}

	public NamedPermissionFactory(Collection<? extends NamedPermission> permissions) {
		for (NamedPermission permission : permissions) {
			registerPermission(permission, permission.getName());
		}
	}

	protected void registerPublicPermissions(Class<? extends NamedPermission> clazz) {
		Assert.notNull(clazz, "Class required");
		Field[] fields = clazz.getFields();
		for (Field field : fields) {
			try {
				Object fieldValue = field.get(null);
				
				if (NamedPermission.class.isAssignableFrom(fieldValue.getClass())) {
					// Found a NamedPermission static field
					NamedPermission permission = (NamedPermission) fieldValue;
					registerPermission(permission, permission.getName());
				}
			} catch (IllegalArgumentException e) {
				
			} catch (IllegalAccessException e) {
				
			}
		}
	}

	protected void registerPermission(NamedPermission perm, String permissionName) {
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
		List<Permission> permissions = new ArrayList<>(names.size());
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

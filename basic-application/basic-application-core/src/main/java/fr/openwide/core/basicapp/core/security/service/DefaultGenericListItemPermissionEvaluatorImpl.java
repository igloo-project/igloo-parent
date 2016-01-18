package fr.openwide.core.basicapp.core.security.service;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.PermissionFactory;
import org.springframework.security.acls.model.Permission;
import org.springframework.stereotype.Service;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.jpa.more.business.generic.model.GenericListItem;
import fr.openwide.core.jpa.security.model.CorePermissionConstants;

@Service
public class DefaultGenericListItemPermissionEvaluatorImpl implements IDefaultGenericListItemPermissionEvaluator {
	
	@Autowired
	protected PermissionFactory permissionFactory;
	
	@Override
	public boolean hasPermission(User user, GenericListItem<?> genericListItem, Permission permission) {
		if (is(permission, CorePermissionConstants.READ)) {
			return true;
		} else if (is(permission, CorePermissionConstants.WRITE)) {
			return genericListItem.isEditable();
		} else if (is(permission, CorePermissionConstants.CREATE)) {
			return true;
		} else {
			return false;
		}
	}
	
	protected final boolean is(Permission permission, String ... permissionNames) {
		return is(permission, Arrays.asList(permissionNames));
	}
	
	protected final boolean is(Permission permission, Collection<String> permissionNames) {
		for (String permissionName : permissionNames) {
			Permission permissionFromName = permissionFactory.buildFromName(permissionName);
			if (permissionFromName.equals(permission)) {
				return true;
			}
		}
		
		return false;
	}
}

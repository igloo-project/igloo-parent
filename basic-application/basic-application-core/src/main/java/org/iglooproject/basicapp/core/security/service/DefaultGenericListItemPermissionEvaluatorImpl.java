package org.iglooproject.basicapp.core.security.service;

import org.springframework.security.acls.model.Permission;
import org.springframework.stereotype.Service;

import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.jpa.more.business.generic.model.GenericListItem;
import org.iglooproject.jpa.security.model.CorePermissionConstants;

@Service
public class DefaultGenericListItemPermissionEvaluatorImpl extends AbstractGenericPermissionEvaluator<GenericListItem<?>>
		implements IDefaultGenericListItemPermissionEvaluator {
	
	@Override
	public boolean hasPermission(User user, GenericListItem<?> genericListItem, Permission permission) {
		if (is(permission, CorePermissionConstants.READ)) {
			return true;
		} else if (is(permission, CorePermissionConstants.WRITE)) {
			return genericListItem.isEditable();
		} else {
			return is(permission, CorePermissionConstants.CREATE);
		}
	}
}

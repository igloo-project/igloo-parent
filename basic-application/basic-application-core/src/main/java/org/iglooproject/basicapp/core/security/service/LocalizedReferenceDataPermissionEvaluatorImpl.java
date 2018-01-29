package org.iglooproject.basicapp.core.security.service;

import org.iglooproject.basicapp.core.business.referencedata.model.LocalizedReferenceData;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.jpa.security.model.CorePermissionConstants;
import org.springframework.security.acls.model.Permission;
import org.springframework.stereotype.Service;

@Service
public class LocalizedReferenceDataPermissionEvaluatorImpl extends AbstractGenericPermissionEvaluator<LocalizedReferenceData<?>>
		implements ILocalizedReferenceDataPermissionEvaluator {
	
	@Override
	public boolean hasPermission(User user, LocalizedReferenceData<?> referenceData, Permission permission) {
		if (is(permission, CorePermissionConstants.READ)) {
			return true;
		} else if (is(permission, CorePermissionConstants.WRITE)) {
			return referenceData.isEditable();
		} else {
			return is(permission, CorePermissionConstants.CREATE);
		}
	}
}

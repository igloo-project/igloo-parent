package org.iglooproject.basicapp.core.security.service;

import org.iglooproject.basicapp.core.business.referencedata.model.ReferenceData;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.jpa.security.model.CorePermissionConstants;
import org.springframework.security.acls.model.Permission;
import org.springframework.stereotype.Service;

@Service
public class ReferenceDataPermissionEvaluatorImpl extends AbstractGenericPermissionEvaluator<ReferenceData<?>>
		implements IReferenceDataPermissionEvaluator {

	@Override
	public boolean hasPermission(User user, ReferenceData<?> referenceData, Permission permission) {
		if (is(permission, CorePermissionConstants.READ)) {
			return true;
		} else if (is(permission, CorePermissionConstants.WRITE)) {
			return referenceData.isEditable();
		} else {
			return is(permission, CorePermissionConstants.CREATE);
		}
	}

}

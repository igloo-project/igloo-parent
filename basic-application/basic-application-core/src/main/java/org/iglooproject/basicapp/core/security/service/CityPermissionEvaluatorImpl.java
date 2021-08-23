package org.iglooproject.basicapp.core.security.service;

import static org.iglooproject.jpa.security.model.CorePermissionConstants.READ;

import org.iglooproject.basicapp.core.business.referencedata.model.City;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.springframework.security.acls.model.Permission;
import org.springframework.stereotype.Service;

@Service
public class CityPermissionEvaluatorImpl extends AbstractGenericPermissionEvaluator<City> implements ICityPermissionEvaluator {

	@Override
	public boolean hasPermission(User user, City city, Permission permission) {
		if (is(permission, READ)) {
			return true;
		}
		
		return false;
	}

}

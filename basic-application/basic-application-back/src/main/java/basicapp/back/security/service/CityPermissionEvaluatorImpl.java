package basicapp.back.security.service;

import static org.iglooproject.jpa.security.model.CorePermissionConstants.READ;

import org.springframework.security.acls.model.Permission;
import org.springframework.stereotype.Service;

import basicapp.back.business.referencedata.model.City;
import basicapp.back.business.user.model.User;

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

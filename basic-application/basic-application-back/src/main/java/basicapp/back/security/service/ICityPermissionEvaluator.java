package basicapp.back.security.service;

import org.iglooproject.jpa.security.service.IGenericPermissionEvaluator;

import basicapp.back.business.referencedata.model.City;
import basicapp.back.business.user.model.User;

public interface ICityPermissionEvaluator extends IGenericPermissionEvaluator<User, City> {

}

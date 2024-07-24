package basicapp.back.security.service;

import basicapp.back.business.referencedata.model.City;
import basicapp.back.business.user.model.User;
import org.iglooproject.jpa.security.service.IGenericPermissionEvaluator;

public interface ICityPermissionEvaluator extends IGenericPermissionEvaluator<User, City> {}

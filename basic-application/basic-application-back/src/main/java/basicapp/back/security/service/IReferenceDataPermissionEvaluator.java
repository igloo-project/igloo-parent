package basicapp.back.security.service;

import org.iglooproject.jpa.security.service.IGenericPermissionEvaluator;

import basicapp.back.business.referencedata.model.ReferenceData;
import basicapp.back.business.user.model.User;

public interface IReferenceDataPermissionEvaluator extends IGenericPermissionEvaluator<User, ReferenceData<?>> {

}

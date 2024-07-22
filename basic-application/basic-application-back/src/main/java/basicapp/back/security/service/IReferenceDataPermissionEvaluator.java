package basicapp.back.security.service;

import basicapp.back.business.referencedata.model.ReferenceData;
import basicapp.back.business.user.model.User;
import org.iglooproject.jpa.security.service.IGenericPermissionEvaluator;

public interface IReferenceDataPermissionEvaluator
    extends IGenericPermissionEvaluator<User, ReferenceData<?>> {}

package org.iglooproject.basicapp.core.security.service;

import org.iglooproject.basicapp.core.business.referencedata.model.LocalizedReferenceData;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.jpa.security.service.IGenericPermissionEvaluator;

public interface ILocalizedReferenceDataPermissionEvaluator extends IGenericPermissionEvaluator<User, LocalizedReferenceData<?>> {

}
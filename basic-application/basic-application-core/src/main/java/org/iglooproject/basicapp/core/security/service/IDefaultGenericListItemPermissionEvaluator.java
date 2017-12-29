package org.iglooproject.basicapp.core.security.service;

import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.jpa.more.business.generic.model.GenericListItem;
import org.iglooproject.jpa.security.service.IGenericPermissionEvaluator;

public interface IDefaultGenericListItemPermissionEvaluator extends IGenericPermissionEvaluator<User, GenericListItem<?>> {

}
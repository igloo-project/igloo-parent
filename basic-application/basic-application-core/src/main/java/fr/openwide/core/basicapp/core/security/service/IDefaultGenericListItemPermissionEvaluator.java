package fr.openwide.core.basicapp.core.security.service;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.jpa.more.business.generic.model.GenericListItem;
import fr.openwide.core.jpa.security.service.IGenericPermissionEvaluator;

public interface IDefaultGenericListItemPermissionEvaluator extends IGenericPermissionEvaluator<User, GenericListItem<?>> {

}
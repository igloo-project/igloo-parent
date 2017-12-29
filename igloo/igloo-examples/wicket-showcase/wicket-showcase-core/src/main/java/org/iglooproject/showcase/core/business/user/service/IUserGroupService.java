package org.iglooproject.showcase.core.business.user.service;

import org.iglooproject.jpa.security.business.person.service.IGenericUserGroupService;
import org.iglooproject.showcase.core.business.user.model.User;
import org.iglooproject.showcase.core.business.user.model.UserGroup;

public interface IUserGroupService extends IGenericUserGroupService<UserGroup, User> {

}

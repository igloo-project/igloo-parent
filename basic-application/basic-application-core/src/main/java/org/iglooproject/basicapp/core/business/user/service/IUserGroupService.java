package org.iglooproject.basicapp.core.business.user.service;

import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.business.user.model.UserGroup;
import org.iglooproject.jpa.security.business.user.service.IGenericUserGroupService;

public interface IUserGroupService extends IGenericUserGroupService<UserGroup, User> {}

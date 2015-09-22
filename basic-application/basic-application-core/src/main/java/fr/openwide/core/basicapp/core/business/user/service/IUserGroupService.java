package fr.openwide.core.basicapp.core.business.user.service;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.business.user.model.UserGroup;
import fr.openwide.core.jpa.security.business.person.service.IGenericUserGroupService;

public interface IUserGroupService extends IGenericUserGroupService<UserGroup, User> {
}

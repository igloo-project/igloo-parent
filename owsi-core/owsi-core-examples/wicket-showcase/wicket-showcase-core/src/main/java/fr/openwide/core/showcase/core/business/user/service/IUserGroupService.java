package fr.openwide.core.showcase.core.business.user.service;

import fr.openwide.core.jpa.security.business.person.service.IPersonGroupService;
import fr.openwide.core.showcase.core.business.user.model.User;
import fr.openwide.core.showcase.core.business.user.model.UserGroup;

public interface IUserGroupService extends IPersonGroupService<UserGroup, User> {

}

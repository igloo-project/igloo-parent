package fr.openwide.core.basicapp.core.business.user.service;

import java.util.List;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.jpa.security.business.person.service.IPersonService;

public interface IUserService extends IPersonService<User> {

	List<User> listByUserName(String userName);
}
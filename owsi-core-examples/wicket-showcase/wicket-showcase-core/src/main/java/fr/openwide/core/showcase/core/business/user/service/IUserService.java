package fr.openwide.core.showcase.core.business.user.service;

import java.util.List;

import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.security.business.person.service.IPersonService;
import fr.openwide.core.showcase.core.business.user.model.User;

public interface IUserService extends IPersonService<User> {

	List<User> searchAutocomplete(String searchPattern, Integer limit) throws ServiceException;

}

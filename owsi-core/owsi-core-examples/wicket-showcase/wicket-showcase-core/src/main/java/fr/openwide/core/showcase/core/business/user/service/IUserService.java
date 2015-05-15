package fr.openwide.core.showcase.core.business.user.service;

import java.util.List;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.security.business.person.service.IGenericUserService;
import fr.openwide.core.showcase.core.business.user.model.User;

public interface IUserService extends IGenericUserService<User> {

	@Override
	List<User> searchAutocomplete(String searchPattern) throws ServiceException;

	void updateUserPosition(List<User> userList) throws ServiceException, SecurityServiceException;
}

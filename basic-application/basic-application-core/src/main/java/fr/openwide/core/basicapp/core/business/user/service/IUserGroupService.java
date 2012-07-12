package fr.openwide.core.basicapp.core.business.user.service;

import java.util.List;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.business.user.model.UserGroup;
import fr.openwide.core.jpa.business.generic.service.IGenericEntityService;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;

public interface IUserGroupService extends IGenericEntityService<Long, UserGroup> {

	void addPerson(UserGroup group, User user) throws ServiceException, SecurityServiceException;

	List<UserGroup> searchAutocomplete(String searchPattern) throws ServiceException, SecurityServiceException;
}

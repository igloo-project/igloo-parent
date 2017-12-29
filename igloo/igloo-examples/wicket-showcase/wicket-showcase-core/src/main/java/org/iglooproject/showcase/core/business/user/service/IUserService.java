package org.iglooproject.showcase.core.business.user.service;

import java.util.List;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.security.business.person.service.IGenericUserService;
import org.iglooproject.showcase.core.business.user.model.User;

public interface IUserService extends IGenericUserService<User> {

	void updateUserPosition(List<User> userList) throws ServiceException, SecurityServiceException;
}

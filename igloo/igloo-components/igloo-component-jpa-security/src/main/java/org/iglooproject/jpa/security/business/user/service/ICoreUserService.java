package org.iglooproject.jpa.security.business.user.service;

import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.business.generic.service.IGenericEntityService;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.security.business.user.model.IUser;

public interface ICoreUserService<U extends GenericEntity<Long, U> & IUser>
    extends IGenericEntityService<Long, U>, ICoreUserSecurityService<U> {

  void setPasswords(U user, String rawPassword) throws ServiceException, SecurityServiceException;
}

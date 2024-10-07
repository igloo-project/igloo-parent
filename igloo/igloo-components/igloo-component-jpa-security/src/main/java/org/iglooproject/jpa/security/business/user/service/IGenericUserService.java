package org.iglooproject.jpa.security.business.user.service;

import java.util.Locale;
import org.iglooproject.jpa.business.generic.service.IGenericEntityService;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.security.business.user.model.GenericUser;

public interface IGenericUserService<U extends GenericUser<U>>
    extends IGenericEntityService<Long, U>, ISecurityUserService<U> {

  void setEnabled(U user, boolean enabled) throws ServiceException, SecurityServiceException;

  void setPasswords(U user, String clearTextPassword)
      throws ServiceException, SecurityServiceException;

  void updateLastLoginDate(U user) throws ServiceException, SecurityServiceException;

  void updateLocale(U user, Locale locale) throws ServiceException, SecurityServiceException;
}

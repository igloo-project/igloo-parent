package org.iglooproject.jpa.security.business.user.service;

import org.iglooproject.jpa.business.generic.service.IGenericEntityService;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.security.business.user.model.GenericUser;
import org.iglooproject.jpa.security.business.user.model.GenericUserGroup;

public interface IGenericUserGroupService<
        G extends GenericUserGroup<G, U>, U extends GenericUser<U, G>>
    extends IGenericEntityService<Long, G> {

  G getByName(String name);

  void addUser(G group, U user) throws ServiceException, SecurityServiceException;

  void removeUser(G group, U user) throws ServiceException, SecurityServiceException;
}

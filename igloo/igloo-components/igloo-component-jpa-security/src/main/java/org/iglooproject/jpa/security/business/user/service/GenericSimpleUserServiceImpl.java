package org.iglooproject.jpa.security.business.user.service;

import org.iglooproject.jpa.security.business.user.dao.IGenericUserDao;
import org.iglooproject.jpa.security.business.user.model.GenericSimpleUser;

public abstract class GenericSimpleUserServiceImpl<U extends GenericSimpleUser<U>>
    extends GenericUserServiceImpl<U> {

  public GenericSimpleUserServiceImpl(IGenericUserDao<U> dao) {
    super(dao);
  }
}

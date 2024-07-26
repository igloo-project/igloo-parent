package org.iglooproject.jpa.security.business.user.dao;

import org.iglooproject.jpa.business.generic.dao.IGenericEntityDao;
import org.iglooproject.jpa.security.business.user.model.GenericUser;

public interface IGenericUserDao<U extends GenericUser<?, ?>> extends IGenericEntityDao<Long, U> {

  U getByUsernameCaseInsensitive(String username);
}

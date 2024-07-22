package org.iglooproject.jpa.security.business.authority.dao;

import org.iglooproject.jpa.business.generic.dao.IGenericEntityDao;
import org.iglooproject.jpa.security.business.authority.model.Authority;

public interface IAuthorityDao extends IGenericEntityDao<Long, Authority> {

  Authority getByName(String name);
}

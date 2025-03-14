package org.iglooproject.test.jpa.security.business.person.dao;

import org.iglooproject.jpa.business.generic.dao.IGenericEntityDao;
import org.iglooproject.test.jpa.security.business.person.model.MockUser;

public interface IMockUserDao extends IGenericEntityDao<Long, MockUser> {

  MockUser getByUsernameCaseInsensitive(String username);

  Long countEnabled();
}

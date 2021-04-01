package org.iglooproject.test.jpa.security.business.person.dao;

import org.iglooproject.jpa.security.business.person.dao.IGenericUserDao;
import org.iglooproject.test.jpa.security.business.person.model.MockUser;

public interface IMockUserDao extends IGenericUserDao<MockUser> {

	Long countEnabled();

}

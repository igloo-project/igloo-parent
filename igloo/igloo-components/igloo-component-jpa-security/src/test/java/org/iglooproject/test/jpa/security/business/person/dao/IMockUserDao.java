package org.iglooproject.test.jpa.security.business.person.dao;

import org.iglooproject.jpa.security.business.user.dao.IGenericUserDao;
import org.iglooproject.test.jpa.security.business.person.model.MockUser;
import org.iglooproject.test.jpa.security.business.person.model.MockUserGroup;

public interface IMockUserDao extends IGenericUserDao<MockUser, MockUserGroup> {

	Long countEnabled();

}

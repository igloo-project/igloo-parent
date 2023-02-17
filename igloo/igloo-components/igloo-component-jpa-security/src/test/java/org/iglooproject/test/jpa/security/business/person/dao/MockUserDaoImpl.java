package org.iglooproject.test.jpa.security.business.person.dao;

import org.iglooproject.jpa.security.business.user.dao.GenericUserDaoImpl;
import org.iglooproject.test.jpa.security.business.person.model.MockUser;
import org.iglooproject.test.jpa.security.business.person.model.MockUserGroup;
import org.iglooproject.test.jpa.security.business.person.model.QMockUser;
import org.springframework.stereotype.Repository;

@Repository
public class MockUserDaoImpl extends GenericUserDaoImpl<MockUser, MockUserGroup> implements IMockUserDao {

	private static final QMockUser qMockUser = QMockUser.mockUser;

	@Override
	public Long countEnabled() {
		return countByField(qMockUser, qMockUser.enabled, true);
	}

}

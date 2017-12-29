package org.iglooproject.test.jpa.security.business.person.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.iglooproject.jpa.security.business.person.service.GenericUserServiceImpl;
import org.iglooproject.test.jpa.security.business.person.dao.IMockUserDao;
import org.iglooproject.test.jpa.security.business.person.model.MockUser;

@Service("mockPersonService")
public class MockUserServiceImpl extends GenericUserServiceImpl<MockUser> implements IMockUserService {

	@Autowired
	public MockUserServiceImpl(IMockUserDao testPersonDao) {
		super(testPersonDao);
	}

	@Override
	public void protectedMethodRoleAdmin() {
	}

	@Override
	public void protectedMethodRoleAuthenticated() {
	}
}

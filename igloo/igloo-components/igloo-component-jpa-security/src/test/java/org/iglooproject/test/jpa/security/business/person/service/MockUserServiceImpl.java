package org.iglooproject.test.jpa.security.business.person.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.iglooproject.jpa.security.business.user.service.GenericUserServiceImpl;
import org.iglooproject.test.jpa.security.business.person.dao.IMockUserDao;
import org.iglooproject.test.jpa.security.business.person.model.MockUser;

@Service
public class MockUserServiceImpl extends GenericUserServiceImpl<MockUser> implements IMockUserService {

	private IMockUserDao dao;

	@Autowired
	public MockUserServiceImpl(IMockUserDao dao) {
		super(dao);
		this.dao = dao;
	}

	@Override
	public void protectedMethodRoleAdmin() {
	}

	@Override
	public void protectedMethodRoleAuthenticated() {
	}

	@Override
	public Long countEnabled() {
		return dao.countEnabled();
	}

}

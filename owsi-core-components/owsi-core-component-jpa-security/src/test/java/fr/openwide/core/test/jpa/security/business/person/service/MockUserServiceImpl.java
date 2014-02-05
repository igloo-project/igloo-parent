package fr.openwide.core.test.jpa.security.business.person.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.core.jpa.security.business.person.service.GenericUserServiceImpl;
import fr.openwide.core.test.jpa.security.business.person.dao.IMockPersonDao;
import fr.openwide.core.test.jpa.security.business.person.model.MockUser;

@Service("mockPersonService")
public class MockUserServiceImpl extends GenericUserServiceImpl<MockUser> implements IMockUserService {

	@Autowired
	public MockUserServiceImpl(IMockPersonDao testPersonDao) {
		super(testPersonDao);
	}

	@Override
	public void protectedMethodRoleAdmin() {
	}

	@Override
	public void protectedMethodRoleAuthenticated() {
	}
}

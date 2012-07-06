package fr.openwide.core.test.jpa.security.business.person.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.core.jpa.security.business.person.service.AbstractPersonServiceImpl;
import fr.openwide.core.test.jpa.security.business.person.dao.IMockPersonDao;
import fr.openwide.core.test.jpa.security.business.person.model.MockPerson;

@Service("mockPersonService")
public class MockPersonServiceImpl extends AbstractPersonServiceImpl<MockPerson> implements IMockPersonService {

	@Autowired
	public MockPersonServiceImpl(IMockPersonDao testPersonDao) {
		super(testPersonDao);
	}

	@Override
	public void protectedMethodRoleAdmin() {
	}

	@Override
	public void protectedMethodRoleAuthenticated() {
	}
}

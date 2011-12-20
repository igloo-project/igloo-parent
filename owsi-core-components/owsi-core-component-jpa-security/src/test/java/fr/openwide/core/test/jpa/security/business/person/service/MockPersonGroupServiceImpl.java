package fr.openwide.core.test.jpa.security.business.person.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.core.jpa.security.business.person.service.AbstractPersonGroupServiceImpl;
import fr.openwide.core.test.jpa.security.business.person.dao.IMockPersonGroupDao;
import fr.openwide.core.test.jpa.security.business.person.model.MockPerson;
import fr.openwide.core.test.jpa.security.business.person.model.MockPersonGroup;

@Service("mockPersonGroupService")
public class MockPersonGroupServiceImpl extends AbstractPersonGroupServiceImpl<MockPersonGroup, MockPerson> implements IMockPersonGroupService {

	@Autowired
	public MockPersonGroupServiceImpl(IMockPersonGroupDao testPersonGroupDao) {
		super(testPersonGroupDao);
	}
}

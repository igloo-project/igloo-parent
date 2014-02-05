package fr.openwide.core.test.jpa.security.business.person.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.core.jpa.security.business.person.service.GenericUserGroupServiceImpl;
import fr.openwide.core.test.jpa.security.business.person.dao.IMockPersonGroupDao;
import fr.openwide.core.test.jpa.security.business.person.model.MockUser;
import fr.openwide.core.test.jpa.security.business.person.model.MockUserGroup;

@Service("mockPersonGroupService")
public class MockUserGroupServiceImpl extends GenericUserGroupServiceImpl<MockUserGroup, MockUser> implements IMockUserGroupService {

	@Autowired
	public MockUserGroupServiceImpl(IMockPersonGroupDao testPersonGroupDao) {
		super(testPersonGroupDao);
	}
}

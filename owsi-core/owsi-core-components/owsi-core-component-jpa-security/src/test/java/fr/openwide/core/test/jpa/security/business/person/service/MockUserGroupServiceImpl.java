package fr.openwide.core.test.jpa.security.business.person.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.security.business.person.service.GenericUserGroupServiceImpl;
import fr.openwide.core.test.jpa.security.business.person.dao.IMockUserGroupDao;
import fr.openwide.core.test.jpa.security.business.person.model.MockUser;
import fr.openwide.core.test.jpa.security.business.person.model.MockUserGroup;

@Service("mockPersonGroupService")
public class MockUserGroupServiceImpl extends GenericUserGroupServiceImpl<MockUserGroup, MockUser> implements IMockUserGroupService {

	private final IMockUserGroupDao mockUserGroupDao;

	@Autowired
	public MockUserGroupServiceImpl(IMockUserGroupDao mockUserGroupDao) {
		super(mockUserGroupDao);
		this.mockUserGroupDao = mockUserGroupDao;
	}
	
	@Override
	public List<MockUser> listUsersByUserGroup(MockUserGroup group) throws ServiceException, SecurityServiceException {
		return mockUserGroupDao.listUsersByUserGroup(group);
	}
}

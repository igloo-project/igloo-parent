package org.iglooproject.test.jpa.security.business.person.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.security.business.user.service.GenericUserGroupServiceImpl;
import org.iglooproject.test.jpa.security.business.person.dao.IMockUserGroupDao;
import org.iglooproject.test.jpa.security.business.person.model.MockUser;
import org.iglooproject.test.jpa.security.business.person.model.MockUserGroup;

@Service
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

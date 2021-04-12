package org.iglooproject.test.jpa.security.business.person.dao;

import java.util.List;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.security.business.user.dao.IGenericUserGroupDao;
import org.iglooproject.test.jpa.security.business.person.model.MockUser;
import org.iglooproject.test.jpa.security.business.person.model.MockUserGroup;

public interface IMockUserGroupDao extends IGenericUserGroupDao<MockUserGroup, MockUser> {

	List<MockUser> listUsersByUserGroup(MockUserGroup group) throws ServiceException, SecurityServiceException;

}

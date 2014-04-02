package fr.openwide.core.test.jpa.security.business.person.dao;

import java.util.List;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.security.business.person.dao.IGenericUserGroupDao;
import fr.openwide.core.test.jpa.security.business.person.model.MockUser;
import fr.openwide.core.test.jpa.security.business.person.model.MockUserGroup;

public interface IMockUserGroupDao extends IGenericUserGroupDao<MockUserGroup, MockUser> {

	List<MockUser> listUsersByUserGroup(MockUserGroup group) throws ServiceException, SecurityServiceException;

}

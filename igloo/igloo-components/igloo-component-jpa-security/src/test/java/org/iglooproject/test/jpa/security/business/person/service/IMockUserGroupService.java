package org.iglooproject.test.jpa.security.business.person.service;

import java.util.List;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.security.business.user.service.IGenericUserGroupService;
import org.iglooproject.test.jpa.security.business.person.model.MockUser;
import org.iglooproject.test.jpa.security.business.person.model.MockUserGroup;

public interface IMockUserGroupService extends IGenericUserGroupService<MockUserGroup, MockUser> {

  List<MockUser> listUsersByUserGroup(MockUserGroup group)
      throws ServiceException, SecurityServiceException;
}

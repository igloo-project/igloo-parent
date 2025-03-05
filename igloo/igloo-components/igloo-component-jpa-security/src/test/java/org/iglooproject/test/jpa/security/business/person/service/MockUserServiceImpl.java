package org.iglooproject.test.jpa.security.business.person.service;

import org.iglooproject.jpa.business.generic.service.GenericEntityServiceImpl;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.spring.util.StringUtils;
import org.iglooproject.test.jpa.security.business.person.dao.IMockUserDao;
import org.iglooproject.test.jpa.security.business.person.model.MockUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MockUserServiceImpl extends GenericEntityServiceImpl<Long, MockUser>
    implements IMockUserService {

  private IMockUserDao dao;

  @Autowired private PasswordEncoder passwordEncoder;

  @Autowired
  public MockUserServiceImpl(IMockUserDao dao) {
    super(dao);
    this.dao = dao;
  }

  @Override
  public void protectedMethodRoleAdmin() {}

  @Override
  public void protectedMethodRoleAuthenticated() {}

  @Override
  public void protectedMethodRoleAnonymous() {}

  @Override
  public void setPasswords(MockUser user, String rawPassword)
      throws ServiceException, SecurityServiceException {
    user.setPasswordHash(passwordEncoder.encode(rawPassword));
    super.update(user);
  }

  @Override
  public MockUser getByUsername(String username) {
    if (!StringUtils.hasText(username)) {
      return null;
    }
    return getByNaturalId(username);
  }

  @Override
  public MockUser getByUsernameCaseInsensitive(String username) {
    if (!StringUtils.hasText(username)) {
      return null;
    }
    return dao.getByUsernameCaseInsensitive(username);
  }

  @Override
  public Long countEnabled() {
    return dao.countEnabled();
  }
}

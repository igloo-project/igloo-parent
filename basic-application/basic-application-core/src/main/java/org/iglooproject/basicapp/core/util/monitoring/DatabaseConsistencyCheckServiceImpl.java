package org.iglooproject.basicapp.core.util.monitoring;

import org.iglooproject.basicapp.core.business.user.service.IUserService;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.util.IDatabaseConsistencyCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseConsistencyCheckServiceImpl implements IDatabaseConsistencyCheckService {

  @Autowired private IUserService userService;

  @Override
  public void checkDatabaseAccess() {
    userService.count();
  }

  @Override
  public void checkDatabaseConsistency() throws ServiceException, SecurityServiceException {
    // rien
  }

  @Override
  public void checkOnStartup(boolean allowCreateMissingElements)
      throws ServiceException, SecurityServiceException {
    // rien
  }
}

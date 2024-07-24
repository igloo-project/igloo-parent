package basicapp.back.security.service;

import basicapp.back.business.user.model.User;
import basicapp.back.business.user.service.IUserService;
import org.iglooproject.jpa.security.service.CoreAuthenticationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

public class BasicApplicationAuthenticationServiceImpl extends CoreAuthenticationServiceImpl
    implements IBasicApplicationAuthenticationService {

  @Autowired private IUserService userService;

  @Override
  public User getUser() {
    return userService.getAuthenticatedUser();
  }
}

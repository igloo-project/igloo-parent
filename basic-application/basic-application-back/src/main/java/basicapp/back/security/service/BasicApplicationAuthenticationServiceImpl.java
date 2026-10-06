package basicapp.back.security.service;

import basicapp.back.business.user.model.User;
import basicapp.back.business.user.service.business.IUserService;
import org.iglooproject.jpa.security.service.CoreAuthenticationServiceImpl;

public class BasicApplicationAuthenticationServiceImpl extends CoreAuthenticationServiceImpl
    implements IBasicApplicationAuthenticationService {

  private final IUserService userService;

  public BasicApplicationAuthenticationServiceImpl(IUserService userService) {
    this.userService = userService;
  }

  @Override
  public User getUser() {
    return userService.getAuthenticatedUser();
  }
}

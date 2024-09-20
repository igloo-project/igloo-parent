package basicapp.back.business.user.service.controller;

import basicapp.back.business.user.model.User;
import basicapp.back.business.user.service.business.IUserService;
import jakarta.annotation.Nonnull;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserControllerServiceImpl implements IUserControllerService {

  private final IUserService userService;

  @Autowired
  public UserControllerServiceImpl(IUserService userService) {
    this.userService = userService;
  }

  public void affectRoles(@Nonnull User user) throws SecurityServiceException, ServiceException {
    userService.affectRoles(user);
  }

  @Override
  public void saveBasicUser(@Nonnull User user, String password)
      throws SecurityServiceException, ServiceException {
    userService.saveBasicUser(user, password);
  }

  @Override
  public void saveTechnicalUser(@Nonnull User user, String password)
      throws SecurityServiceException, ServiceException {
    userService.saveTechnicalUser(user, password);
  }

  @Override
  public void onSignIn(User user) throws ServiceException, SecurityServiceException {
    userService.onSignIn(user);
  }

  @Override
  public void onSignInFail(User user) throws ServiceException, SecurityServiceException {
    userService.onSignInFail(user);
  }

  @Override
  public void enable(User user) throws SecurityServiceException, ServiceException {
    userService.enable(user);
  }

  @Override
  public void openAnnouncement(User user) throws ServiceException, SecurityServiceException {
    userService.openAnnouncement(user);
  }

  @Override
  public void closeAnnouncement(User user) throws ServiceException, SecurityServiceException {
    userService.closeAnnouncement(user);
  }

  @Override
  public User getByEmailCaseInsensitive(String value) {
    return userService.getByEmailCaseInsensitive(value);
  }

  @Override
  public User getByUsername(String value) {
    return userService.getByUsername(value);
  }

  @Override
  public void disable(User user) throws SecurityServiceException, ServiceException {
    userService.disable(user);
  }

  @Override
  public void initPasswordRecoveryRequest(String email)
      throws SecurityServiceException, ServiceException {
    userService.initPasswordRecoveryRequest(email);
  }
}

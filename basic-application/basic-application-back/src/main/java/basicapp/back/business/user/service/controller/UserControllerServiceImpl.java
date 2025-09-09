package basicapp.back.business.user.service.controller;

import basicapp.back.business.common.model.EmailAddress;
import basicapp.back.business.notification.service.exception.NotificationException;
import basicapp.back.business.user.model.User;
import basicapp.back.business.user.service.business.IUserService;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserControllerServiceImpl implements IUserControllerService {

  private final IUserService userService;

  @Autowired
  public UserControllerServiceImpl(IUserService userService) {
    this.userService = userService;
  }

  @Override
  public void saveBasicUser(User user, String password)
      throws SecurityServiceException, NotificationException {
    userService.saveBasicUser(user, password);
  }

  @Override
  public void saveTechnicalUser(User user, String password)
      throws SecurityServiceException, NotificationException {
    userService.saveTechnicalUser(user, password);
  }

  @Override
  public void updateRoles(User user) {
    userService.saveUser(user);
  }

  @Override
  public void enable(User user) {
    userService.enable(user);
  }

  @Override
  public void disable(User user) {
    userService.disable(user);
  }

  @Override
  public void openAnnouncement(User user) {
    userService.openAnnouncement(user);
  }

  @Override
  public void closeAnnouncement(User user) {
    userService.closeAnnouncement(user);
  }

  @Override
  public void initPasswordRecoveryRequest(EmailAddress emailAddress) throws NotificationException {
    userService.initPasswordRecoveryRequest(emailAddress);
  }

  @Override
  public void onSignIn(User user) {
    userService.onSignIn(user);
  }

  @Override
  public void onSignInFail(User user) {
    userService.onSignInFail(user);
  }

  @Override
  public User getByEmailAddressCaseInsensitive(EmailAddress emailAddress) {
    return userService.getByEmailAddressCaseInsensitive(emailAddress);
  }

  @Override
  public User getByUsername(String value) {
    return userService.getByUsername(value);
  }
}

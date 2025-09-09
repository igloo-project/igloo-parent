package basicapp.back.security.service;

import basicapp.back.business.notification.service.exception.NotificationException;
import basicapp.back.business.user.model.User;
import basicapp.back.business.user.model.atomic.UserPasswordRecoveryRequestInitiator;
import basicapp.back.business.user.model.atomic.UserPasswordRecoveryRequestType;
import basicapp.back.business.user.model.atomic.UserType;
import basicapp.back.security.model.SecurityOptions;
import org.iglooproject.jpa.exception.SecurityServiceException;

public interface ISecurityManagementService {

  void updatePassword(User user, String password, User author) throws SecurityServiceException;

  void initiatePasswordRecoveryRequest(
      User user,
      UserPasswordRecoveryRequestType type,
      UserPasswordRecoveryRequestInitiator initiator)
      throws NotificationException;

  void initiatePasswordRecoveryRequest(
      User user,
      UserPasswordRecoveryRequestType type,
      UserPasswordRecoveryRequestInitiator initiator,
      User author)
      throws NotificationException;

  boolean checkPassword(String password, User user);

  boolean isPasswordExpired(User user);

  boolean isPasswordRecoveryRequestExpired(User user);

  SecurityOptions getSecurityOptionsDefault();

  SecurityOptions getSecurityOptions(UserType userType);

  SecurityOptions getSecurityOptions(User user);
}

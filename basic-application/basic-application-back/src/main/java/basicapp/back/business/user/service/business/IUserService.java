package basicapp.back.business.user.service.business;

import basicapp.back.business.common.model.EmailAddress;
import basicapp.back.business.notification.service.exception.NotificationException;
import basicapp.back.business.user.model.User;
import java.util.Locale;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.security.business.user.service.ICoreUserSecurityService;

public interface IUserService extends ICoreUserSecurityService<User> {

  void saveBasicUser(User user, String password)
      throws SecurityServiceException, NotificationException;

  void saveTechnicalUser(User user, String password)
      throws SecurityServiceException, NotificationException;

  void saveUser(User user);

  void onSignIn(User user);

  void onSignInFail(User user);

  void setPasswords(User user, String rawPassword) throws SecurityServiceException;

  void initPasswordRecoveryRequest(EmailAddress emailAddress) throws NotificationException;

  void enable(User user);

  void disable(User user);

  void updateLastLoginDate(User user);

  void updateLocale(User user, Locale locale);

  void openAnnouncement(User user);

  void closeAnnouncement(User user);

  User getByEmailAddressCaseInsensitive(EmailAddress emailAddress);

  User getAuthenticatedUser();

  long count();
}

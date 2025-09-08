package basicapp.back.business.user.service.business;

import basicapp.back.business.common.model.EmailAddress;
import basicapp.back.business.user.model.User;
import java.util.Locale;
import org.iglooproject.jpa.business.generic.service.IGenericEntityService;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.security.business.user.service.ICoreUserSecurityService;

public interface IUserService extends ICoreUserSecurityService<User>, IGenericEntityService<Long, User> {

  void saveBasicUser(User user, String password) throws SecurityServiceException, ServiceException;

  void saveTechnicalUser(User user, String password)
      throws SecurityServiceException, ServiceException;

  void onSignIn(User user) throws ServiceException, SecurityServiceException;

  void onSignInFail(User user) throws ServiceException, SecurityServiceException;

  void setPasswords(User user, String rawPassword)
      throws ServiceException, SecurityServiceException;

  void initPasswordRecoveryRequest(EmailAddress emailAddress)
      throws SecurityServiceException, ServiceException;

  void enable(User user) throws ServiceException, SecurityServiceException;

  void disable(User user) throws ServiceException, SecurityServiceException;

  void updateLastLoginDate(User user) throws ServiceException, SecurityServiceException;

  void updateLocale(User user, Locale locale) throws ServiceException, SecurityServiceException;

  void updateRoles(User user) throws SecurityServiceException, ServiceException;

  void openAnnouncement(User user) throws ServiceException, SecurityServiceException;

  void closeAnnouncement(User user) throws ServiceException, SecurityServiceException;

  User getByEmailAddressCaseInsensitive(EmailAddress emailAddress);

  User getAuthenticatedUser();
}

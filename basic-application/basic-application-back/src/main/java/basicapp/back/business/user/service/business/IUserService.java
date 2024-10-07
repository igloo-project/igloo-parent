package basicapp.back.business.user.service.business;

import basicapp.back.business.user.model.User;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.security.business.user.service.IGenericUserService;

public interface IUserService extends IGenericUserService<User> {

  void saveBasicUser(User user, String password) throws SecurityServiceException, ServiceException;

  void saveTechnicalUser(User user, String password)
      throws SecurityServiceException, ServiceException;

  void updateRoles(User user) throws SecurityServiceException, ServiceException;

  void enable(User user) throws ServiceException, SecurityServiceException;

  void disable(User user) throws ServiceException, SecurityServiceException;

  void openAnnouncement(User user) throws ServiceException, SecurityServiceException;

  void closeAnnouncement(User user) throws ServiceException, SecurityServiceException;

  void initPasswordRecoveryRequest(String emailModel)
      throws SecurityServiceException, ServiceException;

  void onSignIn(User user) throws ServiceException, SecurityServiceException;

  void onSignInFail(User user) throws ServiceException, SecurityServiceException;

  User getByEmailCaseInsensitive(String email);

  User getAuthenticatedUser();
}

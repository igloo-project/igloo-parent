package basicapp.back.business.user.service.controller;

import basicapp.back.business.common.model.EmailAddress;
import basicapp.back.business.user.model.User;
import basicapp.back.business.user.service.business.IUserService;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.springframework.stereotype.Service;

@Service
public class UserControllerServiceImpl implements IUserControllerService {

  private final IUserService userService;

  public UserControllerServiceImpl(IUserService userService) {
    this.userService = userService;
  }

  @Override
  public void saveBasicUserAdmin(User user) throws SecurityServiceException, ServiceException {
    userService.saveBasicUser(user);
  }

  @Override
  public void saveTechnicalUserAdmin(User user) throws SecurityServiceException, ServiceException {
    userService.saveTechnicalUser(user);
  }

  @Override
  public void updateRolesAdmin(User user) throws SecurityServiceException, ServiceException {
    userService.updateRoles(user);
  }

  @Override
  public void enableAdmin(User user) throws SecurityServiceException, ServiceException {
    userService.enable(user);
  }

  @Override
  public void disableAdmin(User user) throws SecurityServiceException, ServiceException {
    userService.disable(user);
  }

  @Override
  public void openAnnouncementUser(User user) throws ServiceException, SecurityServiceException {
    userService.openAnnouncement(user);
  }

  @Override
  public void closeAnnouncementUser(User user) throws ServiceException, SecurityServiceException {
    userService.closeAnnouncement(user);
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

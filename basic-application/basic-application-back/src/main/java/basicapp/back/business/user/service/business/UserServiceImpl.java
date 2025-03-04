package basicapp.back.business.user.service.business;

import basicapp.back.business.history.model.atomic.HistoryEventType;
import basicapp.back.business.history.model.bean.HistoryLogAdditionalInformationBean;
import basicapp.back.business.history.service.IHistoryLogService;
import basicapp.back.business.user.dao.IUserDao;
import basicapp.back.business.user.model.User;
import basicapp.back.business.user.model.atomic.UserPasswordRecoveryRequestInitiator;
import basicapp.back.business.user.model.atomic.UserPasswordRecoveryRequestType;
import basicapp.back.business.user.model.atomic.UserType;
import basicapp.back.security.service.IBasicApplicationAuthenticationService;
import basicapp.back.security.service.ISecurityManagementService;
import com.google.common.base.Preconditions;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.security.business.user.service.GenericSimpleUserServiceImpl;
import org.iglooproject.jpa.util.HibernateUtils;
import org.iglooproject.spring.property.SpringPropertyIds;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.spring.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

@Service("userService")
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public class UserServiceImpl extends GenericSimpleUserServiceImpl<User> implements IUserService {

  private final IUserDao userDao;
  private final IBasicApplicationAuthenticationService authenticationService;
  private final IHistoryLogService historyLogService;
  private final IPropertyService propertyService;
  private final ISecurityManagementService securityManagementService;

  @Autowired
  public UserServiceImpl(
      IUserDao userDao,
      IBasicApplicationAuthenticationService authenticationService,
      IHistoryLogService historyLogService,
      IPropertyService propertyService,
      @Lazy ISecurityManagementService securityManagementService) {
    super(userDao);
    this.userDao = userDao;
    this.authenticationService = authenticationService;
    this.historyLogService = historyLogService;
    this.propertyService = propertyService;
    this.securityManagementService = securityManagementService;
  }

  @Override
  public void saveBasicUser(User user, String password)
      throws SecurityServiceException, ServiceException {
    user.setType(UserType.BASIC);
    User author = getAuthenticatedUser();
    saveUser(user, author, password);
  }

  @Override
  public void saveTechnicalUser(User user, String password)
      throws SecurityServiceException, ServiceException {
    user.setType(UserType.TECHNICAL);
    User author = getAuthenticatedUser();
    saveUser(user, author, password);
  }

  private void saveUser(User user, User author, String password)
      throws SecurityServiceException, ServiceException {
    Objects.requireNonNull(user);

    if (user.getLocale() == null) {
      user.setLocale(propertyService.get(SpringPropertyIds.DEFAULT_LOCALE));
    }

    if (user.isNew()) {
      create(user);
      historyLogService.log(
          HistoryEventType.CREATE, user, HistoryLogAdditionalInformationBean.empty());
      if (StringUtils.hasText(password)) {
        securityManagementService.updatePassword(user, password, author);
      } else {
        securityManagementService.initiatePasswordRecoveryRequest(
            user,
            UserPasswordRecoveryRequestType.CREATION,
            UserPasswordRecoveryRequestInitiator.ADMIN,
            author);
      }
    } else {
      update(user);
    }
  }

  @Override
  public void updateRoles(User user) throws SecurityServiceException, ServiceException {
    Objects.requireNonNull(user);
    update(user);
  }

  @Override
  public void enable(User user) throws ServiceException, SecurityServiceException {
    Objects.requireNonNull(user);
    Preconditions.checkArgument(!user.isEnabled());
    setEnabled(user, true);
    historyLogService.log(
        HistoryEventType.ENABLE, user, HistoryLogAdditionalInformationBean.empty());
  }

  @Override
  public void disable(User user) throws ServiceException, SecurityServiceException {
    Objects.requireNonNull(user);
    Preconditions.checkArgument(user.isEnabled());
    setEnabled(user, false);
    historyLogService.log(
        HistoryEventType.DISABLE, user, HistoryLogAdditionalInformationBean.empty());
  }

  @Override
  public void openAnnouncement(User user) throws ServiceException, SecurityServiceException {
    Objects.requireNonNull(user);
    user.getAnnouncementInformation().setLastActionDate(Instant.now());
    user.getAnnouncementInformation().setOpen(true);
    update(user);
  }

  @Override
  public void closeAnnouncement(User user) throws ServiceException, SecurityServiceException {
    Objects.requireNonNull(user);
    user.getAnnouncementInformation().setLastActionDate(Instant.now());
    user.getAnnouncementInformation().setOpen(false);
    update(user);
  }

  @Override
  public void initPasswordRecoveryRequest(String email)
      throws SecurityServiceException, ServiceException {
    User user = getByEmailCaseInsensitive(email);

    if (user != null && user.isEnabled() && user.isNotificationEnabled()) {
      securityManagementService.initiatePasswordRecoveryRequest(
          user,
          user.hasPasswordHash()
              ? UserPasswordRecoveryRequestType.RESET
              : UserPasswordRecoveryRequestType.CREATION,
          UserPasswordRecoveryRequestInitiator.USER);
    }
  }

  @Override
  public void onSignIn(User user) throws ServiceException, SecurityServiceException {
    historyLogService.log(
        HistoryEventType.SIGN_IN, user, HistoryLogAdditionalInformationBean.empty());
  }

  @Override
  public void onSignInFail(User user) throws ServiceException, SecurityServiceException {
    historyLogService.log(
        HistoryEventType.SIGN_IN_FAIL, user, HistoryLogAdditionalInformationBean.empty());
  }

  @Override
  public User getByEmailCaseInsensitive(String email) {
    if (!StringUtils.hasText(StringUtils.lowerCase(email))) {
      return null;
    }
    return userDao.getByEmailCaseInsensitive(email);
  }

  @Override
  public User getAuthenticatedUser() {
    return Optional.ofNullable(authenticationService.getUsername())
        .map(username -> HibernateUtils.unwrap(getByUsername(username)))
        .orElse(null);
  }
}

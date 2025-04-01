package basicapp.back.business.user.service.business;

import basicapp.back.business.common.model.EmailAddress;
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
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import org.iglooproject.jpa.business.generic.service.GenericEntityServiceImpl;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.util.HibernateUtils;
import org.iglooproject.spring.property.SpringPropertyIds;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.spring.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl extends GenericEntityServiceImpl<Long, User> implements IUserService {

  private final IUserDao dao;
  private final IBasicApplicationAuthenticationService authenticationService;
  private final IHistoryLogService historyLogService;
  private final IPropertyService propertyService;
  private final ISecurityManagementService securityManagementService;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UserServiceImpl(
      IUserDao dao,
      @Lazy IBasicApplicationAuthenticationService authenticationService,
      @Lazy IHistoryLogService historyLogService,
      IPropertyService propertyService,
      @Lazy ISecurityManagementService securityManagementService,
      PasswordEncoder passwordEncoder) {
    super(dao);
    this.dao = dao;
    this.authenticationService = authenticationService;
    this.historyLogService = historyLogService;
    this.propertyService = propertyService;
    this.securityManagementService = securityManagementService;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  protected void createEntity(User user) throws ServiceException, SecurityServiceException {
    Instant now = Instant.now();
    user.setCreationDate(now);
    user.setLastUpdateDate(now);
    super.createEntity(user);
  }

  @Override
  protected void updateEntity(User user) throws ServiceException, SecurityServiceException {
    user.setLastUpdateDate(Instant.now());
    super.updateEntity(user);
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

  /**
   * Encode and set Password to user.
   *
   * <p>check that Password cannot be more than 72 bytes.
   *
   * @see <a href="https://spring.io/security/cve-2025-22228">CVE-2025-22228</a>
   * @see org.springframework.security.crypto.bcrypt.BCrypt#hashpw(byte[], String, boolean)
   */
  @Override
  public void setPasswords(User user, String rawPassword)
      throws ServiceException, SecurityServiceException {
    Preconditions.checkArgument(StringUtils.hasText(rawPassword));

    if (rawPassword.getBytes(StandardCharsets.UTF_8).length > 72) {
      throw new SecurityServiceException("password cannot be more than 72 bytes");
    }

    user.setPasswordHash(passwordEncoder.encode(rawPassword));
    update(user);
  }

  @Override
  public void initPasswordRecoveryRequest(EmailAddress emailAddress)
      throws SecurityServiceException, ServiceException {
    User user = getByEmailAddressCaseInsensitive(emailAddress);

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
  public void updateLocale(User user, Locale locale)
      throws ServiceException, SecurityServiceException {
    user.setLocale(locale);
    updateEntity(user);
  }

  @Override
  public void enable(User user) throws ServiceException, SecurityServiceException {
    Objects.requireNonNull(user);
    Preconditions.checkArgument(!user.isEnabled());
    user.setEnabled(true);
    update(user);
    historyLogService.log(
        HistoryEventType.ENABLE, user, HistoryLogAdditionalInformationBean.empty());
  }

  @Override
  public void disable(User user) throws ServiceException, SecurityServiceException {
    Objects.requireNonNull(user);
    Preconditions.checkArgument(user.isEnabled());
    user.setEnabled(false);
    update(user);
    historyLogService.log(
        HistoryEventType.DISABLE, user, HistoryLogAdditionalInformationBean.empty());
  }

  @Override
  public void updateRoles(User user) throws SecurityServiceException, ServiceException {
    Objects.requireNonNull(user);
    update(user);
  }

  @Override
  public void updateLastLoginDate(User user) throws ServiceException, SecurityServiceException {
    user.setLastLoginDate(Instant.now());
    updateEntity(user);
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
  public User getByUsername(String username) {
    if (!StringUtils.hasText(username)) {
      return null;
    }
    return getByNaturalId(username);
  }

  @Override
  public User getByUsernameCaseInsensitive(String username) {
    if (!StringUtils.hasText(username)) {
      return null;
    }
    return dao.getByUsernameCaseInsensitive(username);
  }

  @Override
  public User getByEmailAddressCaseInsensitive(EmailAddress emailAddress) {
    if (emailAddress == null || !StringUtils.hasText(emailAddress.getValue())) {
      return null;
    }
    return dao.getByEmailCaseInsensitive(emailAddress);
  }

  @Override
  public User getAuthenticatedUser() {
    return Optional.ofNullable(authenticationService.getUsername())
        .map(username -> HibernateUtils.unwrap(getByUsername(username)))
        .orElse(null);
  }
}

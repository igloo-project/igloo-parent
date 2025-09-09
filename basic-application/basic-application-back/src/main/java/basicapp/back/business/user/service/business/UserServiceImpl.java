package basicapp.back.business.user.service.business;

import basicapp.back.business.common.model.EmailAddress;
import basicapp.back.business.history.model.atomic.HistoryEventType;
import basicapp.back.business.history.model.bean.HistoryLogAdditionalInformationBean;
import basicapp.back.business.history.service.IHistoryEventSummaryService;
import basicapp.back.business.history.service.IHistoryLogService;
import basicapp.back.business.notification.service.exception.NotificationException;
import basicapp.back.business.user.model.User;
import basicapp.back.business.user.model.atomic.UserPasswordRecoveryRequestInitiator;
import basicapp.back.business.user.model.atomic.UserPasswordRecoveryRequestType;
import basicapp.back.business.user.model.atomic.UserType;
import basicapp.back.business.user.repository.IUserRepository;
import basicapp.back.security.service.IBasicApplicationAuthenticationService;
import basicapp.back.security.service.ISecurityManagementService;
import com.google.common.base.Preconditions;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.util.HibernateUtils;
import org.iglooproject.spring.property.SpringPropertyIds;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.spring.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userService")
public class UserServiceImpl implements IUserService {

  private final IUserRepository userRepository;
  private final IBasicApplicationAuthenticationService authenticationService;
  private final IHistoryLogService historyLogService;
  private final IHistoryEventSummaryService historyEventSummaryService;
  private final IPropertyService propertyService;
  private final ISecurityManagementService securityManagementService;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UserServiceImpl(
      IUserRepository userRepository,
      @Lazy IBasicApplicationAuthenticationService authenticationService,
      @Lazy IHistoryLogService historyLogService,
      @Lazy IHistoryEventSummaryService historyEventSummaryService,
      IPropertyService propertyService,
      @Lazy ISecurityManagementService securityManagementService,
      PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.authenticationService = authenticationService;
    this.historyLogService = historyLogService;
    this.historyEventSummaryService = historyEventSummaryService;
    this.propertyService = propertyService;
    this.securityManagementService = securityManagementService;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  @Transactional(rollbackFor = {SecurityServiceException.class, NotificationException.class})
  public void saveBasicUser(User user, String password)
      throws SecurityServiceException, NotificationException {
    user.setType(UserType.BASIC);
    User author = getAuthenticatedUser();
    saveUser(user, author, password);
  }

  @Override
  @Transactional(rollbackFor = {SecurityServiceException.class, NotificationException.class})
  public void saveTechnicalUser(User user, String password)
      throws SecurityServiceException, NotificationException {
    user.setType(UserType.TECHNICAL);
    User author = getAuthenticatedUser();
    saveUser(user, author, password);
  }

  private void saveUser(User user, User author, String password)
      throws NotificationException, SecurityServiceException {
    Objects.requireNonNull(user);

    boolean isNew = user.isNew();
    saveUser(user);

    if (isNew) {
      if (StringUtils.hasText(password)) {
        securityManagementService.updatePassword(user, password, author);
      } else {
        securityManagementService.initiatePasswordRecoveryRequest(
            user,
            UserPasswordRecoveryRequestType.CREATION,
            UserPasswordRecoveryRequestInitiator.ADMIN,
            author);
      }
    }
  }

  @Transactional
  @Override
  public void saveUser(User user) {
    Objects.requireNonNull(user);

    if (user.getLocale() == null) {
      user.setLocale(propertyService.get(SpringPropertyIds.DEFAULT_LOCALE));
    }

    if (user.isNew()) {
      historyEventSummaryService.refresh(user.getCreation());
      historyLogService.log(
          HistoryEventType.CREATE, user, HistoryLogAdditionalInformationBean.empty());
    }

    historyEventSummaryService.refresh(user.getModification());
    userRepository.save(user);
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
  @Transactional
  public void onSignIn(User user) {
    historyLogService.log(
        HistoryEventType.SIGN_IN, user, HistoryLogAdditionalInformationBean.empty());
  }

  @Override
  @Transactional
  public void onSignInFail(User user) {
    historyLogService.log(
        HistoryEventType.SIGN_IN_FAIL, user, HistoryLogAdditionalInformationBean.empty());
  }

  @Override
  @Transactional(rollbackFor = SecurityServiceException.class)
  public void setPasswords(User user, String rawPassword) throws SecurityServiceException {
    Preconditions.checkArgument(StringUtils.hasText(rawPassword));

    if (rawPassword.getBytes(StandardCharsets.UTF_8).length > 72) {
      throw new SecurityServiceException("password cannot be more than 72 bytes");
    }

    user.setPasswordHash(passwordEncoder.encode(rawPassword));
    userRepository.save(user);
  }

  @Override
  @Transactional(rollbackFor = NotificationException.class)
  public void initPasswordRecoveryRequest(EmailAddress emailAddress) throws NotificationException {
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
  @Transactional
  public void enable(User user) {
    Objects.requireNonNull(user);
    Preconditions.checkArgument(!user.isEnabled());
    user.setEnabled(true);
    saveUser(user);
    historyLogService.log(
        HistoryEventType.ENABLE, user, HistoryLogAdditionalInformationBean.empty());
  }

  @Override
  @Transactional
  public void disable(User user) {
    Objects.requireNonNull(user);
    Preconditions.checkArgument(user.isEnabled());
    user.setEnabled(false);
    saveUser(user);
    historyLogService.log(
        HistoryEventType.DISABLE, user, HistoryLogAdditionalInformationBean.empty());
  }

  @Override
  @Transactional
  public void updateLastLoginDate(User user) {
    user.setLastLoginDate(Instant.now());
    saveUser(user);
  }

  @Override
  @Transactional
  public void updateLocale(User user, Locale locale) {
    user.setLocale(locale);
    saveUser(user);
  }

  @Override
  @Transactional
  public void openAnnouncement(User user) {
    Objects.requireNonNull(user);
    user.getAnnouncementInformation().setLastActionDate(Instant.now());
    user.getAnnouncementInformation().setOpen(true);
    saveUser(user);
  }

  @Override
  @Transactional
  public void closeAnnouncement(User user) {
    Objects.requireNonNull(user);
    user.getAnnouncementInformation().setLastActionDate(Instant.now());
    user.getAnnouncementInformation().setOpen(false);
    saveUser(user);
  }

  @Override
  @Transactional(readOnly = true)
  public User getByUsername(String username) {
    if (!StringUtils.hasText(username)) {
      return null;
    }
    return userRepository.findByNaturalId(username).orElse(null);
  }

  @Override
  @Transactional(readOnly = true)
  public User getByUsernameCaseInsensitive(String username) {
    if (!StringUtils.hasText(username)) {
      return null;
    }
    return userRepository.findByUsernameIgnoreCase(username).orElse(null);
  }

  @Override
  @Transactional(readOnly = true)
  public User getByEmailAddressCaseInsensitive(EmailAddress emailAddress) {
    if (emailAddress == null || !StringUtils.hasText(emailAddress.getValue())) {
      return null;
    }
    return userRepository.findByEmailAddressIgnoreCase(emailAddress).orElse(null);
  }

  @Override
  @Transactional(readOnly = true)
  public User getAuthenticatedUser() {
    return Optional.ofNullable(authenticationService.getUsername())
        .map(username -> HibernateUtils.unwrap(getByUsername(username)))
        .orElse(null);
  }

  @Override
  public long count() {
    return userRepository.count();
  }
}

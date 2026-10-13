package basicapp.back.security.service;

import static org.iglooproject.jpa.security.service.CoreJpaUserDetailsServiceImpl.EMPTY_PASSWORD_HASH;
import static org.iglooproject.spring.property.SpringSecurityPropertyIds.PASSWORD_EXPIRATION_DAYS;
import static org.iglooproject.spring.property.SpringSecurityPropertyIds.PASSWORD_HISTORY_COUNT;
import static org.iglooproject.spring.property.SpringSecurityPropertyIds.PASSWORD_RECOVERY_REQUEST_EXPIRATION_MINUTES;
import static org.iglooproject.spring.property.SpringSecurityPropertyIds.PASSWORD_RECOVERY_REQUEST_TOKEN_RANDOM_COUNT;

import basicapp.back.business.history.model.atomic.HistoryLogEventType;
import basicapp.back.business.history.model.bean.HistoryLogAdditionalInformationBean;
import basicapp.back.business.history.service.IHistoryLogService;
import basicapp.back.business.notification.service.INotificationService;
import basicapp.back.business.user.model.User;
import basicapp.back.business.user.model.atomic.UserPasswordRecoveryRequestInitiator;
import basicapp.back.business.user.model.atomic.UserPasswordRecoveryRequestType;
import basicapp.back.business.user.model.atomic.UserType;
import basicapp.back.business.user.service.business.IUserService;
import basicapp.back.security.model.SecurityOptions;
import com.google.common.base.Preconditions;
import com.google.common.collect.EvictingQueue;
import com.google.common.collect.ImmutableMap;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.RandomStringUtils;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.spring.util.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

public class SecurityManagementServiceImpl implements ISecurityManagementService {

  private final IUserService userService;
  private final INotificationService notificationService;
  private final IPropertyService propertyService;
  private final IHistoryLogService historyLogService;
  private final PasswordEncoder passwordEncoder;

  private final Map<UserType, SecurityOptions> securityOptionsUsers;

  public SecurityManagementServiceImpl(
      IUserService userService,
      INotificationService notificationService,
      IPropertyService propertyService,
      IHistoryLogService historyLogService,
      PasswordEncoder passwordEncoder,
      ImmutableMap<UserType, SecurityOptions> securityOptionsUsers) {
    this.userService = userService;
    this.notificationService = notificationService;
    this.propertyService = propertyService;
    this.historyLogService = historyLogService;
    this.passwordEncoder = passwordEncoder;
    this.securityOptionsUsers = ImmutableMap.copyOf(securityOptionsUsers);
  }

  @Override
  public void updatePassword(User user, String password)
      throws ServiceException, SecurityServiceException {
    if (user == null || !StringUtils.hasText(password)) {
      return;
    }

    setPassword(user, password);
    user.getPasswordInformation().setLastUpdateDate(Instant.now());

    if (getSecurityOptions(user).isPasswordHistoryEnabled()) {
      EvictingQueue<String> historyQueue =
          EvictingQueue.create(propertyService.get(PASSWORD_HISTORY_COUNT));

      for (String oldPassword : user.getPasswordInformation().getHistory()) {
        historyQueue.offer(oldPassword);
      }
      historyQueue.offer(user.getPasswordHash());

      user.getPasswordInformation().setHistory(List.copyOf(historyQueue));
    }

    user.getPasswordRecoveryRequest().reset();
    userService.update(user);

    historyLogService.log(
        HistoryLogEventType.PASSWORD_UPDATE, user, HistoryLogAdditionalInformationBean.empty());
  }

  @Override
  public void resetPassword(
      User user,
      UserPasswordRecoveryRequestType type,
      UserPasswordRecoveryRequestInitiator initiator)
      throws ServiceException, SecurityServiceException {
    user.getPasswordRecoveryRequest()
        .setToken(
            RandomStringUtils.secure()
                .nextAlphabetic(propertyService.get(PASSWORD_RECOVERY_REQUEST_TOKEN_RANDOM_COUNT)));
    user.getPasswordRecoveryRequest().setCreationDate(Instant.now());
    user.getPasswordRecoveryRequest().setType(type);
    user.getPasswordRecoveryRequest().setInitiator(initiator);

    userService.update(user);

    notificationService.sendUserPasswordRecoveryRequest(user);

    switch (type) {
      case CREATION:
        historyLogService.log(
            HistoryLogEventType.PASSWORD_CREATION_REQUEST,
            user,
            HistoryLogAdditionalInformationBean.empty());
        break;
      case RESET:
        historyLogService.log(
            HistoryLogEventType.PASSWORD_RESET_REQUEST,
            user,
            HistoryLogAdditionalInformationBean.empty());
        break;
      default:
        break;
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
  public void setPassword(User user, String password)
      throws ServiceException, SecurityServiceException {
    Objects.requireNonNull(user);
    Preconditions.checkArgument(StringUtils.hasText(password));

    if (password.getBytes(StandardCharsets.UTF_8).length > 72) {
      throw new SecurityServiceException("password cannot be more than 72 bytes");
    }

    user.setPasswordHash(passwordEncoder.encode(password));
    userService.update(user);
  }

  @Override
  public boolean hasPassword(User user) {
    Objects.requireNonNull(user);
    return !Objects.equals(user.getPasswordHash(), EMPTY_PASSWORD_HASH);
  }

  @Override
  public boolean checkPassword(String password, User user)
      throws ServiceException, SecurityServiceException {
    return passwordEncoder.matches(password, user.getPasswordHash());
  }

  @Override
  public boolean isPasswordExpired(User user) {
    if (user == null
        || user.getPasswordInformation().getLastUpdateDate() == null
        || !getSecurityOptions(user).isPasswordExpirationEnabled()) {
      return false;
    }

    Instant expirationDate =
        user.getPasswordInformation()
            .getLastUpdateDate()
            .plus(propertyService.get(PASSWORD_EXPIRATION_DAYS), ChronoUnit.DAYS);
    Instant now = Instant.now();

    return now.isAfter(expirationDate);
  }

  @Override
  public boolean isPasswordRecoveryRequestValid(User user, String token) {
    if (user == null
        || !StringUtils.hasText(token)
        || user.getPasswordRecoveryRequest().getToken() == null) {
      return false;
    }

    return MessageDigest.isEqual(
            user.getPasswordRecoveryRequest().getToken().getBytes(StandardCharsets.UTF_8),
            token.getBytes(StandardCharsets.UTF_8))
        && (Objects.equals(
                user.getPasswordRecoveryRequest().getType(),
                UserPasswordRecoveryRequestType.CREATION)
            || !isPasswordRecoveryRequestExpired(user));
  }

  @Override
  public boolean isPasswordRecoveryRequestExpired(User user) {
    if (user == null
        || user.getPasswordRecoveryRequest().getToken() == null
        || user.getPasswordRecoveryRequest().getCreationDate() == null) {
      return true;
    }

    Instant expirationDate =
        user.getPasswordRecoveryRequest()
            .getCreationDate()
            .plus(
                propertyService.get(PASSWORD_RECOVERY_REQUEST_EXPIRATION_MINUTES),
                ChronoUnit.MINUTES);
    Instant now = Instant.now();

    return now.isAfter(expirationDate);
  }

  @Override
  public void onSignIn(User user) throws ServiceException, SecurityServiceException {
    historyLogService.log(
        HistoryLogEventType.SIGN_IN, user, HistoryLogAdditionalInformationBean.empty());
  }

  @Override
  public void onSignInFail(String username) throws ServiceException, SecurityServiceException {
    User user = userService.getByUsername(username);
    if (user != null) {
      historyLogService.log(
          HistoryLogEventType.SIGN_IN_FAIL, user, HistoryLogAdditionalInformationBean.empty());
    }
  }

  @Override
  public SecurityOptions getSecurityOptions(UserType userType) {
    Preconditions.checkState(securityOptionsUsers.containsKey(userType));
    return securityOptionsUsers.get(userType);
  }

  @Override
  public SecurityOptions getSecurityOptions(User user) {
    Objects.requireNonNull(user);
    return getSecurityOptions(user.getType());
  }
}

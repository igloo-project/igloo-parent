package basicapp.back.security.service;

import static org.iglooproject.spring.property.SpringSecurityPropertyIds.PASSWORD_EXPIRATION_DAYS;
import static org.iglooproject.spring.property.SpringSecurityPropertyIds.PASSWORD_HISTORY_COUNT;
import static org.iglooproject.spring.property.SpringSecurityPropertyIds.PASSWORD_RECOVERY_REQUEST_EXPIRATION_MINUTES;
import static org.iglooproject.spring.property.SpringSecurityPropertyIds.PASSWORD_RECOVERY_REQUEST_TOKEN_RANDOM_COUNT;

import basicapp.back.business.history.model.atomic.HistoryEventType;
import basicapp.back.business.history.model.bean.HistoryLogAdditionalInformationBean;
import basicapp.back.business.history.service.IHistoryLogService;
import basicapp.back.business.notification.service.INotificationService;
import basicapp.back.business.notification.service.exception.NotificationException;
import basicapp.back.business.user.model.User;
import basicapp.back.business.user.model.atomic.UserPasswordRecoveryRequestInitiator;
import basicapp.back.business.user.model.atomic.UserPasswordRecoveryRequestType;
import basicapp.back.business.user.model.atomic.UserType;
import basicapp.back.business.user.service.business.IUserService;
import basicapp.back.security.model.SecurityOptions;
import com.google.common.collect.EvictingQueue;
import com.google.common.collect.ImmutableMap;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.RandomStringUtils;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.spring.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

public class SecurityManagementServiceImpl implements ISecurityManagementService {

  @Autowired private IUserService userService;

  @Autowired private INotificationService notificationService;

  @Autowired private IPropertyService propertyService;

  @Autowired private IHistoryLogService historyLogService;

  @Autowired private PasswordEncoder passwordEncoder;

  private final SecurityOptions securityOptionsDefault;

  private final Map<UserType, SecurityOptions> securityOptionsUsers;

  public SecurityManagementServiceImpl(
      SecurityOptions securityOptionsDefault,
      ImmutableMap<UserType, SecurityOptions> securityOptionsUsers) {
    this.securityOptionsDefault = securityOptionsDefault;
    this.securityOptionsUsers = ImmutableMap.copyOf(securityOptionsUsers);
  }

  @Override
  @Transactional(rollbackFor = SecurityServiceException.class)
  public void updatePassword(User user, String password, User author)
      throws SecurityServiceException {
    if (user == null || !StringUtils.hasText(password)) {
      return;
    }

    userService.setPasswords(user, password);
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
    userService.saveUser(user);

    historyLogService.log(
        HistoryEventType.PASSWORD_UPDATE, user, HistoryLogAdditionalInformationBean.empty());
  }

  @Override
  @Transactional(rollbackFor = NotificationException.class)
  public void initiatePasswordRecoveryRequest(
      User user,
      UserPasswordRecoveryRequestType type,
      UserPasswordRecoveryRequestInitiator initiator)
      throws NotificationException {
    initiatePasswordRecoveryRequest(user, type, initiator, user);
  }

  @Override
  @Transactional(rollbackFor = NotificationException.class)
  public void initiatePasswordRecoveryRequest(
      User user,
      UserPasswordRecoveryRequestType type,
      UserPasswordRecoveryRequestInitiator initiator,
      User author)
      throws NotificationException {
    user.getPasswordRecoveryRequest()
        .setToken(
            RandomStringUtils.secure()
                .nextAlphabetic(propertyService.get(PASSWORD_RECOVERY_REQUEST_TOKEN_RANDOM_COUNT)));
    user.getPasswordRecoveryRequest().setCreationDate(Instant.now());
    user.getPasswordRecoveryRequest().setType(type);
    user.getPasswordRecoveryRequest().setInitiator(initiator);

    userService.saveUser(user);

    notificationService.sendUserPasswordRecoveryRequest(user);

    switch (type) {
      case CREATION:
        historyLogService.log(
            HistoryEventType.PASSWORD_CREATION_REQUEST,
            user,
            HistoryLogAdditionalInformationBean.empty());
        break;
      case RESET:
        historyLogService.log(
            HistoryEventType.PASSWORD_RESET_REQUEST,
            user,
            HistoryLogAdditionalInformationBean.empty());
        break;
      default:
        break;
    }
  }

  @Override
  @Transactional(readOnly = true)
  public boolean checkPassword(String password, User user) {
    return passwordEncoder.matches(password, user.getPasswordHash());
  }

  @Override
  @Transactional(readOnly = true)
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
  @Transactional(readOnly = true)
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
  @Transactional(readOnly = true)
  public SecurityOptions getSecurityOptionsDefault() {
    return securityOptionsDefault;
  }

  @Override
  @Transactional(readOnly = true)
  public SecurityOptions getSecurityOptions(UserType userType) {
    if (securityOptionsUsers.containsKey(userType)) {
      return securityOptionsUsers.get(userType);
    }
    return getSecurityOptionsDefault();
  }

  @Override
  @Transactional(readOnly = true)
  public SecurityOptions getSecurityOptions(User user) {
    if (user == null) {
      return getSecurityOptionsDefault();
    }
    return getSecurityOptions(user.getType());
  }
}

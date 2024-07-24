package basicapp.back.security.service;

import static org.iglooproject.spring.property.SpringSecurityPropertyIds.PASSWORD_EXPIRATION_DAYS;
import static org.iglooproject.spring.property.SpringSecurityPropertyIds.PASSWORD_HISTORY_COUNT;
import static org.iglooproject.spring.property.SpringSecurityPropertyIds.PASSWORD_RECOVERY_REQUEST_EXPIRATION_MINUTES;
import static org.iglooproject.spring.property.SpringSecurityPropertyIds.PASSWORD_RECOVERY_REQUEST_TOKEN_RANDOM_COUNT;

import basicapp.back.business.history.model.atomic.HistoryEventType;
import basicapp.back.business.history.model.bean.HistoryLogAdditionalInformationBean;
import basicapp.back.business.history.service.IHistoryLogService;
import basicapp.back.business.notification.service.INotificationService;
import basicapp.back.business.user.model.User;
import basicapp.back.business.user.model.atomic.UserPasswordRecoveryRequestInitiator;
import basicapp.back.business.user.model.atomic.UserPasswordRecoveryRequestType;
import basicapp.back.business.user.service.IUserService;
import basicapp.back.security.model.SecurityOptions;
import com.google.common.collect.EvictingQueue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import org.apache.commons.lang3.RandomStringUtils;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.security.business.user.model.GenericUser;
import org.iglooproject.jpa.util.HibernateUtils;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.spring.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

public class SecurityManagementServiceImpl implements ISecurityManagementService {

  @Autowired private IUserService userService;

  @Autowired private INotificationService notificationService;

  @Autowired private IPropertyService propertyService;

  @Autowired private IHistoryLogService historyLogService;

  @Autowired private PasswordEncoder passwordEncoder;

  private final SecurityOptions securityOptionsDefault;

  private final Map<Class<? extends GenericUser<?, ?>>, SecurityOptions> securityOptionsUsers;

  public SecurityManagementServiceImpl(
      SecurityOptions securityOptionsDefault,
      ImmutableMap<Class<? extends GenericUser<?, ?>>, SecurityOptions> securityOptionsUsers) {
    this.securityOptionsDefault = securityOptionsDefault;
    this.securityOptionsUsers = ImmutableMap.copyOf(securityOptionsUsers);
  }

  @Override
  public SecurityOptions getSecurityOptionsDefault() {
    return securityOptionsDefault;
  }

  @Override
  public SecurityOptions getSecurityOptions(Class<? extends User> clazz) {
    if (securityOptionsUsers.containsKey(clazz)) {
      return securityOptionsUsers.get(clazz);
    }
    return getSecurityOptionsDefault();
  }

  @Override
  public SecurityOptions getSecurityOptions(User user) {
    if (user == null) {
      return getSecurityOptionsDefault();
    }
    return getSecurityOptions(HibernateUtils.unwrap(user).getClass());
  }

  @Override
  public void initiatePasswordRecoveryRequest(
      User user,
      UserPasswordRecoveryRequestType type,
      UserPasswordRecoveryRequestInitiator initiator)
      throws ServiceException, SecurityServiceException {
    initiatePasswordRecoveryRequest(user, type, initiator, user);
  }

  @Override
  public void initiatePasswordRecoveryRequest(
      User user,
      UserPasswordRecoveryRequestType type,
      UserPasswordRecoveryRequestInitiator initiator,
      User author)
      throws ServiceException, SecurityServiceException {
    user.getPasswordRecoveryRequest()
        .setToken(
            RandomStringUtils.randomAlphanumeric(
                propertyService.get(PASSWORD_RECOVERY_REQUEST_TOKEN_RANDOM_COUNT)));
    user.getPasswordRecoveryRequest().setCreationDate(Instant.now());
    user.getPasswordRecoveryRequest().setType(type);
    user.getPasswordRecoveryRequest().setInitiator(initiator);

    userService.update(user);

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
  public void updatePassword(User user, String password)
      throws ServiceException, SecurityServiceException {
    updatePassword(user, password, user);
  }

  @Override
  public void updatePassword(User user, String password, User author)
      throws ServiceException, SecurityServiceException {
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

      user.getPasswordInformation().setHistory(ImmutableList.copyOf(historyQueue));
    }

    user.getPasswordRecoveryRequest().reset();
    userService.update(user);

    historyLogService.log(
        HistoryEventType.PASSWORD_UPDATE, user, HistoryLogAdditionalInformationBean.empty());
  }

  @Override
  public boolean checkPassword(String password, User user)
      throws ServiceException, SecurityServiceException {
    return passwordEncoder.matches(password, user.getPasswordHash());
  }
}

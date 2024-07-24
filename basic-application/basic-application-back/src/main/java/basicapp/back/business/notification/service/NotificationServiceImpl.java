package basicapp.back.business.notification.service;

import basicapp.back.business.user.model.User;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.mail.api.SimpleRecipient;
import org.iglooproject.spring.notification.service.AbstractNotificationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl extends AbstractNotificationServiceImpl
    implements INotificationService {

  private static final String ERROR_EXCEPTION_MESSAGE =
      "Error during send mail process (to: %s, subject: %s)";

  @Autowired private IBasicApplicationNotificationUrlBuilderService notificationUrlBuilderService;

  @Autowired private IBasicApplicationNotificationContentDescriptorFactory contentDescriptorFactory;

  @Override
  public void sendExampleNotification(User user) throws ServiceException {
    try {
      Instant instant = Instant.now();
      String url = notificationUrlBuilderService.getUserDescriptionUrl(user);

      builder()
          .to(user)
          .content(contentDescriptorFactory.example(user, instant))
          .template("example.ftl")
          .variable("userFullName", user.getFullName())
          .variable("date", Date.from(instant))
          .variable("url", url)
          .send();
    } catch (RuntimeException | ServiceException e) {
      throw new ServiceException(ERROR_EXCEPTION_MESSAGE, e);
    }
  }

  @Override
  public void sendExampleNotification(User userTo, String from) throws ServiceException {
    try {
      Instant instant = Instant.now();

      builder()
          .sender("no-reply@basicapp.org")
          .from(from)
          .to(new SimpleRecipient(Locale.FRANCE, userTo.getEmail(), userTo.getFullName()))
          .content(contentDescriptorFactory.example(userTo, instant))
          .send();
    } catch (RuntimeException | ServiceException e) {
      throw new ServiceException(ERROR_EXCEPTION_MESSAGE, e);
    }
  }

  @Override
  public void sendUserPasswordRecoveryRequest(User user) throws ServiceException {
    try {
      Instant instant = Instant.now();

      builder()
          .to(user)
          .content(contentDescriptorFactory.userPasswordRecoveryRequest(user, instant))
          .send();
    } catch (RuntimeException | ServiceException e) {
      throw new ServiceException(ERROR_EXCEPTION_MESSAGE, e);
    }
  }
}

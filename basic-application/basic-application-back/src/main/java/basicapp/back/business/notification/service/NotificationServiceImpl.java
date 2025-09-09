package basicapp.back.business.notification.service;

import basicapp.back.business.notification.service.exception.NotificationException;
import basicapp.back.business.user.model.User;
import java.time.Instant;
import java.util.Date;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.spring.notification.service.AbstractNotificationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl extends AbstractNotificationServiceImpl
    implements INotificationService {

  private static final String ERROR_EXCEPTION_MESSAGE =
      "Error during send mail process (to: %s, subject: %s)";

  private final IBasicApplicationNotificationUrlBuilderService notificationUrlBuilderService;
  private final IBasicApplicationNotificationContentDescriptorFactory contentDescriptorFactory;

  @Autowired
  public NotificationServiceImpl(
      IBasicApplicationNotificationUrlBuilderService notificationUrlBuilderService,
      IBasicApplicationNotificationContentDescriptorFactory contentDescriptorFactory) {
    this.notificationUrlBuilderService = notificationUrlBuilderService;
    this.contentDescriptorFactory = contentDescriptorFactory;
  }

  @Override
  public void sendExampleNotification(User user) throws NotificationException {
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
      throw new NotificationException(ERROR_EXCEPTION_MESSAGE, e);
    }
  }

  @Override
  public void sendUserPasswordRecoveryRequest(User user) throws NotificationException {
    try {
      Instant instant = Instant.now();

      builder()
          .to(user)
          .content(contentDescriptorFactory.userPasswordRecoveryRequest(user, instant))
          .send();
    } catch (RuntimeException | ServiceException e) {
      throw new NotificationException(ERROR_EXCEPTION_MESSAGE, e);
    }
  }
}

package basicapp.back.business.notification.service;

import basicapp.back.business.user.model.User;
import java.time.Instant;
import org.iglooproject.spring.notification.model.INotificationContentDescriptor;
import org.iglooproject.spring.notification.util.NotificationContentDescriptors;

public class EmptyNotificationContentDescriptorFactoryImpl
    implements IBasicApplicationNotificationContentDescriptorFactory {

  private static final INotificationContentDescriptor DEFAULT_DESCRIPTOR =
      NotificationContentDescriptors.explicit(
          "defaultSubject", "defaultTextBody", "defaultHtmlBody");

  @Override
  public INotificationContentDescriptor example(User user, Instant instant) {
    return DEFAULT_DESCRIPTOR;
  }

  @Override
  public INotificationContentDescriptor userPasswordRecoveryRequest(User user, Instant instant) {
    return DEFAULT_DESCRIPTOR;
  }
}
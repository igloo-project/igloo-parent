package basicapp.back.business.notification.service;

import basicapp.back.business.user.model.User;
import java.time.Instant;
import org.iglooproject.spring.notification.model.INotificationContentDescriptor;

public interface IBasicApplicationNotificationContentDescriptorFactory {

  INotificationContentDescriptor example(User user, Instant instant);

  INotificationContentDescriptor userPasswordRecoveryRequest(User user, Instant instant);
}

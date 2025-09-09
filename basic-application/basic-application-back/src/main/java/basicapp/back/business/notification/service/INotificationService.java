package basicapp.back.business.notification.service;

import basicapp.back.business.notification.service.exception.NotificationException;
import basicapp.back.business.user.model.User;

public interface INotificationService {

  void sendExampleNotification(User user) throws NotificationException;

  void sendUserPasswordRecoveryRequest(User user) throws NotificationException;
}

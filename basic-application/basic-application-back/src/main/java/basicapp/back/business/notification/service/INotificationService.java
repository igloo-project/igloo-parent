package basicapp.back.business.notification.service;

import basicapp.back.business.user.model.User;
import org.iglooproject.jpa.exception.ServiceException;

public interface INotificationService {

  void sendExampleNotification(User user) throws ServiceException;

  void sendExampleNotification(User userTo, String from) throws ServiceException;

  void sendUserPasswordRecoveryRequest(User user) throws ServiceException;
}

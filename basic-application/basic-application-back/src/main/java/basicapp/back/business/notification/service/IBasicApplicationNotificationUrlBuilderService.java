package basicapp.back.business.notification.service;

import basicapp.back.business.user.model.User;
import org.iglooproject.spring.notification.service.INotificationUrlBuilderService;

public interface IBasicApplicationNotificationUrlBuilderService
    extends INotificationUrlBuilderService {

  String getUserDescriptionUrl(User user);
}

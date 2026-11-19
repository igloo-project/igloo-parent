package basicapp.back.business.notification.service;

import basicapp.back.business.user.model.User;
import org.iglooproject.spring.notification.service.INotificationUrlBuilderService;
import org.jspecify.annotations.Nullable;

public interface IBasicApplicationNotificationUrlBuilderService
    extends INotificationUrlBuilderService {

  @Nullable String getUserDescriptionUrl(User user);
}

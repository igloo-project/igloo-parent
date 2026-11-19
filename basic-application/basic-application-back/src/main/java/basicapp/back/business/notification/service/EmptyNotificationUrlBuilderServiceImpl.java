package basicapp.back.business.notification.service;

import basicapp.back.business.user.model.User;
import org.jspecify.annotations.Nullable;

public class EmptyNotificationUrlBuilderServiceImpl
    implements IBasicApplicationNotificationUrlBuilderService {

  @Override
  public @Nullable String getUserDescriptionUrl(User user) {
    return null;
  }
}

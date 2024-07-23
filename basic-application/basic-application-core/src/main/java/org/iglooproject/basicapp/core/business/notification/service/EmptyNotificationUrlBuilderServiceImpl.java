package org.iglooproject.basicapp.core.business.notification.service;

import org.iglooproject.basicapp.core.business.user.model.User;

public class EmptyNotificationUrlBuilderServiceImpl
    implements IBasicApplicationNotificationUrlBuilderService {

  @Override
  public String getUserDescriptionUrl(User user) {
    return null;
  }
}

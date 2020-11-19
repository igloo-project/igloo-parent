package org.iglooproject.basicapp.core.business.notification.service;

import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.spring.notification.service.INotificationUrlBuilderService;

public interface IBasicApplicationNotificationUrlBuilderService extends INotificationUrlBuilderService {

	String getUserDescriptionUrl(User user);

}

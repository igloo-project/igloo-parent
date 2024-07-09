package basicapp.back.business.notification.service;

import org.iglooproject.spring.notification.service.INotificationUrlBuilderService;

import basicapp.back.business.user.model.User;

public interface IBasicApplicationNotificationUrlBuilderService extends INotificationUrlBuilderService {

	String getUserDescriptionUrl(User user);

}

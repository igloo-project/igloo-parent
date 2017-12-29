package org.iglooproject.basicapp.core.business.notification.service;

import org.iglooproject.basicapp.core.business.user.model.User;


public interface INotificationUrlBuilderService {
	
	String getUserDescriptionUrl(User user);

}

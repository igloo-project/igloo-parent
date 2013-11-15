package fr.openwide.core.basicapp.core.business.notification.service;

import fr.openwide.core.basicapp.core.business.user.model.User;


public interface INotificationUrlBuilderService {
	
	String getUserDescriptionUrl(User user);

}

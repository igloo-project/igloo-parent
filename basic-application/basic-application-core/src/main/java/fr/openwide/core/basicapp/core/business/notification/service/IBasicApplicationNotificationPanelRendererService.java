package fr.openwide.core.basicapp.core.business.notification.service;

import java.util.Date;

import fr.openwide.core.basicapp.core.business.user.model.User;

public interface IBasicApplicationNotificationPanelRendererService {

	String renderExampleNotificationPanel(User user, Date date);

}

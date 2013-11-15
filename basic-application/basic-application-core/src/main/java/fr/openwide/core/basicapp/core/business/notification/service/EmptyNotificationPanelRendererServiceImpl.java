package fr.openwide.core.basicapp.core.business.notification.service;

import java.util.Date;

import fr.openwide.core.basicapp.core.business.user.model.User;


/**
 * Implémentation bouche-trou, uniquement pour combler la dépendance.
 */
public class EmptyNotificationPanelRendererServiceImpl implements IBasicApplicationNotificationPanelRendererService {

	@Override
	public String renderExampleNotificationPanel(User user, Date date) {
		return null;
	}

}

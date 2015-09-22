package fr.openwide.core.basicapp.core.business.notification.service;

import fr.openwide.core.basicapp.core.business.user.model.User;

/**
 * Implémentation bouche-trou, uniquement pour combler la dépendance.
 */
public class EmptyNotificationUrlBuilderServiceImpl implements INotificationUrlBuilderService {

	@Override
	public String getUserDescriptionUrl(User user) {
		return null;
	}
}

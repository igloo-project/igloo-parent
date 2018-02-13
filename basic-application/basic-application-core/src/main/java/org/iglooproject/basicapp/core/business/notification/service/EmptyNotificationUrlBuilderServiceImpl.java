package org.iglooproject.basicapp.core.business.notification.service;

import org.iglooproject.basicapp.core.business.user.model.User;

/**
 * Implémentation bouche-trou, uniquement pour combler la dépendance.
 */
public class EmptyNotificationUrlBuilderServiceImpl implements INotificationUserProfileUrlBuilderService {

	@Override
	public String getUserDescriptionUrl(User user) {
		return null;
	}
}

package basicapp.back.business.notification.service;

import basicapp.back.business.user.model.User;

public class EmptyNotificationUrlBuilderServiceImpl implements IBasicApplicationNotificationUrlBuilderService {

	@Override
	public String getUserDescriptionUrl(User user) {
		return null;
	}

}

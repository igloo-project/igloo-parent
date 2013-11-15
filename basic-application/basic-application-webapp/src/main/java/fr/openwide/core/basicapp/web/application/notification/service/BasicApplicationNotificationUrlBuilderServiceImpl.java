package fr.openwide.core.basicapp.web.application.notification.service;

import org.springframework.stereotype.Service;

import fr.openwide.core.basicapp.core.business.notification.service.INotificationUrlBuilderService;
import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.web.application.BasicApplicationApplication;
import fr.openwide.core.basicapp.web.application.administration.page.AdministrationUserDescriptionPage;
import fr.openwide.core.wicket.more.model.GenericEntityModel;
import fr.openwide.core.wicket.more.notification.service.AbstractNotificationUrlBuilderServiceImpl;

/**
 * This service is used to generate the URL used in the text version of the notification emails.
 * 
 * It shouldn't be used for other purposes.
 */
@Service("basicApplicationNotificationUrlBuilderService")
public class BasicApplicationNotificationUrlBuilderServiceImpl extends AbstractNotificationUrlBuilderServiceImpl
		implements INotificationUrlBuilderService {

	@Override
	protected String getApplicationName() {
		return BasicApplicationApplication.NAME;
	}

	@Override
	public String getUserDescriptionUrl(User user) {
		return buildUrl(AdministrationUserDescriptionPage.linkGenerator(GenericEntityModel.of(user)));
	}
}

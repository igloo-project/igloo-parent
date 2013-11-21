package fr.openwide.core.basicapp.web.application.notification.service;

import java.util.concurrent.Callable;

import org.springframework.stereotype.Service;

import fr.openwide.core.basicapp.core.business.notification.service.INotificationUrlBuilderService;
import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.web.application.BasicApplicationApplication;
import fr.openwide.core.basicapp.web.application.administration.page.AdministrationUserDescriptionPage;
import fr.openwide.core.wicket.more.link.descriptor.generator.IPageLinkGenerator;
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
	public String getUserDescriptionUrl(final User user) {
		Callable<IPageLinkGenerator> pageLinkGeneratorTask = new Callable<IPageLinkGenerator>() {
			@Override
			public IPageLinkGenerator call() throws Exception {
				return AdministrationUserDescriptionPage.linkGenerator(GenericEntityModel.of(user));
			}
		};
		
		return buildUrl(pageLinkGeneratorTask);
	}
}

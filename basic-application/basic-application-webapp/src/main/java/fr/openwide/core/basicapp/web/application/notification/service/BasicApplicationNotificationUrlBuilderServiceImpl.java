package fr.openwide.core.basicapp.web.application.notification.service;

import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.core.basicapp.core.business.notification.service.INotificationUrlBuilderService;
import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.web.application.navigation.link.LinkFactory;
import fr.openwide.core.wicket.more.link.descriptor.generator.IPageLinkGenerator;
import fr.openwide.core.wicket.more.model.GenericEntityModel;
import fr.openwide.core.wicket.more.notification.service.AbstractNotificationUrlBuilderServiceImpl;
import fr.openwide.core.wicket.more.notification.service.IWicketContextExecutor;

/**
 * This service is used to generate the URL used in the text version of the notification emails.
 * 
 * It shouldn't be used for other purposes.
 */
@Service("basicApplicationNotificationUrlBuilderService")
public class BasicApplicationNotificationUrlBuilderServiceImpl extends AbstractNotificationUrlBuilderServiceImpl
		implements INotificationUrlBuilderService {

	@Autowired
	public BasicApplicationNotificationUrlBuilderServiceImpl(IWicketContextExecutor wicketExecutor) {
		super(wicketExecutor);
	}

	@Override
	public String getUserDescriptionUrl(final User user) {
		Callable<IPageLinkGenerator> pageLinkGeneratorTask = new Callable<IPageLinkGenerator>() {
			@Override
			public IPageLinkGenerator call() {
				return LinkFactory.get().userDescription(GenericEntityModel.of(user));
			}
		};
		
		return buildUrl(pageLinkGeneratorTask);
	}
}

package org.iglooproject.basicapp.web.application.notification.service;

import java.util.concurrent.Callable;

import org.iglooproject.basicapp.core.business.notification.service.IBasicApplicationNotificationUrlBuilderService;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.web.application.administration.template.AdministrationUserDetailTemplate;
import org.iglooproject.wicket.more.link.descriptor.generator.IPageLinkGenerator;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import org.iglooproject.wicket.more.notification.service.AbstractNotificationUrlBuilderServiceImpl;
import org.iglooproject.wicket.more.notification.service.IWicketContextProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This service is used to generate the URL used in the text version of the notification emails.
 * 
 * It shouldn't be used for other purposes.
 */
@Service("basicApplicationNotificationUrlBuilderService")
public class BasicApplicationNotificationUrlBuilderServiceImpl extends AbstractNotificationUrlBuilderServiceImpl
		implements IBasicApplicationNotificationUrlBuilderService {

	@Autowired
	public BasicApplicationNotificationUrlBuilderServiceImpl(IWicketContextProvider contextProvider) {
		super(contextProvider);
	}

	@Override
	public String getUserDescriptionUrl(final User user) {
		Callable<IPageLinkGenerator> pageLinkGeneratorTask = new Callable<IPageLinkGenerator>() {
			@Override
			public IPageLinkGenerator call() {
				return AdministrationUserDetailTemplate.mapper().ignoreParameter2().map(GenericEntityModel.of(user));
			}
		};
		
		return buildUrl(pageLinkGeneratorTask);
	}
}

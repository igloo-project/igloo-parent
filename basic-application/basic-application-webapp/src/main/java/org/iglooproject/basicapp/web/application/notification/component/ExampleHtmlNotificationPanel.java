package org.iglooproject.basicapp.web.application.notification.component;

import java.time.Instant;
import java.util.Date;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.basicapp.web.application.BasicApplicationApplication;
import org.iglooproject.basicapp.web.application.administration.template.AdministrationUserDetailTemplate;
import org.iglooproject.basicapp.web.application.notification.model.InstantToDateModel;

import igloo.wicket.component.CoreLabel;
import igloo.wicket.model.BindingModel;

public class ExampleHtmlNotificationPanel extends AbstractHtmlNotificationPanel<User> {
	
	private static final long serialVersionUID = -2406171975975069084L;
	
	public ExampleHtmlNotificationPanel(String id, IModel<User> userModel, IModel<Instant> instantModel) {
		super(id, userModel);
		IModel<Date> dateModel = new InstantToDateModel(instantModel);
		add(
			new WebMarkupContainer("intro")
				.add(
					new CoreLabel("user", userModel)
						.showPlaceholder(),
					new CoreLabel("date", dateModel)
						.showPlaceholder(),
					new CoreLabel("time", dateModel)
						.showPlaceholder()
				)
		);
		
		IModel<String> urlModel = LoadableDetachableModel.of(() ->
			BasicApplicationApplication.get().getHomePageLinkDescriptor()
				.bypassPermissions()
				.fullUrl()
		);
		
		add(
			new ExternalLink("mainLink", urlModel),
			new ExternalLink("url", urlModel, urlModel)
		);
		
		add(
			new ExternalLink("userLink",
				LoadableDetachableModel.of(() ->
					AdministrationUserDetailTemplate.mapper()
						.ignoreParameter2()
						.map(userModel)
						.bypassPermissions()
						.fullUrl()
				),
				BindingModel.of(userModel, Bindings.user().username())
			),
			new CoreLabel("firstname", BindingModel.of(userModel, Bindings.user().firstName())),
			new CoreLabel("lastname", BindingModel.of(userModel, Bindings.user().lastName())),
			new CoreLabel("email", BindingModel.of(userModel, Bindings.user().email())),
			new CoreLabel("lastLoginDate", new InstantToDateModel(BindingModel.of(userModel, Bindings.user().lastLoginDate())))
				.showPlaceholder()
		);
	}

}

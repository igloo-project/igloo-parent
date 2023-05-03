package org.iglooproject.basicapp.web.application.notification.component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.basicapp.web.application.BasicApplicationApplication;
import org.iglooproject.basicapp.web.application.administration.template.AdministrationUserDetailTemplate;

import igloo.wicket.component.CoreLabel;
import igloo.wicket.model.BindingModel;

public class ExampleHtmlNotificationPanel extends AbstractHtmlNotificationPanel<User> {
	
	private static final long serialVersionUID = -2406171975975069084L;
	
	public ExampleHtmlNotificationPanel(String id, IModel<User> userModel, IModel<Instant> dateModel) {
		super(id, userModel);
		
		add(
			new WebMarkupContainer("intro")
				.add(
					new CoreLabel("user", userModel)
						.showPlaceholder(),
					new CoreLabel("date", () -> LocalDate.ofInstant(dateModel.getObject(), ZoneId.systemDefault()))
						.showPlaceholder(),
					new CoreLabel("time", () -> LocalTime.ofInstant(dateModel.getObject(), ZoneId.systemDefault()))
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
			new CoreLabel("lastLoginDate", BindingModel.of(userModel, Bindings.user().lastLoginDate()))
				.showPlaceholder()
		);
	}

}

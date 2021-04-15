package org.iglooproject.basicapp.web.application.notification.page;

import java.util.Date;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.StringResourceModel;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.basicapp.web.application.BasicApplicationApplication;
import org.iglooproject.basicapp.web.application.administration.template.AdministrationUserDetailTemplate;
import org.iglooproject.basicapp.web.application.common.renderer.UserEnabledRenderer;
import org.iglooproject.basicapp.web.application.notification.template.AbstractHtmlNotificationPage;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.behavior.BootstrapColorBehavior;

import igloo.wicket.component.CoreLabel;
import igloo.wicket.component.DateLabel;
import igloo.wicket.model.BindingModel;
import igloo.wicket.util.DatePattern;

public class ExampleHtmlNotificationPage extends AbstractHtmlNotificationPage<User> {

	private static final long serialVersionUID = 1L;

	public ExampleHtmlNotificationPage(IModel<User> userModel, IModel<Date> dateModel) {
		super(userModel);
		
		add(
			new WebMarkupContainer("intro")
				.add(
					new CoreLabel("user", userModel)
						.showPlaceholder(),
					new DateLabel("date", dateModel, DatePattern.SHORT_DATE)
						.showPlaceholder(),
					new DateLabel("time", dateModel, DatePattern.TIME)
						.showPlaceholder()
				)
		);
		
		IModel<String> urlModel = LoadableDetachableModel.of(() ->
			BasicApplicationApplication.get().getHomePageLinkDescriptor()
				.bypassPermissions()
				.fullUrl()
		);
		
		add(
			new ExternalLink("link", urlModel),
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
			new CoreLabel("enabled", UserEnabledRenderer.get().asModel(userModel))
				.showPlaceholder()
				.add(BootstrapColorBehavior.text(UserEnabledRenderer.get().asModel(userModel).getColorModel())),
			new DateLabel("lastLoginDate", BindingModel.of(userModel, Bindings.user().lastLoginDate()), DatePattern.SHORT_DATETIME)
		);
	}

	@Override
	protected IModel<?> getPreviewModel() {
		return new StringResourceModel("notification.panel.example.preview");
	}

}

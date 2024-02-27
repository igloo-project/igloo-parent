package org.iglooproject.basicapp.web.application.notification.page;

import java.time.Instant;
import java.util.Locale;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.StringResourceModel;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.basicapp.core.util.time.DateTimePattern;
import org.iglooproject.basicapp.web.application.BasicApplicationApplication;
import org.iglooproject.basicapp.web.application.administration.template.AdministrationUserDetailTemplate;
import org.iglooproject.basicapp.web.application.common.renderer.UserEnabledRenderer;
import org.iglooproject.basicapp.web.application.notification.model.InstantToDateModel;
import org.iglooproject.basicapp.web.application.notification.template.AbstractHtmlNotificationPage;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.behavior.BootstrapColorBehavior;

import igloo.wicket.component.CoreLabel;
import igloo.wicket.model.BindingModel;
import igloo.wicket.renderer.Renderer;

public class ExampleHtmlNotificationPage extends AbstractHtmlNotificationPage<User> {

	private static final long serialVersionUID = 1L;

	public ExampleHtmlNotificationPage(IModel<User> userModel, IModel<Instant> instantModel, IModel<Locale> localeModel) {
		super(userModel, localeModel);
		
		add(
			new WebMarkupContainer("intro")
				.add(
					new CoreLabel("user", userModel)
						.showPlaceholder(),
					new CoreLabel("date", Renderer.fromDateTimePattern(DateTimePattern.SHORT_DATE).asModel(instantModel))
						.showPlaceholder(),
					new CoreLabel("time", Renderer.fromDateTimePattern(DateTimePattern.TIME).asModel(instantModel))
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
			new CoreLabel("lastLoginDate", new InstantToDateModel(BindingModel.of(userModel, Bindings.user().lastLoginDate())))
		);
	}

	@Override
	protected IModel<?> getPreviewModel() {
		return new StringResourceModel("notification.panel.example.preview");
	}

}
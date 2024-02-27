package org.iglooproject.basicapp.web.application.notification.page;

import java.time.Instant;
import java.util.Date;
import java.util.Locale;

import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.StringResourceModel;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.util.ResourceKeyGenerator;
import org.iglooproject.basicapp.core.util.binding.Bindings;
import org.iglooproject.basicapp.web.application.notification.model.InstantToDateModel;
import org.iglooproject.basicapp.web.application.notification.template.AbstractHtmlNotificationPage;
import org.iglooproject.wicket.more.link.descriptor.generator.IPageLinkGenerator;

import igloo.wicket.component.CoreLabel;
import igloo.wicket.model.BindingModel;

public class UserPasswordRecoveryRequestHtmlNotificationPage<T extends User> extends AbstractHtmlNotificationPage<T> {

	private static final long serialVersionUID = 1L;

	public UserPasswordRecoveryRequestHtmlNotificationPage(
		ResourceKeyGenerator resourceKeyGenerator,
		IModel<T> objectModel,
		IModel<User> authorModel,
		IModel<Instant> instantModel,
		IPageLinkGenerator linkGenerator,
		IModel<Locale> localeModel
	) {
		this(resourceKeyGenerator, resourceKeyGenerator, objectModel, authorModel, instantModel, linkGenerator, localeModel);
	}

	public UserPasswordRecoveryRequestHtmlNotificationPage(
		ResourceKeyGenerator resourceKeyGenerator,
		ResourceKeyGenerator defaultResourceKeyGenerator,
		IModel<T> objectModel,
		IModel<User> authorModel,
		IModel<Instant> instantModel,
		IPageLinkGenerator linkGenerator,
		IModel<Locale> localeModel
	) {
		super(objectModel, localeModel);
		
		IModel<Date> dateModel = new InstantToDateModel(instantModel);
		
		add(
			new CoreLabel(
				"title",
				new StringResourceModel(resourceKeyGenerator.resourceKey("title"))
					.setDefaultValue(
						new StringResourceModel(defaultResourceKeyGenerator.resourceKey("title"))
					)
			)
		);
		
		add(
			new CoreLabel(
				"intro",
				new StringResourceModel(resourceKeyGenerator.resourceKey("intro"), objectModel)
					.setParameters(dateModel, authorModel)
					.setDefaultValue(
						new StringResourceModel(defaultResourceKeyGenerator.resourceKey("intro"), objectModel)
							.setParameters(dateModel, authorModel)
					)
			)
		);
		
		add(
			new CoreLabel(
				"text",
				new StringResourceModel(resourceKeyGenerator.resourceKey("text"), objectModel)
					.setParameters(dateModel, authorModel)
					.setDefaultValue(
						new StringResourceModel(defaultResourceKeyGenerator.resourceKey("text"), objectModel)
							.setParameters(dateModel, authorModel)
					)
			)
		);
		
		StringResourceModel linkLabelModel = new StringResourceModel(resourceKeyGenerator.resourceKey("link"), objectModel)
			.setParameters(dateModel, authorModel)
			.setDefaultValue(
				new StringResourceModel(defaultResourceKeyGenerator.resourceKey("link"), objectModel)
					.setParameters(dateModel, authorModel)
			);
		
		IModel<String> urlModel = LoadableDetachableModel.of(() ->
			linkGenerator
				.bypassPermissions()
				.fullUrl()
		);
		
		add(
			new ExternalLink("link", urlModel, linkLabelModel),
			new ExternalLink("url", urlModel, urlModel)
		);
		
		add(
			new CoreLabel("username", BindingModel.of(objectModel, Bindings.user().username()))
		);
	}

}
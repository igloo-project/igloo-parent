package org.iglooproject.basicapp.web.application.notification.template;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.head.CssContentHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.PackageResource;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.iglooproject.basicapp.web.application.BasicApplicationApplication;
import org.iglooproject.basicapp.web.application.BasicApplicationSession;
import org.iglooproject.basicapp.web.application.common.template.resources.styles.notification.email.NotificationEmailScssResourceReference;
import org.iglooproject.basicapp.web.application.common.template.resources.styles.notification.head.NotificationHeadScssResourceReference;
import org.iglooproject.basicapp.web.application.notification.component.EnvironmentContainerPanel;
import org.iglooproject.wicket.more.markup.html.CoreWebPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import igloo.wicket.behavior.ClassAttributeAppender;
import igloo.wicket.component.CoreLabel;

public abstract class AbstractHtmlNotificationPage<T> extends CoreWebPage {

	private static final long serialVersionUID = -3576134833190785445L;

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractHtmlNotificationPage.class);

	protected AbstractHtmlNotificationPage() {
		this(null);
	}

	protected AbstractHtmlNotificationPage(IModel<T> model) {
		super(model);
		
		add(
			new TransparentWebMarkupContainer("htmlElement")
				.add(AttributeAppender.append("lang", BasicApplicationSession.get().getLocale().getLanguage()))
		);
		
		add(
			new TransparentWebMarkupContainer("bodyElement")
				.add(new ClassAttributeAppender(BasicApplicationSession.get().getEnvironmentModel()))
				.add(AttributeAppender.append("lang", BasicApplicationSession.get().getLocale().getLanguage()))
		);
		
		add(new EnvironmentContainerPanel("environment"));
		
		add(
			new ExternalLink("homePageLink",
				LoadableDetachableModel.of(() ->
					BasicApplicationApplication.get()
						.getHomePageLinkDescriptor()
						.bypassPermissions()
						.fullUrl()
				)
			)
		);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		add(
			new CoreLabel("preview", getPreviewModel())
				.hideIfEmpty()
		);
	}

	protected IModel<?> getPreviewModel() {
		return Model.of();
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		try (
			InputStream isEmail = ((PackageResource) NotificationEmailScssResourceReference.get().getResource()).getResourceStream().getInputStream();
			InputStream isHead = ((PackageResource) NotificationHeadScssResourceReference.get().getResource()).getResourceStream().getInputStream()
		) {
			response.render(CssContentHeaderItem.forCSS(IOUtils.toString(isEmail, StandardCharsets.UTF_8), "notification-email"));
			response.render(CssContentHeaderItem.forCSS(IOUtils.toString(isHead, StandardCharsets.UTF_8), "notification-head"));
		} catch (IOException | ResourceStreamNotFoundException e) {
			LOGGER.error("Error on load notification css.", e);
		}
	}

}

package org.iglooproject.basicapp.web.application.console.notification.demo.page;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.web.application.BasicApplicationSession;
import org.iglooproject.basicapp.web.application.common.form.UserAutocompleteAjaxComponent;
import org.iglooproject.spring.notification.exception.NotificationContentRenderingException;
import org.iglooproject.spring.notification.model.INotificationContentDescriptor;
import org.iglooproject.wicket.more.AbstractCoreSession;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Throwables;

public class NotificationDemoPage extends WebPage {
	
	private static final long serialVersionUID = -1929048481327622623L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NotificationDemoPage.class);
	
	public NotificationDemoPage(final IModel<INotificationContentDescriptor> descriptorModel) throws NotificationContentRenderingException {
		super();
		
		MarkupContainer htmlRootElement = new TransparentWebMarkupContainer("htmlRootElement");
		htmlRootElement.add(AttributeAppender.append("lang", AbstractCoreSession.get().getLocale().getLanguage()));
		add(htmlRootElement);
		
		add(new Label("headPageTitle", new ResourceModel("console.notifications")));
		
		final IModel<User> recipientModel = new GenericEntityModel<>(BasicApplicationSession.get().getUser());
		final IModel<INotificationContentDescriptor> descriptorWithContextModel =
				new AbstractReadOnlyModel<INotificationContentDescriptor>() {
					private static final long serialVersionUID = 1L;
					@Override
					public INotificationContentDescriptor getObject() {
						return descriptorModel.getObject().withContext(recipientModel.getObject());
					}
					@Override
					public void detach() {
						super.detach();
						descriptorModel.detach();
						recipientModel.detach();
					}
				};
		
		Form<?> form = new Form<>("form");
		add(form);
		UserAutocompleteAjaxComponent userSelector = new UserAutocompleteAjaxComponent("recipient", recipientModel) {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(getPage());
			}
		};
		userSelector.setAutoUpdate(true);
		userSelector.setRequired(true);
		form.add(
				userSelector
		);

		IModel<String> subjectModel = new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;
			@Override
			public String getObject() {
				try {
					return descriptorWithContextModel.getObject().renderSubject();
				} catch (NotificationContentRenderingException|RuntimeException e) {
					LOGGER.error("Erreur lors du rendu du sujet", e);
					return Throwables.getStackTraceAsString(e);
				}
			}
			@Override
			public void detach() {
				super.detach();
				descriptorWithContextModel.detach();
			}
		};
		
		add(new Label("subject", subjectModel));
		
		IModel<String> bodyModel = new AbstractReadOnlyModel<String>() {
			private static final long serialVersionUID = 1L;
			@Override
			public String getObject() {
				try {
					return descriptorWithContextModel.getObject().renderHtmlBody();
				} catch (NotificationContentRenderingException|RuntimeException e) {
					LOGGER.error("Erreur lors du rendu du corps", e);
					return "<pre>" + Throwables.getStackTraceAsString(e) + "</pre>";
				}
			}
			@Override
			public void detach() {
				super.detach();
				descriptorWithContextModel.detach();
			}
		};
		add(
				new Label("htmlPanel", bodyModel)
						.setEscapeModelStrings(false)
		);
	}
}

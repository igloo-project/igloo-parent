package fr.openwide.core.basicapp.web.application.notification.service;

import java.util.Date;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableList;

import fr.openwide.core.basicapp.core.business.notification.service.IBasicApplicationNotificationContentDescriptorFactory;
import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.web.application.BasicApplicationApplication;
import fr.openwide.core.basicapp.web.application.BasicApplicationSession;
import fr.openwide.core.basicapp.web.application.notification.component.ExampleHtmlNotificationPanel;
import fr.openwide.core.basicapp.web.application.notification.component.SimpleUserActionHtmlNotificationPanel;
import fr.openwide.core.basicapp.web.application.notification.util.INotificationTypeDescriptor;
import fr.openwide.core.basicapp.web.application.notification.util.NotificationUserTypeDescriptor;
import fr.openwide.core.basicapp.web.application.security.util.SecurityUserTypeDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.generator.ILinkGenerator;
import fr.openwide.core.wicket.more.model.GenericEntityModel;
import fr.openwide.core.wicket.more.notification.model.IWicketNotificationDescriptor;
import fr.openwide.core.wicket.more.notification.service.AbstractNotificationContentDescriptorFactory;

@Service("BasicApplicationNotificationPanelRendererService")
public class BasicApplicationNotificationContentDescriptorFactoryImpl extends AbstractNotificationContentDescriptorFactory
		implements IBasicApplicationNotificationContentDescriptorFactory<IWicketNotificationDescriptor> {

	@Override
	protected String getApplicationName() {
		return BasicApplicationApplication.NAME;
	}

	@Override
	public IWicketNotificationDescriptor example(final User user, final Date date) {
		final IModel<User> userModel = GenericEntityModel.of(user);
		return new AbstractSimpleWicketNotificationDescriptor("notification.panel.example") {
			@Override
			public Object getSubjectParameter() {
				return userModel;
			}
			@Override
			public Iterable<?> getSubjectPositionalParameters() {
				return ImmutableList.of(user.getFullName());
			}
			@Override
			public Component createComponent(String wicketId) {
				return new ExampleHtmlNotificationPanel(wicketId, userModel, Model.of(date));
			}
		};
	}

	protected final <T> IWicketNotificationDescriptor simpleUserActionNotification(
			final INotificationTypeDescriptor typeDescriptor, final String actionMessageKeyPart,
			final IModel<T> objetModel, final ILinkGenerator linkGenerator) {
		return new AbstractSimpleWicketNotificationDescriptor(typeDescriptor.notificationRessourceKey(actionMessageKeyPart)) {
			@Override
			public IModel<?> getSubjectParameter() {
				return objetModel;
			}
			@Override
			public Component createComponent(String wicketId) {
				return new SimpleUserActionHtmlNotificationPanel<>(wicketId, typeDescriptor, actionMessageKeyPart,
						objetModel, BasicApplicationSession.get().getUserModel(), Model.of(new Date()), linkGenerator);
			}
		};
	}

	@Override
	public IWicketNotificationDescriptor userPasswordRecoveryRequest(User user) {
		IModel<User> model = GenericEntityModel.of(user);
		String actionMessageKeyPart = "password.recovery.request." + user.getPasswordRecoveryRequest().getType().name() + "." + user.getPasswordRecoveryRequest().getInitiator().name();
		return simpleUserActionNotification(NotificationUserTypeDescriptor.USER, actionMessageKeyPart, model, SecurityUserTypeDescriptor.USER.signInPageLinkDescriptor());
	}

}

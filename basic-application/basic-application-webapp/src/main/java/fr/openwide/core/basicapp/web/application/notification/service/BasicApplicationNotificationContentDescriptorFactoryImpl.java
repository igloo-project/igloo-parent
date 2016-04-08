package fr.openwide.core.basicapp.web.application.notification.service;

import java.util.Date;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableList;

import fr.openwide.core.basicapp.core.business.notification.service.IBasicApplicationNotificationContentDescriptorFactory;
import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.util.binding.Bindings;
import fr.openwide.core.basicapp.web.application.BasicApplicationSession;
import fr.openwide.core.basicapp.web.application.common.typedescriptor.INotificationTypeDescriptor;
import fr.openwide.core.basicapp.web.application.common.typedescriptor.user.NotificationUserTypeDescriptor;
import fr.openwide.core.basicapp.web.application.common.typedescriptor.user.UserTypeDescriptor;
import fr.openwide.core.basicapp.web.application.notification.component.ExampleHtmlNotificationPanel;
import fr.openwide.core.basicapp.web.application.notification.component.SimpleUserActionHtmlNotificationPanel;
import fr.openwide.core.wicket.more.link.descriptor.IPageLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.generator.ILinkGenerator;
import fr.openwide.core.wicket.more.model.BindingModel;
import fr.openwide.core.wicket.more.model.GenericEntityModel;
import fr.openwide.core.wicket.more.notification.model.IWicketNotificationDescriptor;
import fr.openwide.core.wicket.more.notification.service.AbstractNotificationContentDescriptorFactory;
import fr.openwide.core.wicket.more.notification.service.IWicketContextExecutor;

@Service("BasicApplicationNotificationPanelRendererService")
public class BasicApplicationNotificationContentDescriptorFactoryImpl
		extends AbstractNotificationContentDescriptorFactory
		implements IBasicApplicationNotificationContentDescriptorFactory<IWicketNotificationDescriptor> {
	
	@Autowired
	public BasicApplicationNotificationContentDescriptorFactoryImpl(IWicketContextExecutor wicketExecutor) {
		super(wicketExecutor);
	}

	@Override
	public IWicketNotificationDescriptor example(final User user, final Date date) {
		return new AbstractSimpleWicketNotificationDescriptor("notification.panel.example") {
			@Override
			public Object getSubjectParameter() {
				return user;
			}
			@Override
			public Iterable<?> getSubjectPositionalParameters() {
				return ImmutableList.of(user.getFullName());
			}
			@Override
			public Component createComponent(String wicketId) {
				return new ExampleHtmlNotificationPanel(wicketId, GenericEntityModel.of(user), Model.of(date));
			}
		};
	}

	protected final <T> IWicketNotificationDescriptor simpleUserActionNotification(
			final INotificationTypeDescriptor typeDescriptor, final String actionMessageKeyPart,
			final IModel<T> objectModel, final ILinkGenerator linkGenerator) {
		return new AbstractSimpleWicketNotificationDescriptor(typeDescriptor.notificationRessourceKey(actionMessageKeyPart)) {
			@Override
			public IModel<?> getSubjectParameter() {
				return objectModel;
			}
			@Override
			public Component createComponent(String wicketId) {
				return new SimpleUserActionHtmlNotificationPanel<>(wicketId, typeDescriptor, actionMessageKeyPart,
						objectModel, BasicApplicationSession.get().getUserModel(), Model.of(new Date()), linkGenerator);
			}
		};
	}

	@Override
	public IWicketNotificationDescriptor userPasswordRecoveryRequest(User user) {
		IModel<User> model = GenericEntityModel.of(user);
		String actionMessageKeyPart = "password.recovery.request." + user.getPasswordRecoveryRequest().getType().name() + "." + user.getPasswordRecoveryRequest().getInitiator().name();
		
		IPageLinkDescriptor linkDescriptor = null;
		switch (user.getPasswordRecoveryRequest().getType()) {
		case CREATION:
			linkDescriptor = UserTypeDescriptor.get(user).securityTypeDescriptor().passwordCreationPageLinkDescriptor(model, BindingModel.of(model, Bindings.user().passwordRecoveryRequest().token()));
			break;
		case RESET:
			linkDescriptor = UserTypeDescriptor.get(user).securityTypeDescriptor().passwordResetPageLinkDescriptor(model, BindingModel.of(model, Bindings.user().passwordRecoveryRequest().token()));
			break;
		default:
			throw new IllegalStateException("Recovery request type unknown.");
		}
		
		return simpleUserActionNotification(
				NotificationUserTypeDescriptor.USER,
				actionMessageKeyPart,
				model,
				linkDescriptor
		);
	}

}

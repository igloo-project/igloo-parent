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
import fr.openwide.core.basicapp.web.application.common.typedescriptor.user.NotificationUserTypeDescriptor;
import fr.openwide.core.basicapp.web.application.common.typedescriptor.user.UserTypeDescriptor;
import fr.openwide.core.basicapp.web.application.common.util.ResourceKeyGenerator;
import fr.openwide.core.basicapp.web.application.notification.component.ExampleHtmlNotificationPanel;
import fr.openwide.core.basicapp.web.application.notification.component.SimpleUserActionHtmlNotificationPanel;
import fr.openwide.core.spring.notification.model.INotificationContentDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.generator.ILinkGenerator;
import fr.openwide.core.wicket.more.link.descriptor.mapper.ITwoParameterLinkDescriptorMapper;
import fr.openwide.core.wicket.more.model.BindingModel;
import fr.openwide.core.wicket.more.model.GenericEntityModel;
import fr.openwide.core.wicket.more.notification.service.AbstractNotificationContentDescriptorFactory;
import fr.openwide.core.wicket.more.notification.service.IWicketContextProvider;

@Service("BasicApplicationNotificationPanelRendererService")
public class BasicApplicationNotificationContentDescriptorFactoryImpl
		extends AbstractNotificationContentDescriptorFactory
		implements IBasicApplicationNotificationContentDescriptorFactory {
	
	@Autowired
	public BasicApplicationNotificationContentDescriptorFactoryImpl(IWicketContextProvider contextProvider) {
		super(contextProvider);
	}

	@Override
	public INotificationContentDescriptor example(final User user, final Date date) {
		return new AbstractSimpleWicketNotificationDescriptor("notification.panel.example") {
			@Override
			public Object getSubjectParameter() {
				return user;
			}
			@Override
			public Iterable<?> getSubjectPositionalParameters() {
				return ImmutableList.of(user.getFullName(), date);
			}
			@Override
			public Component createComponent(String wicketId) {
				return new ExampleHtmlNotificationPanel(wicketId, GenericEntityModel.of(user), Model.of(date));
			}
		};
	}

	@Override
	public INotificationContentDescriptor userPasswordRecoveryRequest(final User user) {
		
		final ITwoParameterLinkDescriptorMapper<? extends ILinkGenerator, User, String> mapper;
		switch (user.getPasswordRecoveryRequest().getType()) {
		case CREATION:
			mapper = UserTypeDescriptor.get(user).securityTypeDescriptor().passwordCreationPageLinkDescriptorMapper();
			break;
		case RESET:
			mapper = UserTypeDescriptor.get(user).securityTypeDescriptor().passwordResetPageLinkDescriptorMapper();
			break;
		default:
			throw new IllegalStateException("Recovery request type unknown.");
		}

		String actionMessageKeyPart = "password.recovery.request." + user.getPasswordRecoveryRequest().getType().name()
				+ "." + user.getPasswordRecoveryRequest().getInitiator().name();
		final ResourceKeyGenerator keyGenerator = NotificationUserTypeDescriptor.USER.resourceKeyGenerator()
				.append(actionMessageKeyPart);
		return new AbstractSimpleWicketNotificationDescriptor(keyGenerator.resourceKey()) {
			@Override
			public IModel<?> getSubjectParameter() {
				return GenericEntityModel.of(user);
			}
			@Override
			public Component createComponent(String wicketId) {
				IModel<User> userModel = GenericEntityModel.of(user);
				return new SimpleUserActionHtmlNotificationPanel<>(
						wicketId,
						keyGenerator,
						userModel, userModel, Model.of(new Date()),
						mapper.map(userModel, BindingModel.of(userModel, Bindings.user().passwordRecoveryRequest().token()))
				);
			}
		};
	}

}

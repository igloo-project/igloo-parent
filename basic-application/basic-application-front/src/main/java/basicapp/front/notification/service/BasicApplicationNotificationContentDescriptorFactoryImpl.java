package basicapp.front.notification.service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.iglooproject.spring.notification.model.INotificationContentDescriptor;
import org.iglooproject.wicket.more.link.descriptor.generator.IPageLinkGenerator;
import org.iglooproject.wicket.more.link.descriptor.mapper.ITwoParameterLinkDescriptorMapper;
import org.iglooproject.wicket.more.model.GenericEntityModel;
import org.iglooproject.wicket.more.notification.service.AbstractNotificationContentDescriptorFactory;
import org.iglooproject.wicket.more.notification.service.IWicketContextProvider;
import org.springframework.beans.factory.annotation.Autowired;

import basicapp.back.business.notification.service.IBasicApplicationNotificationContentDescriptorFactory;
import basicapp.back.business.user.model.User;
import basicapp.back.util.ResourceKeyGenerator;
import basicapp.back.util.binding.Bindings;
import basicapp.front.notification.page.ExampleHtmlNotificationPage;
import basicapp.front.notification.page.UserPasswordRecoveryRequestHtmlNotificationPage;
import basicapp.front.security.password.page.SecurityPasswordCreationPage;
import basicapp.front.security.password.page.SecurityPasswordResetPage;
import igloo.wicket.model.BindingModel;

public class BasicApplicationNotificationContentDescriptorFactoryImpl
		extends AbstractNotificationContentDescriptorFactory
		implements IBasicApplicationNotificationContentDescriptorFactory {
	
	@Autowired
	public BasicApplicationNotificationContentDescriptorFactoryImpl(IWicketContextProvider contextProvider) {
		super(contextProvider);
	}

	@Override
	public INotificationContentDescriptor example(User user, Instant instant) {
		return new AbstractSimpleWicketNotificationDescriptor("notification.panel.example") {
			@Override
			public Object getSubjectParameter() {
				return GenericEntityModel.of(user);
			}
			@Override
			public Iterable<?> getSubjectPositionalParameters() {
				return List.of(Date.from(instant));
			}
			@Override
			public Page createPage(Locale locale) {
				return new ExampleHtmlNotificationPage(GenericEntityModel.of(user), Model.of(instant), Model.of(locale));
			}
			@Override
			public Class<? extends Component> getComponentClass() {
				return ExampleHtmlNotificationPage.class;
			}
		};
	}

	@Override
	public INotificationContentDescriptor userPasswordRecoveryRequest(User user, Instant instant) {
		ITwoParameterLinkDescriptorMapper<? extends IPageLinkGenerator, User, String> mapper;
		
		switch (user.getPasswordRecoveryRequest().getType()) {
		case CREATION:
			mapper = SecurityPasswordCreationPage.MAPPER;
			break;
		case RESET:
			mapper = SecurityPasswordResetPage.MAPPER;
			break;
		default:
			throw new IllegalStateException("Recovery request type unknown.");
		}
		
		ResourceKeyGenerator keyGenerator = ResourceKeyGenerator.of("notification.panel.user.password.recovery.request")
			.append(user.getPasswordRecoveryRequest().getType().name())
			.append(user.getPasswordRecoveryRequest().getInitiator().name());
		
		return new AbstractSimpleWicketNotificationDescriptor(keyGenerator.resourceKey()) {
			@Override
			public Object getSubjectParameter() {
				return GenericEntityModel.of(user);
			}
			@Override
			public Page createPage(Locale locale) {
				IModel<User> userModel = GenericEntityModel.of(user);
				return new UserPasswordRecoveryRequestHtmlNotificationPage<>(
					keyGenerator,
					userModel,
					userModel,
					Model.of(instant),
					mapper.map(userModel, BindingModel.of(userModel, Bindings.user().passwordRecoveryRequest().token())),
					Model.of(locale)
				);
			}
			@Override
			public Class<? extends Component> getComponentClass() {
				return UserPasswordRecoveryRequestHtmlNotificationPage.class;
			}
		};
	}

}

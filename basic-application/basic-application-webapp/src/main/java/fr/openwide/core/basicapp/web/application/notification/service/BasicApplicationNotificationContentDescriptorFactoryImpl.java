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
import fr.openwide.core.basicapp.web.application.notification.component.ExampleHtmlNotificationPanel;
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
			public IModel<?> getSubjectModelParameter() {
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

}

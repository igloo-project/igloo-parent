package fr.openwide.core.basicapp.web.application.notification.service;

import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Callable;

import org.apache.wicket.Component;
import org.apache.wicket.model.Model;
import org.springframework.stereotype.Service;

import fr.openwide.core.basicapp.core.business.notification.service.IBasicApplicationNotificationPanelRendererService;
import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.web.application.BasicApplicationApplication;
import fr.openwide.core.basicapp.web.application.notification.component.ExampleHtmlNotificationPanel;
import fr.openwide.core.wicket.more.model.GenericEntityModel;
import fr.openwide.core.wicket.more.notification.service.AbstractNotificationPanelRendererServiceImpl;

@Service("BasicApplicationNotificationPanelRendererService")
public class BasicApplicationNotificationPanelRendererServiceImpl extends AbstractNotificationPanelRendererServiceImpl
		implements IBasicApplicationNotificationPanelRendererService {
	
	@Override
	public String renderExampleNotificationPanel(final User user, final Date date) {
		Callable<Component> component = new Callable<Component>() {
			@Override
			public Component call() throws Exception {
				return new ExampleHtmlNotificationPanel("htmlPanel", GenericEntityModel.of(user), Model.of(date));
			}
		};
		
		return renderComponent(component, getLocale(user));
	}
	
	private Locale getLocale(User user) {
		return configurer.toAvailableLocale(user != null ? user.getLocale() : null);
	}
	
	@Override
	protected String getApplicationName() {
		return BasicApplicationApplication.NAME;
	}

}

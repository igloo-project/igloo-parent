package fr.openwide.core.wicket.more.notification.listener;

import org.apache.wicket.Component;
import org.apache.wicket.application.IComponentInitializationListener;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.spring.injection.annot.SpringBean;

import fr.openwide.core.wicket.more.notification.service.IHtmlNotificationCssService;

public class HtmlNotificationComponentCssClassHandler implements IComponentInitializationListener {

	@SpringBean
	private IHtmlNotificationCssService cssService;

	public HtmlNotificationComponentCssClassHandler() {
		Injector.get().inject(this);
	}

	@Override
	public void onInitialize(Component component) {
		if (cssService.hasRegistry(component.getVariation())) {
			component.add(new HtmlNotificationComponentCssClassHandlerBehavior(cssService));
		}
	}
}

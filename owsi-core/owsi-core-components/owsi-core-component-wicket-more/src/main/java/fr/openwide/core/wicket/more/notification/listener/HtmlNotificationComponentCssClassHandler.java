package fr.openwide.core.wicket.more.notification.listener;

import org.apache.wicket.Component;
import org.apache.wicket.application.IComponentInitializationListener;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.util.StringUtils;

import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.wicket.more.notification.service.IHtmlNotificationCssService;

public class HtmlNotificationComponentCssClassHandler implements IComponentInitializationListener {
	
	private static final String CSS_STYLE_ATTRIBUTE = "style";

	@SpringBean
	private IHtmlNotificationCssService cssService;

	public HtmlNotificationComponentCssClassHandler() {
		Injector.get().inject(this);
	}

	@Override
	public void onInitialize(Component component) {
		if (cssService.hasRegistry(component.getVariation())) {
			component.add(new Behavior() {
				private static final long serialVersionUID = 1L;
				
				@Override
				public void onComponentTag(Component component, ComponentTag tag) {
					try {
						IHtmlNotificationCssService.IHtmlNotificationCssRegistry cssRegistry = cssService.getRegistry(component.getVariation());
						if (cssRegistry != null) {
							String appendedStyle = cssRegistry.getStyle(tag);
							if (StringUtils.hasText(appendedStyle)) {
								tag.append(CSS_STYLE_ATTRIBUTE, appendedStyle, "");
							}
						}
					} catch (ServiceException e) {
						throw new IllegalStateException(e);
					}
				}
			});
		}
	}

}

package fr.openwide.core.wicket.more.notification.listener;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.springframework.util.StringUtils;

import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.wicket.more.notification.service.IHtmlNotificationCssService;

public class HtmlNotificationComponentCssClassHandlerBehavior extends Behavior {

	private static final long serialVersionUID = -2594977749523035835L;

	private static final String CSS_STYLE_ATTRIBUTE = "style";
	
	private IHtmlNotificationCssService cssService;
	
	public HtmlNotificationComponentCssClassHandlerBehavior(IHtmlNotificationCssService cssService) {
		super();
		this.cssService = cssService;
	}
	
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
}

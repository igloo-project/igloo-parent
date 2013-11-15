package fr.openwide.core.wicket.more.notification.service;

import org.apache.wicket.markup.ComponentTag;

public interface IHtmlNotificationCssService {
	
	IHtmlNotificationCssRegistry getRegistry(String componentVariation);
	
	void registerStyles(String componentVariation, IHtmlNotificationCssRegistry registry);
	
	interface IHtmlNotificationCssRegistry {
		
		String getStyle(ComponentTag tag);
	
	}
}

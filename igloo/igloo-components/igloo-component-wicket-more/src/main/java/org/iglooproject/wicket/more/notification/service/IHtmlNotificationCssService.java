package org.iglooproject.wicket.more.notification.service;

import org.apache.wicket.markup.ComponentTag;
import org.jsoup.nodes.Node;

import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.wicket.more.css.lesscss.LessCssResourceReference;

public interface IHtmlNotificationCssService {
	
	void registerStyles(String componentVariation, LessCssResourceReference cssResourceReference) throws ServiceException;
	
	boolean hasRegistry(String componentVariation);
	
	IHtmlNotificationCssRegistry getRegistry(String componentVariation) throws ServiceException;
	
	interface IHtmlNotificationCssRegistry {
		
		String getStyle(ComponentTag tag);
		
		String getStyle(Node node);
	
	}
}
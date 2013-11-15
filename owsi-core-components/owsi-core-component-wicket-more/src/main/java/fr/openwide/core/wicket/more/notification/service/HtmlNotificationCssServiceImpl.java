package fr.openwide.core.wicket.more.notification.service;

import java.util.Map;

import com.google.common.collect.Maps;

public class HtmlNotificationCssServiceImpl implements IHtmlNotificationCssService {
	
	private final Map<String, IHtmlNotificationCssRegistry> registries = Maps.newHashMap();
	
	@Override
	public IHtmlNotificationCssRegistry getRegistry(String componentVariation) {
		return registries.get(componentVariation);
	}

	@Override
	public void registerStyles(String componentVariation, IHtmlNotificationCssRegistry registry) {
		registries.put(componentVariation, registry);
	}

}

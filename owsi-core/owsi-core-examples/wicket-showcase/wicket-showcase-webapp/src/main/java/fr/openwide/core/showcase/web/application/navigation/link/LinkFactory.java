package fr.openwide.core.showcase.web.application.navigation.link;

import fr.openwide.core.wicket.more.link.factory.AbstractLinkFactory;

public final class LinkFactory extends AbstractLinkFactory {
	
	private static final LinkFactory INSTANCE = new LinkFactory();
	
	private LinkFactory() { }
	
	public static LinkFactory get() {
		return INSTANCE;
	}

}

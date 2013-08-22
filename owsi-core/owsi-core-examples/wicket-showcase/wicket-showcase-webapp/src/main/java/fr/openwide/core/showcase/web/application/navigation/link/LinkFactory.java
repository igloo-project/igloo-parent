package fr.openwide.core.showcase.web.application.navigation.link;

import fr.openwide.core.showcase.web.application.navigation.page.SignInPage;
import fr.openwide.core.wicket.more.link.descriptor.IPageLinkDescriptor;
import fr.openwide.core.wicket.more.link.factory.CoreWicketAuthenticatedApplicationLinkFactory;

public final class LinkFactory extends CoreWicketAuthenticatedApplicationLinkFactory {
	
	private static final LinkFactory INSTANCE = new LinkFactory();
	
	private LinkFactory() { }
	
	public static LinkFactory get() {
		return INSTANCE;
	}

	@Override
	public IPageLinkDescriptor signIn() {
		return builder()
				.page(SignInPage.class)
				.build();
	}
}

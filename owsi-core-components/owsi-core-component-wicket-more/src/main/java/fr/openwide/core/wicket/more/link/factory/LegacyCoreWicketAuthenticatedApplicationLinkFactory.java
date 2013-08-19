package fr.openwide.core.wicket.more.link.factory;

import fr.openwide.core.wicket.more.application.CoreWicketAuthenticatedApplication;
import fr.openwide.core.wicket.more.link.descriptor.IPageLinkDescriptor;

public class LegacyCoreWicketAuthenticatedApplicationLinkFactory
		extends CoreWicketAuthenticatedApplicationLinkFactory {
	
	private static final LegacyCoreWicketAuthenticatedApplicationLinkFactory INSTANCE = new LegacyCoreWicketAuthenticatedApplicationLinkFactory();
	
	private LegacyCoreWicketAuthenticatedApplicationLinkFactory() { }

	/**
	 * @deprecated Use {@link CoreWicketAuthenticatedApplication#getLinkFactory()}, or the getter of your own link factory, instead.
	 */
	@Deprecated
	public static LegacyCoreWicketAuthenticatedApplicationLinkFactory get() {
		return INSTANCE;
	}

	@SuppressWarnings("deprecation")
	@Override
	public IPageLinkDescriptor signIn() {
		return builder()
				.page(CoreWicketAuthenticatedApplication.get().getSignInPageClass())
				.build();
	}

}

package fr.openwide.core.wicket.more.link.factory;

import fr.openwide.core.wicket.more.application.CoreWicketApplication;

public class LegacyCoreWicketApplicationLinkFactory
		extends CoreWicketApplicationLinkFactory {
	
	private static final LegacyCoreWicketApplicationLinkFactory INSTANCE = new LegacyCoreWicketApplicationLinkFactory();
	
	private LegacyCoreWicketApplicationLinkFactory() { }

	/**
	 * @deprecated Use {@link CoreWicketApplication#getLinkFactory()}, or the getter of your own link factory, instead.
	 */
	@Deprecated
	public static LegacyCoreWicketApplicationLinkFactory get() {
		return INSTANCE;
	}

}

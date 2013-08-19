package fr.openwide.core.wicket.more.link.factory;

import fr.openwide.core.wicket.more.link.descriptor.IPageLinkDescriptor;

public abstract class CoreWicketAuthenticatedApplicationLinkFactory
		extends CoreWicketApplicationLinkFactory {
	
	public abstract IPageLinkDescriptor signIn();

}

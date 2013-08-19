package fr.openwide.core.wicket.more.link.factory;

import org.apache.wicket.Application;

import fr.openwide.core.wicket.more.link.descriptor.IPageLinkDescriptor;

public abstract class CoreWicketApplicationLinkFactory extends AbstractLinkFactory {
	
	public final IPageLinkDescriptor home() {
		return builder()
				.page(Application.get().getHomePage())
				.build();
	}

}

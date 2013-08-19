package fr.openwide.core.wicket.more.link.factory;

import fr.openwide.core.wicket.more.link.descriptor.builder.CoreLinkDescriptorBuilder;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.IBaseState;

public abstract class AbstractLinkFactory {

	protected static IBaseState builder() {
		return new CoreLinkDescriptorBuilder();
	}

}
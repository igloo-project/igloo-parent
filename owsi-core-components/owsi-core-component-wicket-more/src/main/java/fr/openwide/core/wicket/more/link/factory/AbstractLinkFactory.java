package fr.openwide.core.wicket.more.link.factory;

import fr.openwide.core.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.IBaseState;

public abstract class AbstractLinkFactory {

	protected IBaseState builder() {
		return new LinkDescriptorBuilder();
	}

}
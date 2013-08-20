package fr.openwide.core.wicket.more.link.descriptor.builder.state;

import fr.openwide.core.wicket.more.link.descriptor.ILinkDescriptor;

public interface ITerminalState<T extends ILinkDescriptor> {

	T build();

}

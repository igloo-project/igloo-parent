package fr.openwide.core.wicket.more.link.descriptor.builder.state;

import fr.openwide.core.wicket.more.link.descriptor.ILinkDescriptor;

public interface IBuildState<T extends ILinkDescriptor> {

	T build();

}

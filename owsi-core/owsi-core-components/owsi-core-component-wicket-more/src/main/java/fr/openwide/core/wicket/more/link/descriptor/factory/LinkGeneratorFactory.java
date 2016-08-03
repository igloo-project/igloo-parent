package fr.openwide.core.wicket.more.link.descriptor.factory;

import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import fr.openwide.core.wicket.more.link.descriptor.generator.ILinkGenerator;
import fr.openwide.core.wicket.more.link.descriptor.mapper.AbstractOneParameterLinkDescriptorMapper;
import fr.openwide.core.wicket.more.link.descriptor.mapper.IOneParameterLinkDescriptorMapper;

/**
 * @deprecated Instead of extending this class, implement {@link IOneParameterLinkDescriptorMapper} by extending
 * {@link AbstractOneParameterLinkDescriptorMapper} or build one such object using a {@link LinkDescriptorBuilder}.
 */
@Deprecated
public abstract class LinkGeneratorFactory<T> implements IDetachable {

	private static final long serialVersionUID = 7320937503075282012L;

	public abstract ILinkGenerator create(IModel<T> model);

	@Override
	public void detach() {
		// Rien par défaut
	}
}

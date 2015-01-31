package fr.openwide.core.wicket.more.link.descriptor.factory;

import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.link.descriptor.generator.ILinkGenerator;

public abstract class LinkGeneratorFactory<T> implements IDetachable {

	private static final long serialVersionUID = 7320937503075282012L;

	public abstract ILinkGenerator create(IModel<T> model);

	@Override
	public void detach() {
		// Rien par défaut
	}
}

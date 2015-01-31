package fr.openwide.core.wicket.more.link.descriptor.factory;

import org.apache.wicket.model.IModel;
import org.bindgen.BindingRoot;

import fr.openwide.core.wicket.more.link.descriptor.generator.ILinkGenerator;
import fr.openwide.core.wicket.more.model.BindingModel;

public class BindingLinkGeneratorFactory<R, T> extends LinkGeneratorFactory<R> {
	
	private static final long serialVersionUID = -1677511112381705789L;

	private final BindingRoot<? super R, T> binding;
	
	private final LinkGeneratorFactory<T> factory;

	public BindingLinkGeneratorFactory(BindingRoot<? super R, T> binding, LinkGeneratorFactory<T> factory) {
		super();
		this.binding = binding;
		this.factory = factory;
	}

	@Override
	public ILinkGenerator create(IModel<R> model) {
		return factory.create(BindingModel.of(model, binding));
	}
	
	@Override
	public void detach() {
		super.detach();
		factory.detach();
	}

}

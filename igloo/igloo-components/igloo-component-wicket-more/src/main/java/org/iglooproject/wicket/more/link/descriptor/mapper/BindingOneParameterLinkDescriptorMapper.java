package org.iglooproject.wicket.more.link.descriptor.mapper;

import org.apache.wicket.model.IModel;
import org.bindgen.BindingRoot;
import org.iglooproject.wicket.model.BindingModel;

public class BindingOneParameterLinkDescriptorMapper<L, R, T> extends AbstractOneParameterLinkDescriptorMapper<L, R> {
	
	private static final long serialVersionUID = -1677511112381705789L;

	private final BindingRoot<? super R, T> binding;
	
	private final ILinkDescriptorMapper<L, ? super IModel<T>> delegate;

	public BindingOneParameterLinkDescriptorMapper(BindingRoot<? super R, T> binding,
			ILinkDescriptorMapper<L, ? super IModel<T>> delegate) {
		super();
		this.binding = binding;
		this.delegate = delegate;
	}

	@Override
	public L map(IModel<R> model) {
		return delegate.map(BindingModel.of(model, binding));
	}
	
	@Override
	public void detach() {
		super.detach();
		delegate.detach();
	}

}

package org.iglooproject.wicket.more.link.descriptor.mapper;

import org.apache.wicket.model.IModel;

import com.google.common.base.Function;

import org.iglooproject.wicket.more.link.descriptor.generator.ILinkGenerator;
import org.iglooproject.wicket.more.model.ReadOnlyModel;

public class FunctionOneParameterLinkDescriptorMapper<R, T> extends AbstractOneParameterLinkDescriptorMapper<ILinkGenerator, R> {
	
	private static final long serialVersionUID = -1677511112381705789L;

	private final Function<? super R, T> function;
	
	private final ILinkDescriptorMapper<? extends ILinkGenerator, ? super IModel<T>> delegate;

	public FunctionOneParameterLinkDescriptorMapper(Function<? super R, T> function,
			ILinkDescriptorMapper<? extends ILinkGenerator, ? super IModel<T>> delegate) {
		super();
		this.function = function;
		this.delegate = delegate;
	}

	@Override
	public ILinkGenerator map(IModel<R> model) {
		return delegate.map(ReadOnlyModel.of(model, function));
	}
	
	@Override
	public void detach() {
		super.detach();
		delegate.detach();
	}

}

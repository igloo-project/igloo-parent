package fr.openwide.core.wicket.more.link.descriptor.mapper;

import org.apache.wicket.model.IModel;
import org.bindgen.BindingRoot;

import fr.openwide.core.wicket.more.model.BindingModel;

public class BindingOneParameterLinkDescriptorMapper<L, R, T> extends AbstractOneParameterLinkDescriptorMapper<L, R> {
	
	private static final long serialVersionUID = -1677511112381705789L;

	private final BindingRoot<? super R, T> binding;
	
	private final IOneParameterLinkDescriptorMapper<L, T> mapper;

	public BindingOneParameterLinkDescriptorMapper(BindingRoot<? super R, T> binding, IOneParameterLinkDescriptorMapper<L, T> mapper) {
		super();
		this.binding = binding;
		this.mapper = mapper;
	}

	@Override
	public L map(IModel<R> model) {
		return mapper.map(BindingModel.of(model, binding));
	}
	
	@Override
	public void detach() {
		super.detach();
		mapper.detach();
	}

}

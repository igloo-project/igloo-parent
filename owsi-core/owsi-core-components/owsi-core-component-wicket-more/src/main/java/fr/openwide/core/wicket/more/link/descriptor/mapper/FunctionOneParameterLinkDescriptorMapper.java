package fr.openwide.core.wicket.more.link.descriptor.mapper;

import org.apache.wicket.model.IModel;

import com.google.common.base.Function;

import fr.openwide.core.wicket.more.link.descriptor.generator.ILinkGenerator;
import fr.openwide.core.wicket.more.model.ReadOnlyModel;

public class FunctionOneParameterLinkDescriptorMapper<R, T> extends AbstractOneParameterLinkDescriptorMapper<ILinkGenerator, R> {
	
	private static final long serialVersionUID = -1677511112381705789L;

	private final Function<? super R, T> function;
	
	private final IOneParameterLinkDescriptorMapper<? extends ILinkGenerator, T> mapper;

	public FunctionOneParameterLinkDescriptorMapper(Function<? super R, T> function, IOneParameterLinkDescriptorMapper<? extends ILinkGenerator, T> mapper) {
		super();
		this.function = function;
		this.mapper = mapper;
	}

	@Override
	public ILinkGenerator map(IModel<R> model) {
		return mapper.map(ReadOnlyModel.of(model, function));
	}
	
	@Override
	public void detach() {
		super.detach();
		mapper.detach();
	}

}

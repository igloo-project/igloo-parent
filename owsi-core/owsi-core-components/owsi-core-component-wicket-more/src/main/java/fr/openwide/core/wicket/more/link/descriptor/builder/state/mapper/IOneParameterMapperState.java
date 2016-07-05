package fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper;

import fr.openwide.core.wicket.more.link.descriptor.builder.state.IParameterMappingState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping.IOneParameterMapperOneChosenParameterMappingState;
import fr.openwide.core.wicket.more.link.descriptor.mapper.IOneParameterLinkDescriptorMapper;

public interface IOneParameterMapperState<TLinkDescriptor, TParam1>
		extends IParameterMappingState<IOneParameterLinkDescriptorMapper<TLinkDescriptor, TParam1>>,
				IOneParameterMapperOneChosenParameterMappingState<
						IOneParameterMapperState<TLinkDescriptor, TParam1>, TParam1, TParam1
				> {
	
	<TParam2> ITwoParameterMapperState<TLinkDescriptor, TParam1, TParam2> model(Class<? super TParam2> clazz);

}

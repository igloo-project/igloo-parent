package fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper;

import fr.openwide.core.wicket.more.link.descriptor.builder.state.IParameterMappingState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping.ITwoParameterMapperOneChosenParameterMappingState;
import fr.openwide.core.wicket.more.link.descriptor.mapper.ITwoParameterLinkDescriptorMapper;

public interface ITwoParameterMapperState<TLinkDescriptor, TParam1, TParam2>
		extends IParameterMappingState<ITwoParameterLinkDescriptorMapper<TLinkDescriptor, TParam1, TParam2>> {

	<TParam3> IThreeParameterMapperState<TLinkDescriptor, TParam1, TParam2, TParam3> model(Class<? super TParam3> clazz);
	
	ITwoParameterMapperOneChosenParameterMappingState<
			ITwoParameterMapperState<TLinkDescriptor, TParam1, TParam2>, TParam1, TParam2, TParam1
			> pickFirst();
	
	ITwoParameterMapperOneChosenParameterMappingState<
			ITwoParameterMapperState<TLinkDescriptor, TParam1, TParam2>, TParam1, TParam2, TParam2
			> pickSecond();

}

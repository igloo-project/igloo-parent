package fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper;

import fr.openwide.core.wicket.more.link.descriptor.builder.state.IParameterMappingState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping.ITwoParameterMapperOneChosenParameterMappingState;
import fr.openwide.core.wicket.more.link.descriptor.mapper.ITwoParameterLinkDescriptorMapper;

public interface ITwoParameterMapperState<L, T1, T2>
		extends IParameterMappingState<ITwoParameterLinkDescriptorMapper<L, T1, T2>> {

	<T3> IThreeParameterMapperState<L, T1, T2, T3> model(Class<? super T3> clazz);
	
	ITwoParameterMapperOneChosenParameterMappingState<ITwoParameterMapperState<L, T1, T2>, T1, T2, T1> pickFirst();
	
	ITwoParameterMapperOneChosenParameterMappingState<ITwoParameterMapperState<L, T1, T2>, T1, T2, T2> pickSecond();

}

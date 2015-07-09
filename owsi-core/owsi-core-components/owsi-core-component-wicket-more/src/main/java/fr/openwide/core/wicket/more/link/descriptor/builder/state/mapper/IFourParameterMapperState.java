package fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper;

import fr.openwide.core.wicket.more.link.descriptor.builder.state.IParameterMappingState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping.IFourParameterMapperOneChosenParameterMappingState;
import fr.openwide.core.wicket.more.link.descriptor.mapper.IFourParameterLinkDescriptorMapper;

public interface IFourParameterMapperState<L, T1, T2, T3, T4>
		extends IParameterMappingState<IFourParameterLinkDescriptorMapper<L, T1, T2, T3, T4>> {
	
	IFourParameterMapperOneChosenParameterMappingState<IFourParameterMapperState<L, T1, T2, T3, T4>, T1, T2, T3, T4, T1> pickFirst();
	
	IFourParameterMapperOneChosenParameterMappingState<IFourParameterMapperState<L, T1, T2, T3, T4>, T1, T2, T3, T4, T2> pickSecond();
	
	IFourParameterMapperOneChosenParameterMappingState<IFourParameterMapperState<L, T1, T2, T3, T4>, T1, T2, T3, T4, T3> pickThird();
	
	IFourParameterMapperOneChosenParameterMappingState<IFourParameterMapperState<L, T1, T2, T3, T4>, T1, T2, T3, T4, T4> pickFourth();

}

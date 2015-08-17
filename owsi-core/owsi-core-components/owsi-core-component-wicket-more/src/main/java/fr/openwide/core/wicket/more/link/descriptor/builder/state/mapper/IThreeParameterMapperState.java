package fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper;

import fr.openwide.core.wicket.more.link.descriptor.builder.state.IParameterMappingState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping.IThreeParameterMapperOneChosenParameterMappingState;
import fr.openwide.core.wicket.more.link.descriptor.mapper.IThreeParameterLinkDescriptorMapper;

public interface IThreeParameterMapperState<L, T1, T2, T3>
		extends IParameterMappingState<IThreeParameterLinkDescriptorMapper<L, T1, T2, T3>> {
	
	<T4> IFourParameterMapperState<L, T1, T2, T3, T4> model(Class<? super T4> clazz);
	
	IThreeParameterMapperOneChosenParameterMappingState<IThreeParameterMapperState<L, T1, T2, T3>, T1, T2, T3, T1> pickFirst();
	
	IThreeParameterMapperOneChosenParameterMappingState<IThreeParameterMapperState<L, T1, T2, T3>, T1, T2, T3, T2> pickSecond();
	
	IThreeParameterMapperOneChosenParameterMappingState<IThreeParameterMapperState<L, T1, T2, T3>, T1, T2, T3, T3> pickThird();

}

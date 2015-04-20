package fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper;

import fr.openwide.core.wicket.more.link.descriptor.builder.state.IParameterMappingState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping.IOneParameterMapperOneChosenParameterMappingState;
import fr.openwide.core.wicket.more.link.descriptor.mapper.IOneParameterLinkDescriptorMapper;

public interface IOneParameterMapperState<L, T1>
		extends IParameterMappingState<IOneParameterLinkDescriptorMapper<L, T1>>,
				IOneParameterMapperOneChosenParameterMappingState<IOneParameterMapperState<L, T1>, T1, T1> {
	
	/**
	 * @deprecated Use {@link #model(Class)} instead;
	 */
	@Deprecated
	<T2> ITwoParameterMapperState<L, T1, T2> addDynamicParameter(Class<? super T2> clazz);

	<T2> ITwoParameterMapperState<L, T1, T2> model(Class<? super T2> clazz);

}

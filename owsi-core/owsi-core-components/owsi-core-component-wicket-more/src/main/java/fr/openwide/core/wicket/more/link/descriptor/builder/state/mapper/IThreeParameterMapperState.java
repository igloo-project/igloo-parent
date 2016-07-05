package fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper;

import fr.openwide.core.wicket.more.link.descriptor.builder.state.IParameterMappingState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping.IThreeParameterMapperOneChosenParameterMappingState;
import fr.openwide.core.wicket.more.link.descriptor.mapper.IThreeParameterLinkDescriptorMapper;

public interface IThreeParameterMapperState<TLinkDescriptor, TParam1, TParam2, TParam3>
		extends IParameterMappingState<IThreeParameterLinkDescriptorMapper<TLinkDescriptor, TParam1, TParam2, TParam3>> {
	
	<TParam4> IFourParameterMapperState<TLinkDescriptor, TParam1, TParam2, TParam3, TParam4> model(Class<? super TParam4> clazz);
	
	IThreeParameterMapperOneChosenParameterMappingState<
			IThreeParameterMapperState<TLinkDescriptor, TParam1, TParam2, TParam3>, TParam1, TParam2, TParam3, TParam1
			> pickFirst();
	
	IThreeParameterMapperOneChosenParameterMappingState<
			IThreeParameterMapperState<TLinkDescriptor, TParam1, TParam2, TParam3>, TParam1, TParam2, TParam3, TParam2
			> pickSecond();
	
	IThreeParameterMapperOneChosenParameterMappingState<
			IThreeParameterMapperState<TLinkDescriptor, TParam1, TParam2, TParam3>, TParam1, TParam2, TParam3, TParam3
			> pickThird();

}

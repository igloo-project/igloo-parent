package fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper;

import fr.openwide.core.wicket.more.link.descriptor.builder.state.IParameterMappingState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.mapper.mapping.IFourParameterMapperOneChosenParameterMappingState;
import fr.openwide.core.wicket.more.link.descriptor.mapper.IFourParameterLinkDescriptorMapper;

public interface IFourParameterMapperState<TLinkDescriptor, TParam1, TParam2, TParam3, TParam4>
		extends IParameterMappingState<
				IFourParameterLinkDescriptorMapper<TLinkDescriptor, TParam1, TParam2, TParam3, TParam4>
				> {
	
	IFourParameterMapperOneChosenParameterMappingState<
			IFourParameterMapperState<
					TLinkDescriptor, TParam1, TParam2, TParam3, TParam4
			>,
			TParam1, TParam2, TParam3, TParam4, TParam1
			> pickFirst();
	
	IFourParameterMapperOneChosenParameterMappingState<
			IFourParameterMapperState<
					TLinkDescriptor, TParam1, TParam2, TParam3, TParam4
			>,
			TParam1, TParam2, TParam3, TParam4, TParam2
			> pickSecond();
	
	IFourParameterMapperOneChosenParameterMappingState<
			IFourParameterMapperState<
					TLinkDescriptor, TParam1, TParam2, TParam3, TParam4
			>,
			TParam1, TParam2, TParam3, TParam4, TParam3
			> pickThird();
	
	IFourParameterMapperOneChosenParameterMappingState<
			IFourParameterMapperState<
					TLinkDescriptor, TParam1, TParam2, TParam3, TParam4
			>,
			TParam1, TParam2, TParam3, TParam4, TParam4
			> pickFourth();

}

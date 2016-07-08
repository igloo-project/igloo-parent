package fr.openwide.core.wicket.more.link.descriptor.builder.state.main;

import fr.openwide.core.wicket.more.link.descriptor.builder.state.main.generic.IGenericFourMappableParameterMainState;
import fr.openwide.core.wicket.more.link.descriptor.mapper.IFourParameterLinkDescriptorMapper;

public interface IFourMappableParameterMainState
		<
		TParam1, TParam2, TParam3, TParam4,
		TEarlyTargetDefinitionLinkDescriptor,
		TLateTargetDefinitionPageLinkDescriptor,
		TLateTargetDefinitionResourceLinkDescriptor,
		TLateTargetDefinitionImageResourceLinkDescriptor
		>
		extends IGenericFourMappableParameterMainState
				<
				IFourMappableParameterMainState
						<
						TParam1, TParam2, TParam3, TParam4,
						TEarlyTargetDefinitionLinkDescriptor,
						TLateTargetDefinitionPageLinkDescriptor,
						TLateTargetDefinitionResourceLinkDescriptor,
						TLateTargetDefinitionImageResourceLinkDescriptor
						>,
				TParam1, TParam2, TParam3, TParam4,
				IFourParameterLinkDescriptorMapper
						<
						TEarlyTargetDefinitionLinkDescriptor,
						TParam1, TParam2, TParam3, TParam4
						>,
				IFourParameterLinkDescriptorMapper
						<
						TLateTargetDefinitionPageLinkDescriptor,
						TParam1, TParam2, TParam3, TParam4
						>,
				IFourParameterLinkDescriptorMapper
						<
						TLateTargetDefinitionResourceLinkDescriptor,
						TParam1, TParam2, TParam3, TParam4
						>,
				IFourParameterLinkDescriptorMapper
						<
						TLateTargetDefinitionImageResourceLinkDescriptor,
						TParam1, TParam2, TParam3, TParam4
						>
				> {

}

package fr.openwide.core.wicket.more.link.descriptor.builder.state.main;

import fr.openwide.core.wicket.more.link.descriptor.builder.state.main.common.IMainState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.main.common.IMappableParameterDeclarationState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.main.generic.IGenericOneMappableParameterMainState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.parameter.chosen.common.IOneChosenParameterState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.terminal.IBackwardCompatibleTerminalState;
import fr.openwide.core.wicket.more.link.descriptor.mapper.IOneParameterLinkDescriptorMapper;

/**
 * A builder state with one mappable parameter from which one may:
 * <ul>
 *  <li>call any of the {@link IMainState} methods to map parameters and defined validators
 *  <li>call any of the {@link IChosenParameterState} and {@link IOneChosenParameterState} methods, allowing to
 *  reference the newly-added mappable parameters without it being entirely defined (no model was provided yet).
 *  <li>add another mappable parameter by calling the {@link #model(Class)} method.
 *  <li>end the build with one of the {@link IBackwardCompatibleTerminalState} methods.
 * </ul>
 */
public interface IOneMappableParameterMainState
		<
		TParam1,
		TEarlyTargetDefinitionLinkDescriptor,
		TLateTargetDefinitionPageLinkDescriptor,
		TLateTargetDefinitionResourceLinkDescriptor,
		TLateTargetDefinitionImageResourceLinkDescriptor
		>
		extends IGenericOneMappableParameterMainState
						<
						IOneMappableParameterMainState
								<
								TParam1,
								TEarlyTargetDefinitionLinkDescriptor,
								TLateTargetDefinitionPageLinkDescriptor,
								TLateTargetDefinitionResourceLinkDescriptor,
								TLateTargetDefinitionImageResourceLinkDescriptor
								>,
						TParam1,
						IOneParameterLinkDescriptorMapper
								<
								TEarlyTargetDefinitionLinkDescriptor,
								TParam1
								>,
						IOneParameterLinkDescriptorMapper
								<
								TLateTargetDefinitionPageLinkDescriptor,
								TParam1
								>,
						IOneParameterLinkDescriptorMapper
								<
								TLateTargetDefinitionResourceLinkDescriptor,
								TParam1
								>,
						IOneParameterLinkDescriptorMapper
								<
								TLateTargetDefinitionImageResourceLinkDescriptor,
								TParam1
								>
						>,
				IMappableParameterDeclarationState {
	
	@Override
	<TParam2> ITwoMappableParameterMainState<
			TParam1, TParam2,
			TEarlyTargetDefinitionLinkDescriptor,
			TLateTargetDefinitionPageLinkDescriptor,
			TLateTargetDefinitionResourceLinkDescriptor,
			TLateTargetDefinitionImageResourceLinkDescriptor
			> model(Class<? super TParam2> clazz);

}

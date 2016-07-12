package fr.openwide.core.wicket.more.link.descriptor.builder.state.main;

import fr.openwide.core.wicket.more.link.descriptor.builder.state.main.common.IMainState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.main.generic.IGenericFourMappableParameterMainState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.parameter.choice.nonechosen.IFourOrMoreMappableParameterNoneChosenChoiceState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.parameter.chosen.common.IChosenParameterState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.terminal.IBackwardCompatibleTerminalState;
import fr.openwide.core.wicket.more.link.descriptor.mapper.IFourParameterLinkDescriptorMapper;

/**
 * A builder state with four mappable parameters from which one may:
 * <ul>
 *  <li>call any of the {@link IMainState} methods to map parameters and defined validators
 *  <li>call {@link #pickFirst()} or {@link #pickSecond()} (etc.) to choose among mappable parameters and then use any
 *  of the {@link IChosenParameterState} methods, allowing to reference these mappable parameters without them being
 *  entirely defined (no model was provided yet).
 *  <li>end the build with one of the {@link IBackwardCompatibleTerminalState} methods.
 * </ul>
 */
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
				>,
		IFourOrMoreMappableParameterNoneChosenChoiceState {

}

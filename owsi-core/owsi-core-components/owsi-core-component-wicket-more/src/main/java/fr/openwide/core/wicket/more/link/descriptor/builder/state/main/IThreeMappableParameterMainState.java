package fr.openwide.core.wicket.more.link.descriptor.builder.state.main;

import fr.openwide.core.wicket.more.link.descriptor.builder.state.main.common.IMainState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.main.common.IMappableParameterDeclarationState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.main.generic.IGenericThreeMappableParameterMainState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.parameter.chosen.common.IChosenParameterState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.terminal.IBackwardCompatibleTerminalState;
import fr.openwide.core.wicket.more.link.descriptor.mapper.IThreeParameterLinkDescriptorMapper;

/**
 * A builder state with three mappable parameters from which one may:
 * <ul>
 *  <li>call any of the {@link IMainState} methods to map parameters and defined validators
 *  <li>call {@link #pickFirst()} or {@link #pickSecond()} (etc.) to choose among mappable parameters and then use any
 *  of the {@link IChosenParameterState} methods, allowing to reference these mappable parameters without them being
 *  entirely defined (no model was provided yet).
 *  <li>add another mappable parameter by calling the {@link #model(Class)} method.
 *  <li>end the build with one of the {@link IBackwardCompatibleTerminalState} methods.
 * </ul>
 */
public interface IThreeMappableParameterMainState
		<
		TParam1, TParam2, TParam3,
		TEarlyTargetDefinitionLinkDescriptor,
		TLateTargetDefinitionPageLinkDescriptor,
		TLateTargetDefinitionResourceLinkDescriptor,
		TLateTargetDefinitionImageResourceLinkDescriptor
		>
		extends IGenericThreeMappableParameterMainState
						<
						IThreeMappableParameterMainState
								<
								TParam1, TParam2, TParam3,
								TEarlyTargetDefinitionLinkDescriptor,
								TLateTargetDefinitionPageLinkDescriptor,
								TLateTargetDefinitionResourceLinkDescriptor,
								TLateTargetDefinitionImageResourceLinkDescriptor
								>,
						TParam1, TParam2, TParam3,
						IThreeParameterLinkDescriptorMapper
								<
								TEarlyTargetDefinitionLinkDescriptor,
								TParam1, TParam2, TParam3
								>,
						IThreeParameterLinkDescriptorMapper
								<
								TLateTargetDefinitionPageLinkDescriptor,
								TParam1, TParam2, TParam3
								>,
						IThreeParameterLinkDescriptorMapper
								<
								TLateTargetDefinitionResourceLinkDescriptor,
								TParam1, TParam2, TParam3
								>,
						IThreeParameterLinkDescriptorMapper
								<
								TLateTargetDefinitionImageResourceLinkDescriptor,
								TParam1, TParam2, TParam3
								>
						>,
				IMappableParameterDeclarationState {
	
	@Override
	<TParam4> IFourMappableParameterMainState<
			TParam1, TParam2, TParam3, TParam4,
			TEarlyTargetDefinitionLinkDescriptor,
			TLateTargetDefinitionPageLinkDescriptor,
			TLateTargetDefinitionResourceLinkDescriptor,
			TLateTargetDefinitionImageResourceLinkDescriptor
			> model(Class<? super TParam4> clazz);

}

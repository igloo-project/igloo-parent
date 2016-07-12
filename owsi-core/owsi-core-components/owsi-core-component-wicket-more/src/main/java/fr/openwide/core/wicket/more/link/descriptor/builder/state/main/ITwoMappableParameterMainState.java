package fr.openwide.core.wicket.more.link.descriptor.builder.state.main;

import fr.openwide.core.wicket.more.link.descriptor.builder.state.main.common.IMainState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.main.common.IMappableParameterDeclarationState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.main.generic.IGenericTwoMappableParameterMainState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.parameter.chosen.common.IChosenParameterState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.terminal.IBackwardCompatibleTerminalState;
import fr.openwide.core.wicket.more.link.descriptor.mapper.ITwoParameterLinkDescriptorMapper;

/**
 * A builder state with two mappable parameters from which one may:
 * <ul>
 *  <li>call any of the {@link IMainState} methods to map parameters and defined validators
 *  <li>call {@link #pickFirst()} or {@link #pickSecond()} to choose among mappable parameters and then use any of the
 *  {@link IChosenParameterState} methods, allowing to reference these mappable parameters without them being entirely
 *  defined (no model was provided yet).
 *  <li>add another mappable parameter by calling the {@link #model(Class)} method.
 *  <li>end the build with one of the {@link IBackwardCompatibleTerminalState} methods.
 * </ul>
 */
public interface ITwoMappableParameterMainState
		<
		TParam1, TParam2,
		TEarlyTargetDefinitionLinkDescriptor,
		TLateTargetDefinitionPageLinkDescriptor,
		TLateTargetDefinitionResourceLinkDescriptor,
		TLateTargetDefinitionImageResourceLinkDescriptor
		>
		extends IGenericTwoMappableParameterMainState
						<
						ITwoMappableParameterMainState
								<
								TParam1, TParam2,
								TEarlyTargetDefinitionLinkDescriptor,
								TLateTargetDefinitionPageLinkDescriptor,
								TLateTargetDefinitionResourceLinkDescriptor,
								TLateTargetDefinitionImageResourceLinkDescriptor
								>,
						TParam1, TParam2,
						ITwoParameterLinkDescriptorMapper
								<
								TEarlyTargetDefinitionLinkDescriptor,
								TParam1, TParam2
								>,
						ITwoParameterLinkDescriptorMapper
								<
								TLateTargetDefinitionPageLinkDescriptor,
								TParam1, TParam2
								>,
						ITwoParameterLinkDescriptorMapper
								<
								TLateTargetDefinitionResourceLinkDescriptor,
								TParam1, TParam2
								>,
						ITwoParameterLinkDescriptorMapper
								<
								TLateTargetDefinitionImageResourceLinkDescriptor,
								TParam1, TParam2
								>
						>,
				IMappableParameterDeclarationState {

	@Override
	<TParam3> IThreeMappableParameterMainState<
			TParam1, TParam2, TParam3,
			TEarlyTargetDefinitionLinkDescriptor,
			TLateTargetDefinitionPageLinkDescriptor,
			TLateTargetDefinitionResourceLinkDescriptor,
			TLateTargetDefinitionImageResourceLinkDescriptor
			> model(Class<? super TParam3> clazz);

}

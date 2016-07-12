package fr.openwide.core.wicket.more.link.descriptor.builder.state.main;

import fr.openwide.core.wicket.more.link.descriptor.builder.state.main.common.IMainState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.main.common.IMappableParameterDeclarationState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.terminal.IBackwardCompatibleTerminalState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.terminal.ILateTargetDefinitionTerminalState;
import fr.openwide.core.wicket.more.link.descriptor.mapper.IOneParameterLinkDescriptorMapper;
import fr.openwide.core.wicket.more.link.descriptor.mapper.ITwoParameterLinkDescriptorMapper;

/**
 * An initial builder state from which one may:
 * <ul>
 *  <li>build a LinkDescriptor directly, by calling any of the {@link IMainState} methods and then any of the
 *  {@link ILateTargetDefinitionTerminalState} method.
 *  <li>start building a LinkDescriptorMapper (for example {@link IOneParameterLinkDescriptorMapper} or
 *  {@link ITwoParameterLinkDescriptorMapper}), by calling the {@link #model(Class)} method.
 * </ul>
 */
public interface INoMappableParameterMainState
		<
		TEarlyTargetDefinitionLinkDescriptor,
		TLateTargetDefinitionPageLinkDescriptor,
		TLateTargetDefinitionResourceLinkDescriptor,
		TLateTargetDefinitionImageResourceLinkDescriptor
		>
		extends IMainState
						<
						INoMappableParameterMainState
								<
								TEarlyTargetDefinitionLinkDescriptor,
								TLateTargetDefinitionPageLinkDescriptor,
								TLateTargetDefinitionResourceLinkDescriptor,
								TLateTargetDefinitionImageResourceLinkDescriptor
								>
						>,
				IMappableParameterDeclarationState,
				IBackwardCompatibleTerminalState
						<
						TEarlyTargetDefinitionLinkDescriptor,
						TLateTargetDefinitionPageLinkDescriptor,
						TLateTargetDefinitionResourceLinkDescriptor,
						TLateTargetDefinitionImageResourceLinkDescriptor
						> {

	@Override
	<TParam1> IOneMappableParameterMainState<
			TParam1,
			TEarlyTargetDefinitionLinkDescriptor,
			TLateTargetDefinitionPageLinkDescriptor,
			TLateTargetDefinitionResourceLinkDescriptor,
			TLateTargetDefinitionImageResourceLinkDescriptor
			> model(Class<? super TParam1> clazz);

}

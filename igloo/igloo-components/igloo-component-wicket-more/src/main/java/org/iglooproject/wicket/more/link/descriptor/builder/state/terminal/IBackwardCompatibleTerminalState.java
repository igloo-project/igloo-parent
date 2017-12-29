package org.iglooproject.wicket.more.link.descriptor.builder.state.terminal;

import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;


/**
 * A terminal state that will work for clients using either the deprecated early target definition
 * ({@link IEarlyTargetDefinitionTerminalState}) or the recommended late target definition
 * ({@link ILateTargetDefinitionTerminalState}).
 * <p>Note to maintainers: in an ideal world, only the {@link TEarlyTargetDefinitionResult} parameter would be needed.
 * We need to add the three others, though, so that when a client uses the early target definition API, we make the late
 * target definition API return Void instead of link descriptors. This effectively prevents users from mixing both APIs,
 * though in an inelegant way.
 * @see {@link LinkDescriptorBuilder} for discussions about late and early target definition APIs.
 */
@SuppressWarnings("deprecation")
public interface IBackwardCompatibleTerminalState
		<
		TEarlyTargetDefinitionResult,
		TLateTargetDefinitionPageResult, TLateTargetDefinitionResourceResult, TLateTargetDefinitionImageResourceResult
		>
		extends IEarlyTargetDefinitionTerminalState<TEarlyTargetDefinitionResult>,
				ILateTargetDefinitionTerminalState
						<
						TLateTargetDefinitionPageResult,
						TLateTargetDefinitionResourceResult,
						TLateTargetDefinitionImageResourceResult
						> {

}

package fr.openwide.core.wicket.more.link.descriptor.builder.state.main.generic;

import fr.openwide.core.wicket.more.link.descriptor.builder.state.main.IOneMappableParameterMainState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.main.common.IMainState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.parameter.chosen.common.IOneChosenParameterState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.terminal.IBackwardCompatibleTerminalState;

/**
 * This interface only exists so that we don't have to repeat again and again what some of the generic type parameters
 * are ({@link TSelf}, {@link TLateTargetDefinitionPageResult}, {@link TLateTargetDefinitionResourceResult}, etc.)
 * in {@link IOneMappableParameterMainState}, because it would be even more unreadable than it is now.
 */
public interface IGenericOneMappableParameterMainState
		<
		TSelf extends IMainState<TSelf>,
		TParam1,
		TEarlyTargetDefinitionResult,
		TLateTargetDefinitionPageResult,
		TLateTargetDefinitionResourceResult,
		TLateTargetDefinitionImageResourceResult
		>
		extends IMainState<TSelf>,
				IBackwardCompatibleTerminalState
						<
						TEarlyTargetDefinitionResult,
						TLateTargetDefinitionPageResult,
						TLateTargetDefinitionResourceResult,
						TLateTargetDefinitionImageResourceResult
						>,
				/* For a cleaner syntax, parameter picking is not required when there is only one parameter.
				 * We provide right away those methods that would have been available after picking.
				 */
				IOneChosenParameterState
						<
						TSelf,
						TParam1,
						TLateTargetDefinitionPageResult,
						TLateTargetDefinitionResourceResult,
						TLateTargetDefinitionImageResourceResult
						> {

}

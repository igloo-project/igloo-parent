package fr.openwide.core.wicket.more.link.descriptor.builder.state.main.generic;

import fr.openwide.core.wicket.more.link.descriptor.builder.state.main.IMainState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.main.ITwoMappableParameterMainState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.parameter.chosen.ITwoMappableParameterOneChosenParameterState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.terminal.IBackwardCompatibleTerminalState;

/**
 * This interface only exists so that we don't have to repeat again and again what some of the generic type parameters
 * are ({@link TSelf}, {@link TLateTargetDefinitionPageResult}, {@link TLateTargetDefinitionResourceResult}, etc.)
 * in {@link ITwoMappableParameterMainState}, because it would be even more unreadable than it is now.
 */
public interface IGenericTwoMappableParameterMainState
		<
		TSelf extends IMainState<TSelf>,
		TParam1, TParam2,
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
						> {
	
	ITwoMappableParameterOneChosenParameterState<
			TSelf,
			TParam1, TParam2,
			TParam1,
			TLateTargetDefinitionPageResult,
			TLateTargetDefinitionResourceResult,
			TLateTargetDefinitionImageResourceResult
			> pickFirst();
	
	ITwoMappableParameterOneChosenParameterState<
			TSelf,
			TParam1, TParam2,
			TParam2,
			TLateTargetDefinitionPageResult,
			TLateTargetDefinitionResourceResult,
			TLateTargetDefinitionImageResourceResult
			> pickSecond();

}

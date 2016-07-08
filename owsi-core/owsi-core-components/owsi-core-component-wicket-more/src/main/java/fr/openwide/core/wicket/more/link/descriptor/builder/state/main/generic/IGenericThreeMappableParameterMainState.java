package fr.openwide.core.wicket.more.link.descriptor.builder.state.main.generic;

import fr.openwide.core.wicket.more.link.descriptor.builder.state.main.IMainState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.main.IThreeMappableParameterMainState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.parameter.chosen.IThreeMappableParameterOneChosenParameterState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.terminal.IBackwardCompatibleTerminalState;

/**
 * This interface only exists so that we don't have to repeat again and again what some of the generic type parameters
 * are ({@link TSelf}, {@link TLateTargetDefinitionPageResult}, {@link TLateTargetDefinitionResourceResult}, etc.)
 * in {@link IThreeMappableParameterMainState}, because it would be even more unreadable than it is now.
 */
public interface IGenericThreeMappableParameterMainState
		<
		TSelf extends IMainState<TSelf>,
		TParam1, TParam2, TParam3,
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
	
	IThreeMappableParameterOneChosenParameterState<
			TSelf,
			TParam1, TParam2, TParam3,
			TParam1,
			TLateTargetDefinitionPageResult,
			TLateTargetDefinitionResourceResult,
			TLateTargetDefinitionImageResourceResult
			> pickFirst();
	
	IThreeMappableParameterOneChosenParameterState<
			TSelf,
			TParam1, TParam2, TParam3,
			TParam2,
			TLateTargetDefinitionPageResult,
			TLateTargetDefinitionResourceResult,
			TLateTargetDefinitionImageResourceResult
			> pickSecond();

	IThreeMappableParameterOneChosenParameterState<
			TSelf,
			TParam1, TParam2, TParam3,
			TParam3,
			TLateTargetDefinitionPageResult,
			TLateTargetDefinitionResourceResult,
			TLateTargetDefinitionImageResourceResult
			> pickThird();

}

package fr.openwide.core.wicket.more.link.descriptor.builder.state.main.generic;

import fr.openwide.core.wicket.more.link.descriptor.builder.state.main.IFourMappableParameterMainState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.main.IMainState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.parameter.chosen.IFourMappableParameterOneChosenParameterState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.terminal.IBackwardCompatibleTerminalState;

/**
 * This interface only exists so that we don't have to repeat again and again what some of the generic type parameters
 * are ({@link TSelf}, {@link TLateTargetDefinitionPageResult}, {@link TLateTargetDefinitionResourceResult}, etc.)
 * in {@link IFourMappableParameterMainState}, because it would be even more unreadable than it is now.
 */
public interface IGenericFourMappableParameterMainState
		<
		TSelf extends IMainState<TSelf>,
		TParam1, TParam2, TParam3, TParam4,
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
	
	IFourMappableParameterOneChosenParameterState<
			TSelf,
			TParam1, TParam2, TParam3, TParam4,
			TParam1,
			TLateTargetDefinitionPageResult,
			TLateTargetDefinitionResourceResult,
			TLateTargetDefinitionImageResourceResult
			> pickFirst();
	
	IFourMappableParameterOneChosenParameterState<
			TSelf,
			TParam1, TParam2, TParam3, TParam4,
			TParam2,
			TLateTargetDefinitionPageResult,
			TLateTargetDefinitionResourceResult,
			TLateTargetDefinitionImageResourceResult
			> pickSecond();

	IFourMappableParameterOneChosenParameterState<
			TSelf,
			TParam1, TParam2, TParam3, TParam4,
			TParam3,
			TLateTargetDefinitionPageResult,
			TLateTargetDefinitionResourceResult,
			TLateTargetDefinitionImageResourceResult
			> pickThird();

	IFourMappableParameterOneChosenParameterState<
			TSelf,
			TParam1, TParam2, TParam3, TParam4,
			TParam4,
			TLateTargetDefinitionPageResult,
			TLateTargetDefinitionResourceResult,
			TLateTargetDefinitionImageResourceResult
			> pickFourth();

}

package fr.openwide.core.commons.util.functional.builder.function.generic;

import com.google.common.base.Function;

import fr.openwide.core.commons.util.functional.builder.function.BooleanFunctionBuildState;
import fr.openwide.core.commons.util.functional.builder.function.DateFunctionBuildState;
import fr.openwide.core.commons.util.functional.builder.function.IntegerFunctionBuildState;
import fr.openwide.core.commons.util.functional.builder.function.StringFunctionBuildState;

public abstract class GenericIntegerFunctionBuildStateImpl
		<
		TBuildResult,
		TBooleanState extends BooleanFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TStringState>,
		TDateState extends DateFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TStringState>, 
		TIntegerState extends IntegerFunctionBuildState<TBuildResult, TBooleanState, TDateState, TIntegerState, TStringState>,
		TStringState extends StringFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TStringState>
		>
		extends GenericFunctionBuildStateImpl<TBuildResult, Integer, TBooleanState, TDateState, TIntegerState, TStringState>
		implements IntegerFunctionBuildState<TBuildResult, TBooleanState, TDateState, TIntegerState, TStringState> {
	
	@Override
	public TBuildResult withDefault(final Integer defaultValue) {
		return toInteger(new Function<Integer, Integer>() {
			@Override
			public Integer apply(Integer input) {
				return input == null ? defaultValue : input;
			}
		}).build();
	}

}

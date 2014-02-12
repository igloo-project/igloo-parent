package fr.openwide.core.commons.util.functional.builder.function.generic;

import com.google.common.base.Function;

import fr.openwide.core.commons.util.functional.builder.function.BooleanFunctionBuildState;
import fr.openwide.core.commons.util.functional.builder.function.DateFunctionBuildState;
import fr.openwide.core.commons.util.functional.builder.function.IntegerFunctionBuildState;
import fr.openwide.core.commons.util.functional.builder.function.StringFunctionBuildState;

public abstract class GenericBooleanFunctionBuildStateImpl
		<
		TBuildResult,
		TBooleanState extends BooleanFunctionBuildState<TBuildResult, TBooleanState, TDateState, TIntegerState, TStringState>,
		TDateState extends DateFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TStringState>, 
		TIntegerState extends IntegerFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TStringState>,
		TStringState extends StringFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TStringState>
		>
		extends GenericFunctionBuildStateImpl<TBuildResult, Boolean, TBooleanState, TDateState, TIntegerState, TStringState>
		implements BooleanFunctionBuildState<TBuildResult, TBooleanState, TDateState, TIntegerState, TStringState> {

	@Override
	public TBooleanState not() {
		return toBoolean(new Function<Boolean, Boolean>() {
			@Override
			public Boolean apply(Boolean input) {
				return input != null ? !input : null;
			}
		});
	}
	
	@Override
	public TBuildResult withDefault(final Boolean defaultValue) {
		return toBoolean(new Function<Boolean, Boolean>() {
			@Override
			public Boolean apply(Boolean input) {
				return input == null ? defaultValue : input;
			}
		}).build();
	}

}

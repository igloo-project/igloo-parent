package fr.openwide.core.commons.util.functional.builder.function.generic;

import java.util.Date;

import com.google.common.base.Function;

import fr.openwide.core.commons.util.functional.builder.function.BooleanFunctionBuildState;
import fr.openwide.core.commons.util.functional.builder.function.DateFunctionBuildState;
import fr.openwide.core.commons.util.functional.builder.function.IntegerFunctionBuildState;
import fr.openwide.core.commons.util.functional.builder.function.StringFunctionBuildState;

public abstract class GenericDateBuildStateImpl
		<
		TBuildResult,
		TBooleanState extends BooleanFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TStringState>,
		TDateState extends DateFunctionBuildState<TBuildResult, TBooleanState, TDateState, TIntegerState, TStringState>, 
		TIntegerState extends IntegerFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TStringState>,
		TStringState extends StringFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TStringState>
		>
		extends GenericFunctionBuildStateImpl<TBuildResult, Date, TBooleanState, TDateState, TIntegerState, TStringState>
		implements DateFunctionBuildState<TBuildResult, TBooleanState, TDateState, TIntegerState, TStringState> {
	
	@Override
	public TBuildResult withDefault(final Date defaultValue) {
		return toDate(new Function<Date, Date>() {
			@Override
			public Date apply(Date input) {
				return input == null ? defaultValue : input;
			}
		}).build();
	}

}

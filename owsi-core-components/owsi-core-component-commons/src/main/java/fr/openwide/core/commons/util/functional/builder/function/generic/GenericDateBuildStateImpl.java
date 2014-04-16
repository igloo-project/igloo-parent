package fr.openwide.core.commons.util.functional.builder.function.generic;

import java.util.Date;

import fr.openwide.core.commons.util.functional.Functions2;
import fr.openwide.core.commons.util.functional.builder.function.BooleanFunctionBuildState;
import fr.openwide.core.commons.util.functional.builder.function.DateFunctionBuildState;
import fr.openwide.core.commons.util.functional.builder.function.DoubleFunctionBuildState;
import fr.openwide.core.commons.util.functional.builder.function.IntegerFunctionBuildState;
import fr.openwide.core.commons.util.functional.builder.function.LongFunctionBuildState;
import fr.openwide.core.commons.util.functional.builder.function.StringFunctionBuildState;

public abstract class GenericDateBuildStateImpl
		<
		TBuildResult,
		TBooleanState extends BooleanFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TStringState>,
		TDateState extends DateFunctionBuildState<TBuildResult, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TStringState>, 
		TIntegerState extends IntegerFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TStringState>,
		TLongState extends LongFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TStringState>,
		TDoubleState extends DoubleFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TStringState>,
		TStringState extends StringFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TStringState>
		>
		extends GenericFunctionBuildStateImpl<TBuildResult, Date, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TStringState>
		implements DateFunctionBuildState<TBuildResult, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TStringState> {
	
	@Override
	public TBuildResult withDefault(final Date defaultValue) {
		return toDate(Functions2.defaultValue(defaultValue)).build();
	}

}

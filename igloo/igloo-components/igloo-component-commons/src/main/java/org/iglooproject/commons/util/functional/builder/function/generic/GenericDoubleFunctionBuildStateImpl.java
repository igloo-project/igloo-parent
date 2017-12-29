package org.iglooproject.commons.util.functional.builder.function.generic;

import org.iglooproject.commons.util.functional.Functions2;
import org.iglooproject.commons.util.functional.builder.function.BigDecimalFunctionBuildState;
import org.iglooproject.commons.util.functional.builder.function.BooleanFunctionBuildState;
import org.iglooproject.commons.util.functional.builder.function.DateFunctionBuildState;
import org.iglooproject.commons.util.functional.builder.function.DoubleFunctionBuildState;
import org.iglooproject.commons.util.functional.builder.function.IntegerFunctionBuildState;
import org.iglooproject.commons.util.functional.builder.function.LongFunctionBuildState;
import org.iglooproject.commons.util.functional.builder.function.StringFunctionBuildState;

public abstract class GenericDoubleFunctionBuildStateImpl
		<
		TBuildResult,
		TStateSwitcher extends FunctionBuildStateSwitcher<TBuildResult, Double, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TBigDecimalState, TStringState>,
		TBooleanState extends BooleanFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TBigDecimalState, TStringState>,
		TDateState extends DateFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TBigDecimalState, TStringState>, 
		TIntegerState extends IntegerFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TBigDecimalState, TStringState>,
		TLongState extends LongFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TBigDecimalState, TStringState>,
		TDoubleState extends DoubleFunctionBuildState<TBuildResult, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TBigDecimalState, TStringState>,
		TBigDecimalState extends BigDecimalFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TBigDecimalState, TStringState>,
		TStringState extends StringFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TBigDecimalState, TStringState>
		>
		extends GenericNumberFunctionBuildStateImpl<TBuildResult, Double, TStateSwitcher, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TBigDecimalState, TStringState>
		implements DoubleFunctionBuildState<TBuildResult, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TBigDecimalState, TStringState> {
	
	@Override
	public TBuildResult withDefault(final Double defaultValue) {
		return toDouble(Functions2.defaultValue(defaultValue)).build();
	}

}

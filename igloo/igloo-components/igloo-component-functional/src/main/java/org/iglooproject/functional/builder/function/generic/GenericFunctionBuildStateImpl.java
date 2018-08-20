package org.iglooproject.functional.builder.function.generic;

import java.math.BigDecimal;
import java.util.Date;

import org.iglooproject.functional.Function2;
import org.iglooproject.functional.builder.function.BigDecimalFunctionBuildState;
import org.iglooproject.functional.builder.function.BooleanFunctionBuildState;
import org.iglooproject.functional.builder.function.DateFunctionBuildState;
import org.iglooproject.functional.builder.function.DoubleFunctionBuildState;
import org.iglooproject.functional.builder.function.FunctionBuildState;
import org.iglooproject.functional.builder.function.IntegerFunctionBuildState;
import org.iglooproject.functional.builder.function.LongFunctionBuildState;
import org.iglooproject.functional.builder.function.StringFunctionBuildState;

public abstract class GenericFunctionBuildStateImpl
		<
		TBuildResult,
		TCurrentType,
		TStateSwitcher extends FunctionBuildStateSwitcher<TBuildResult, TCurrentType, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TBigDecimalState, TStringState>,
		TBooleanState extends BooleanFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TBigDecimalState, TStringState>,
		TDateState extends DateFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TBigDecimalState, TStringState>, 
		TIntegerState extends IntegerFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TBigDecimalState, TStringState>,
		TLongState extends LongFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TBigDecimalState, TStringState>,
		TDoubleState extends DoubleFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TBigDecimalState, TStringState>,
		TBigDecimalState extends BigDecimalFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TBigDecimalState, TStringState>,
		TStringState extends StringFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TBigDecimalState, TStringState>
		>
		implements FunctionBuildState<TBuildResult, TCurrentType, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TBigDecimalState, TStringState> {
	
	protected abstract TStateSwitcher getStateSwitcher();

	@Override
	public TStringState toString(Function2<? super TCurrentType, String> function) {
		return getStateSwitcher().toString(function);
	}

	@Override
	public TIntegerState toInteger(Function2<? super TCurrentType, Integer> function) {
		return getStateSwitcher().toInteger(function);
	}

	@Override
	public TLongState toLong(Function2<? super TCurrentType, Long> function) {
		return getStateSwitcher().toLong(function);
	}

	@Override
	public TDoubleState toDouble(Function2<? super TCurrentType, Double> function) {
		return getStateSwitcher().toDouble(function);
	}
	
	@Override
	public TBigDecimalState toBigDecimal(Function2<? super TCurrentType, BigDecimal> function) {
		return getStateSwitcher().toBigDecimal(function);
	}

	@Override
	public TDateState toDate(Function2<? super TCurrentType, ? extends Date> function) {
		return getStateSwitcher().toDate(function);
	}

	@Override
	public TBooleanState toBoolean(Function2<? super TCurrentType, Boolean> function) {
		return getStateSwitcher().toBoolean(function);
	}

	@Override
	public TBuildResult build() {
		return getStateSwitcher().build();
	}

}
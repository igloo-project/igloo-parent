package fr.openwide.core.commons.util.functional.builder.function.generic;

import java.util.Date;

import com.google.common.base.Function;

import fr.openwide.core.commons.util.functional.builder.function.BooleanFunctionBuildState;
import fr.openwide.core.commons.util.functional.builder.function.DateFunctionBuildState;
import fr.openwide.core.commons.util.functional.builder.function.DoubleFunctionBuildState;
import fr.openwide.core.commons.util.functional.builder.function.FunctionBuildState;
import fr.openwide.core.commons.util.functional.builder.function.IntegerFunctionBuildState;
import fr.openwide.core.commons.util.functional.builder.function.LongFunctionBuildState;
import fr.openwide.core.commons.util.functional.builder.function.StringFunctionBuildState;

public abstract class GenericFunctionBuildStateImpl
		<
		TBuildResult,
		TCurrentType,
		TStateSwitcher extends FunctionBuildStateSwitcher<TBuildResult, TCurrentType, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TStringState>,
		TBooleanState extends BooleanFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TStringState>,
		TDateState extends DateFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TStringState>, 
		TIntegerState extends IntegerFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TStringState>,
		TLongState extends LongFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TStringState>,
		TDoubleState extends DoubleFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TStringState>,
		TStringState extends StringFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TStringState>
		>
		implements FunctionBuildState<TBuildResult, TCurrentType, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TStringState> {
	
	protected abstract TStateSwitcher getStateSwitcher();

	@Override
	public TStringState toString(Function<? super TCurrentType, String> function) {
		return getStateSwitcher().toString(function);
	}

	@Override
	public TIntegerState toInteger(Function<? super TCurrentType, Integer> function) {
		return getStateSwitcher().toInteger(function);
	}

	@Override
	public TLongState toLong(Function<? super TCurrentType, Long> function) {
		return getStateSwitcher().toLong(function);
	}

	@Override
	public TDoubleState toDouble(Function<? super TCurrentType, Double> function) {
		return getStateSwitcher().toDouble(function);
	}

	@Override
	public TDateState toDate(Function<? super TCurrentType, ? extends Date> function) {
		return getStateSwitcher().toDate(function);
	}

	@Override
	public TBooleanState toBoolean(Function<? super TCurrentType, Boolean> function) {
		return getStateSwitcher().toBoolean(function);
	}

	@Override
	public TBuildResult build() {
		return getStateSwitcher().build();
	}

}
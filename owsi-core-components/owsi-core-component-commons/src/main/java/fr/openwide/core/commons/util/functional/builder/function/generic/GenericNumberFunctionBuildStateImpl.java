package fr.openwide.core.commons.util.functional.builder.function.generic;

import java.math.BigDecimal;

import com.google.common.base.Function;

import fr.openwide.core.commons.util.functional.builder.function.BigDecimalFunctionBuildState;
import fr.openwide.core.commons.util.functional.builder.function.BooleanFunctionBuildState;
import fr.openwide.core.commons.util.functional.builder.function.DateFunctionBuildState;
import fr.openwide.core.commons.util.functional.builder.function.DoubleFunctionBuildState;
import fr.openwide.core.commons.util.functional.builder.function.IntegerFunctionBuildState;
import fr.openwide.core.commons.util.functional.builder.function.LongFunctionBuildState;
import fr.openwide.core.commons.util.functional.builder.function.NumberFunctionBuildState;
import fr.openwide.core.commons.util.functional.builder.function.StringFunctionBuildState;

public abstract class GenericNumberFunctionBuildStateImpl
		<
		TBuildResult,
		TNumber extends Number,
		TStateSwitcher extends FunctionBuildStateSwitcher<TBuildResult, TNumber, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TBigDecimalState, TStringState>,
		TBooleanState extends BooleanFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TBigDecimalState, TStringState>,
		TDateState extends DateFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TBigDecimalState, TStringState>, 
		TIntegerState extends IntegerFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TBigDecimalState, TStringState>,
		TLongState extends LongFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TBigDecimalState, TStringState>,
		TDoubleState extends DoubleFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TBigDecimalState, TStringState>,
		TBigDecimalState extends BigDecimalFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TBigDecimalState, TStringState>,
		TStringState extends StringFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TBigDecimalState, TStringState>
		>
		extends GenericFunctionBuildStateImpl<TBuildResult, TNumber, TStateSwitcher, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TBigDecimalState, TStringState>
		implements NumberFunctionBuildState<TBuildResult, TNumber, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TBigDecimalState, TStringState> {
	
	@Override
	public TIntegerState toInteger() {
		return toInteger(new Function<TNumber, Integer>() {
			@Override
			public Integer apply(TNumber input) {
				return input == null ? null : input.intValue();
			}
		});
	}
	
	@Override
	public TLongState toLong() {
		return toLong(new Function<TNumber, Long>() {
			@Override
			public Long apply(TNumber input) {
				return input == null ? null : input.longValue();
			}
		});
	}
	
	@Override
	public TDoubleState toDouble() {
		return toDouble(new Function<TNumber, Double>() {
			@Override
			public Double apply(TNumber input) {
				return input == null ? null : input.doubleValue();
			}
		});
	}
	
	@Override
	public TBigDecimalState toBigDecimal() {
		return toBigDecimal(new Function<TNumber, BigDecimal>() {
			@Override
			public BigDecimal apply(TNumber input) {
				return input == null ? null : new BigDecimal(input.doubleValue());
			}
		});
	}

}

package fr.openwide.core.commons.util.functional.builder.function;

import java.util.Date;

import com.google.common.base.Function;

public interface FunctionBuildState
		<
		TBuildResult,
		TCurrentType,
		TBooleanState extends BooleanFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TStringState>,
		TDateState extends DateFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TStringState>, 
		TIntegerState extends IntegerFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TStringState>,
		TStringState extends StringFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TStringState>
		> {
	
	TStringState toString(Function<? super TCurrentType, String> function);
	
	TIntegerState toInteger(Function<? super TCurrentType, Integer> function);
	
	TDateState toDate(Function<? super TCurrentType, ? extends Date> function);
	
	TBooleanState toBoolean(Function<? super TCurrentType, Boolean> function);
	
	TBuildResult build();
	
	TBuildResult withDefault(TCurrentType defaultValue);

}

package fr.openwide.core.commons.util.functional.builder.function;


public interface NumberFunctionBuildState
		<
		TBuildResult,
		TNumber extends Number,
		TBooleanState extends BooleanFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TStringState>,
		TDateState extends DateFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TStringState>, 
		TIntegerState extends IntegerFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TStringState>,
		TLongState extends LongFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TStringState>,
		TDoubleState extends DoubleFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TStringState>,
		TStringState extends StringFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TStringState>
		>
		extends FunctionBuildState<TBuildResult, TNumber, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TStringState> {

	TLongState toLong();
	
	TIntegerState toInteger();
	
	TDoubleState toDouble();

}

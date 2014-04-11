package fr.openwide.core.commons.util.functional.builder.function;


public interface BooleanFunctionBuildState
		<
		TBuildResult,
		TBooleanState extends BooleanFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TStringState>,
		TDateState extends DateFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TStringState>, 
		TIntegerState extends IntegerFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TStringState>,
		TStringState extends StringFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TStringState>
		>
		extends FunctionBuildState<TBuildResult, Boolean, TBooleanState, TDateState, TIntegerState, TStringState> {
	
	TBooleanState not();
	
}

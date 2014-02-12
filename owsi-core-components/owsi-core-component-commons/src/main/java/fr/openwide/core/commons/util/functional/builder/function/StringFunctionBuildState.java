package fr.openwide.core.commons.util.functional.builder.function;

import com.google.common.base.CharMatcher;

public interface StringFunctionBuildState
		<
		TBuildResult,
		TBooleanState extends BooleanFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TStringState>,
		TDateState extends DateFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TStringState>, 
		TIntegerState extends IntegerFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TStringState>,
		TStringState extends StringFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TStringState>
		>
		extends FunctionBuildState<TBuildResult, String, TBooleanState, TDateState, TIntegerState, TStringState> {
	
	TStringState trimmed();

	/** Strips all whitespace characters from the beginning and the end of <code>charSequence</code>.
	 * <p>Exactly what is a whitespace is defined by {@link CharMatcher.WHITESPACE} ; this includes non-breaking spaces.
	 */
	TStringState stripped();
	
	/**
	 * Shorthand for .trimmed().stripped()
	 */
	TStringState cleaned();
	
	TStringState capitalized(char... delimiters);
	
	TStringState capitalizedFully(char... delimiters);

}

package fr.openwide.core.commons.util.functional.builder.function;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.CharMatcher;

public interface StringFunctionBuildState
		<
		TBuildResult,
		TBooleanState extends BooleanFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TBigDecimalState, TStringState>,
		TDateState extends DateFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TBigDecimalState, TStringState>, 
		TIntegerState extends IntegerFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TBigDecimalState, TStringState>,
		TLongState extends LongFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TBigDecimalState, TStringState>,
		TDoubleState extends DoubleFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TBigDecimalState, TStringState>,
		TBigDecimalState extends BigDecimalFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TBigDecimalState, TStringState>,
		TStringState extends StringFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TBigDecimalState, TStringState>
		>
		extends FunctionBuildState<TBuildResult, String, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TBigDecimalState, TStringState> {
	
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
	
	TStringState replaceAll(Pattern pattern, String replacement);
	
	TStringState removeAll(Pattern pattern);
	
	/**
	 * @see Matcher#group()
	 */
	TStringState extract(Pattern pattern);
	
	/**
	 * @see Matcher#group(int)
	 */
	TStringState extract(Pattern pattern, int group);

}

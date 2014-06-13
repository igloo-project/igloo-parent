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
	
	TStringState trim();

	/** Strips all whitespace characters from the beginning and the end of <code>charSequence</code>.
	 * <p>Exactly what is a whitespace is defined by {@link CharMatcher.WHITESPACE} ; this includes non-breaking spaces.
	 */
	TStringState strip();
	
	/**
	 * Shorthand for .trimmed().stripped()
	 */
	TStringState clean();
	
	TStringState capitalize(char... delimiters);
	
	TStringState capitalizeFully(char... delimiters);
	
	TStringState replaceAll(Pattern pattern, String replacement);

	TStringState replaceAll(CharMatcher charMatcher, CharSequence replacement);
	
	TStringState removeAll(Pattern pattern);

	TStringState removeAll(CharMatcher charMatcher);
	
	/**
	 * @see Matcher#group()
	 */
	TStringState extract(Pattern pattern);
	
	/**
	 * @see Matcher#group(int)
	 */
	TStringState extract(Pattern pattern, int group);

	TStringState stripLineBreaks();

}

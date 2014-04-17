package fr.openwide.core.commons.util.functional.builder.function.generic;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import com.google.common.base.CharMatcher;
import com.google.common.base.Function;
import com.google.common.base.Strings;

import fr.openwide.core.commons.util.functional.builder.function.BooleanFunctionBuildState;
import fr.openwide.core.commons.util.functional.builder.function.DateFunctionBuildState;
import fr.openwide.core.commons.util.functional.builder.function.DoubleFunctionBuildState;
import fr.openwide.core.commons.util.functional.builder.function.IntegerFunctionBuildState;
import fr.openwide.core.commons.util.functional.builder.function.LongFunctionBuildState;
import fr.openwide.core.commons.util.functional.builder.function.StringFunctionBuildState;

public abstract class GenericStringFunctionBuildStateImpl
		<
		TBuildResult,
		TStateSwitcher extends FunctionBuildStateSwitcher<TBuildResult, String, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TStringState>,
		TBooleanState extends BooleanFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TStringState>,
		TDateState extends DateFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TStringState>, 
		TIntegerState extends IntegerFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TStringState>,
		TLongState extends LongFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TStringState>,
		TDoubleState extends DoubleFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TStringState>,
		TStringState extends StringFunctionBuildState<TBuildResult, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TStringState>
		>
		extends GenericFunctionBuildStateImpl<TBuildResult, String, TStateSwitcher, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TStringState>
		implements StringFunctionBuildState<TBuildResult, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TStringState> {
	
	private static final char[] DEFAULT_WORD_DELIMITERS =  { ' ', '\t', '\'' };
	
	private static String stripToNull(CharSequence charSequence) {
		if (charSequence == null) {
			return null;
		}
		return Strings.emptyToNull(CharMatcher.WHITESPACE.trimFrom(charSequence));
	}
	
	@Override
	public TBuildResult withDefault(final String defaultValue) {
		return toString(new Function<String, String>() {
			@Override
			public String apply(String input) {
				return StringUtils.defaultIfEmpty(input, defaultValue);
			}
		}).build();
	}
	
	@Override
	public TStringState stripped() {
		return toString(new Function<String, String>() {
			@Override
			public String apply(String input) {
				return stripToNull(input);
			}
		});
	}
	
	@Override
	public TStringState trimmed() {
		return toString(new Function<String, String>() {
			@Override
			public String apply(String input) {
				return StringUtils.trimToNull(input);
			}
		});
	}
	
	@Override
	public TStringState cleaned() {
		return toString(new Function<String, String>() {
			@Override
			public String apply(String input) {
				return stripToNull(StringUtils.trimToNull(input));
			}
		});
	}
	
	@Override
	public TStringState capitalized(char... delimiters) {
		final char[] actualDelimiters = (delimiters == null || delimiters.length == 0) ? DEFAULT_WORD_DELIMITERS : delimiters; 
		return toString(new Function<String, String>() {
			@Override
			public String apply(String input) {
				return WordUtils.capitalize(input, actualDelimiters);
			}
		});
	}
	
	@Override
	public TStringState capitalizedFully(char... delimiters) {
		final char[] actualDelimiters = (delimiters == null || delimiters.length == 0) ? DEFAULT_WORD_DELIMITERS : delimiters;
		return toString(new Function<String, String>() {
			@Override
			public String apply(String input) {
				return WordUtils.capitalizeFully(input, actualDelimiters);
			}
		});
	}

}

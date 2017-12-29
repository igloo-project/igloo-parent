package org.iglooproject.commons.util.functional.builder.function.generic;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import com.google.common.base.CharMatcher;
import com.google.common.base.Function;
import com.google.common.base.Strings;

import org.iglooproject.commons.util.functional.builder.function.BigDecimalFunctionBuildState;
import org.iglooproject.commons.util.functional.builder.function.BooleanFunctionBuildState;
import org.iglooproject.commons.util.functional.builder.function.DateFunctionBuildState;
import org.iglooproject.commons.util.functional.builder.function.DoubleFunctionBuildState;
import org.iglooproject.commons.util.functional.builder.function.IntegerFunctionBuildState;
import org.iglooproject.commons.util.functional.builder.function.LongFunctionBuildState;
import org.iglooproject.commons.util.functional.builder.function.StringFunctionBuildState;

public abstract class GenericStringFunctionBuildStateImpl
		<
		TBuildResult,
		TStateSwitcher extends FunctionBuildStateSwitcher<TBuildResult, String, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TBigDecimalState, TStringState>,
		TBooleanState extends BooleanFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TBigDecimalState, TStringState>,
		TDateState extends DateFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TBigDecimalState, TStringState>, 
		TIntegerState extends IntegerFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TBigDecimalState, TStringState>,
		TLongState extends LongFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TBigDecimalState, TStringState>,
		TDoubleState extends DoubleFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TBigDecimalState, TStringState>,
		TBigDecimalState extends BigDecimalFunctionBuildState<?, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TBigDecimalState, TStringState>,
		TStringState extends StringFunctionBuildState<TBuildResult, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TBigDecimalState, TStringState>
		>
		extends GenericFunctionBuildStateImpl<TBuildResult, String, TStateSwitcher, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TBigDecimalState, TStringState>
		implements StringFunctionBuildState<TBuildResult, TBooleanState, TDateState, TIntegerState, TLongState, TDoubleState, TBigDecimalState, TStringState> {
	
	private static final char[] DEFAULT_WORD_DELIMITERS =  { ' ', '\t', '\'' };
	
	private static String stripToNull(CharSequence charSequence) {
		if (charSequence == null) {
			return null;
		}
		return Strings.emptyToNull(CharMatcher.whitespace().trimFrom(charSequence));
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
	public TStringState strip() {
		return toString(new Function<String, String>() {
			@Override
			public String apply(String input) {
				return stripToNull(input);
			}
		});
	}
	
	@Override
	public TStringState trim() {
		return toString(new Function<String, String>() {
			@Override
			public String apply(String input) {
				return StringUtils.trimToNull(input);
			}
		});
	}
	
	@Override
	public TStringState clean() {
		return toString(new Function<String, String>() {
			@Override
			public String apply(String input) {
				return stripToNull(StringUtils.trimToNull(input));
			}
		});
	}
	
	@Override
	public TStringState capitalize(char... delimiters) {
		final char[] actualDelimiters = (delimiters == null || delimiters.length == 0) ? DEFAULT_WORD_DELIMITERS : delimiters; 
		return toString(new Function<String, String>() {
			@Override
			public String apply(String input) {
				return WordUtils.capitalize(input, actualDelimiters);
			}
		});
	}
	
	@Override
	public TStringState capitalizeFully(char... delimiters) {
		final char[] actualDelimiters = (delimiters == null || delimiters.length == 0) ? DEFAULT_WORD_DELIMITERS : delimiters;
		return toString(new Function<String, String>() {
			@Override
			public String apply(String input) {
				return WordUtils.capitalizeFully(input, actualDelimiters);
			}
		});
	}
	
	@Override
	public TStringState replaceAll(final Pattern pattern, final String replacement) {
		return toString(new Function<String, String>() {
			@Override
			public String apply(String input) {
				if (input == null) {
					return null;
				} else {
					Matcher matcher = pattern.matcher(input);
					return matcher.replaceAll(replacement);
				}
			}
		});
	}
	
	@Override
	public TStringState replaceAll(final CharMatcher charMatcher, final CharSequence replacement) {
		return toString(new Function<String, String>() {
			@Override
			public String apply(String input) {
				if (input == null) {
					return null;
				} else {
					return charMatcher.replaceFrom(input, replacement);
				}
			}
		});
	}
	
	@Override
	public TStringState removeAll(Pattern pattern) {
		return replaceAll(pattern, "");
	}
	
	@Override
	public TStringState removeAll(final CharMatcher charMatcher) {
		return toString(new Function<String, String>() {
			@Override
			public String apply(String input) {
				if (input == null) {
					return null;
				} else {
					return charMatcher.removeFrom(input);
				}
			}
		});
	}
	
	@Override
	public TStringState extract(Pattern pattern) {
		return extract(pattern, 0);
	}
	
	@Override
	public TStringState extract(final Pattern pattern, final int group) {
		return toString(new Function<String, String>() {
			@Override
			public String apply(String input) {
				if (input == null) {
					return null;
				} else {
					Matcher matcher = pattern.matcher(input);
					if (matcher.find()) {
						return matcher.group(group);
					} else {
						return null;
					}
				}
			}
		});
	}
	
	@Override
	public TStringState stripLineBreaks() {
		return toString(new Function<String, String>() {
			@Override
			public String apply(String input) {
				if (input == null) {
					return null;
				}
				
				String cleanString = input.replace(StringUtils.CR + StringUtils.LF, " ");
				cleanString = cleanString.replace(StringUtils.LF, " ");
				cleanString = cleanString.replace(StringUtils.CR, " ");
				
				return cleanString;
			}
		});
	}

}

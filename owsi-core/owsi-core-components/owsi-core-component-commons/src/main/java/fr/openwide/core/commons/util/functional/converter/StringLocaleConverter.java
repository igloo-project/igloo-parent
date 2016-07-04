package fr.openwide.core.commons.util.functional.converter;

import java.util.Locale;

import com.google.common.base.Converter;

public class StringLocaleConverter extends Converter<String, Locale> {

	private static final StringLocaleConverter INSTANCE = new StringLocaleConverter();

	public static StringLocaleConverter get() {
		return INSTANCE;
	}

	protected StringLocaleConverter() {
	}

	@Override
	protected Locale doForward(String a) {
		return Locale.forLanguageTag(a);
	}

	@Override
	protected String doBackward(Locale b) {
		return b.toLanguageTag();
	}

	/**
	 * Workaround sonar/findbugs - https://github.com/google/guava/issues/1858
	 * Guava Converter overrides only equals to add javadoc, but findbugs warns about non coherent equals/hashcode
	 * possible issue.
	 */
	@Override
	public boolean equals(Object object) {
		return super.equals(object);
	}

	/**
	 * Workaround sonar/findbugs - see #equals(Object)
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
	}

}

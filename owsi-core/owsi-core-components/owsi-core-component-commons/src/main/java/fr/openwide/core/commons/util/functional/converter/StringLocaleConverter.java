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

}

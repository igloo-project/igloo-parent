package org.iglooproject.functional.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.time.DateUtils;

public class StringDateConverter extends SerializableConverter2<String, Date> {

	private static final long serialVersionUID = 9040382196643230920L;

	private static final StringDateConverter INSTANCE = new StringDateConverter();

	private static final String PATTERN = "yyyy-MM-dd";

	public static StringDateConverter get() {
		return INSTANCE;
	}

	protected StringDateConverter() {
	}

	@Override
	protected Date doForward(String a) {
		try {
			return DateUtils.parseDate(a, Locale.ROOT, PATTERN);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Invalid date value '" + a + "'", e);
		}
	}

	@Override
	protected String doBackward(Date b) {
		return new SimpleDateFormat(PATTERN, Locale.ROOT).format(b);
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

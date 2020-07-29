package org.iglooproject.functional.converter;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public class StringBooleanConverter extends SerializableConverter2<String, Boolean> {

	private static final long serialVersionUID = -4686524165984936767L;

	private static final StringBooleanConverter INSTANCE = new StringBooleanConverter();

	private static final Set<String> trueValues = new HashSet<>(4);

	private static final Set<String> falseValues = new HashSet<>(4);

	static {
		trueValues.add("true");
		trueValues.add("on");
		trueValues.add("yes");
		trueValues.add("1");

		falseValues.add("false");
		falseValues.add("off");
		falseValues.add("no");
		falseValues.add("0");
	}

	public static StringBooleanConverter get() {
		return INSTANCE;
	}

	protected StringBooleanConverter() {
	}

	@Override
	protected Boolean doForward(String a) {
		String value = StringUtils.lowerCase(StringUtils.trim(a));
		if (trueValues.contains(value)) {
			return Boolean.TRUE;
		} else if (falseValues.contains(value)) {
			return Boolean.FALSE;
		} else {
			throw new IllegalArgumentException("Invalid boolean value '" + a + "'");
		}
	}

	@Override
	protected String doBackward(Boolean b) {
		return b.toString();
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

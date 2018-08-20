package org.iglooproject.functional.converter;

import java.math.BigDecimal;

public class StringBigDecimalConverter extends SerializableConverter2<String, BigDecimal> {

	private static final long serialVersionUID = -1296083611279109238L;

	private static final StringBigDecimalConverter INSTANCE = new StringBigDecimalConverter();

	public static StringBigDecimalConverter get() {
		return INSTANCE;
	}

	protected StringBigDecimalConverter() {
	}

	@Override
	protected BigDecimal doForward(String a) {
		return new BigDecimal(a);
	}

	@Override
	protected String doBackward(BigDecimal b) {
		return b.toPlainString();
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

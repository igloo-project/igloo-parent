package fr.openwide.core.commons.util.functional.converter;

import java.math.BigDecimal;

import com.google.common.base.Converter;

public class StringBigDecimalConverter extends Converter<String, BigDecimal> {

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

}

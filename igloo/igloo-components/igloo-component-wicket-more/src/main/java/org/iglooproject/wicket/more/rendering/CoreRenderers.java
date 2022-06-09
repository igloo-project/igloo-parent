package org.iglooproject.wicket.more.rendering;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.iglooproject.functional.Functions2;
import org.iglooproject.functional.SerializableFunction2;

import igloo.wicket.renderer.Renderer;

public final class CoreRenderers {

	private static SerializableFunction2<Locale, DecimalFormat> percentDecimalFormatFunction(final String pattern, final RoundingMode roundingMode) {
		return input -> {
			DecimalFormat df = new DecimalFormat(pattern, new DecimalFormatSymbols(input));
			df.setRoundingMode(roundingMode);
			df.setMultiplier(100);
			return df;
		};
	}

	private static final SerializableFunction2<Locale, DecimalFormat> PERCENT_FORMAT_FUNCTION = percentDecimalFormatFunction("#0.00\u00A0'%'", RoundingMode.HALF_UP);

	private static final SerializableFunction2<Locale, DecimalFormat> PERCENT_NO_SIGN_FORMAT_FUNCTION = percentDecimalFormatFunction("#0.00\u00A0", RoundingMode.HALF_UP);

	private static final Renderer<BigDecimal> PERCENT = Renderer.<BigDecimal>fromNumberFormat(PERCENT_FORMAT_FUNCTION);

	private static final Renderer<BigDecimal> PERCENT_NO_SIGN = Renderer.<BigDecimal>fromNumberFormat(PERCENT_NO_SIGN_FORMAT_FUNCTION);

	private static final SerializableFunction2<DecimalFormat, DecimalFormat> TO_RELATIVE_FORMAT_FUNCTION = input -> {
		input.setPositivePrefix("+");
		input.setNegativePrefix("-");
		return input;
	};

	private static final Renderer<BigDecimal> PERCENT_RELATIVE = Renderer.<BigDecimal>fromNumberFormat(Functions2.compose(TO_RELATIVE_FORMAT_FUNCTION, PERCENT_FORMAT_FUNCTION));

	private CoreRenderers() {
	}

	public static Renderer<BigDecimal> percent(String pattern, RoundingMode roundingMode) {
		return Renderer.<BigDecimal>fromNumberFormat(percentDecimalFormatFunction(pattern, roundingMode));
	}

	public static Renderer<BigDecimal> percent() {
		return PERCENT;
	}

	public static Renderer<BigDecimal> percentNoSign() {
		return PERCENT_NO_SIGN;
	}

	public static Renderer<BigDecimal> percentRelative() {
		return PERCENT_RELATIVE;
	}

}

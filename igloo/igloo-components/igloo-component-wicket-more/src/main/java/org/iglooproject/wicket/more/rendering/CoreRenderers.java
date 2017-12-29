package org.iglooproject.wicket.more.rendering;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import javax.annotation.Nonnull;

import com.google.common.base.Function;
import com.google.common.base.Functions;

import org.iglooproject.commons.util.functional.SerializableFunction;

public final class CoreRenderers {

	private static Function<Locale, DecimalFormat> percentDecimalFormatFunction(final String pattern, final RoundingMode roundingMode) {
		return new SerializableFunction<Locale, DecimalFormat>() {
				private static final long serialVersionUID = 1L;
				@Override
				public DecimalFormat apply(@Nonnull Locale input) {
					DecimalFormat df = new DecimalFormat(pattern, new DecimalFormatSymbols(input));
					df.setRoundingMode(roundingMode);
					df.setMultiplier(100);
					return df;
				}
		};
	}

	private static final Function<Locale, DecimalFormat> PERCENT_FORMAT_FUNCTION = percentDecimalFormatFunction("#0.00\u00A0'%'", RoundingMode.HALF_UP);

	private static final Function<Locale, DecimalFormat> PERCENT_NO_SIGN_FORMAT_FUNCTION = percentDecimalFormatFunction("#0.00\u00A0", RoundingMode.HALF_UP);

	private static final Renderer<BigDecimal> PERCENT = Renderer.<BigDecimal>fromNumberFormat(PERCENT_FORMAT_FUNCTION);

	private static final Renderer<BigDecimal> PERCENT_NO_SIGN = Renderer.<BigDecimal>fromNumberFormat(PERCENT_NO_SIGN_FORMAT_FUNCTION);

	private static final Function<DecimalFormat, DecimalFormat> TO_RELATIVE_FORMAT_FUNCTION = new SerializableFunction<DecimalFormat, DecimalFormat>() {
		private static final long serialVersionUID = 1L;
		@Override
		public DecimalFormat apply(@Nonnull DecimalFormat input) {
			input.setPositivePrefix("+");
			input.setNegativePrefix("-");
			return input;
		}
	};

	private static final Renderer<BigDecimal> PERCENT_RELATIVE = Renderer.<BigDecimal>fromNumberFormat(Functions.compose(TO_RELATIVE_FORMAT_FUNCTION, PERCENT_FORMAT_FUNCTION));

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

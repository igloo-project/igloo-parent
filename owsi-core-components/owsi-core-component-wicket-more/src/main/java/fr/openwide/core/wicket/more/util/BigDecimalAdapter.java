package fr.openwide.core.wicket.more.util;

import java.math.BigDecimal;

public class BigDecimalAdapter implements NumberAdapter<BigDecimal> {
	private static final long serialVersionUID = -4217114946825987375L;

	public static final BigDecimalAdapter INSTANCE = new BigDecimalAdapter();

	@Override
	public BigDecimal convert(Number number) {
		if (number == null) {
			return null;
		} else {
			return BigDecimal.valueOf(number.doubleValue());
		}
	}

	@Override
	public Class<BigDecimal> getNumberClass() {
		return BigDecimal.class;
	}
}

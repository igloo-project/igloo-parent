package fr.openwide.core.wicket.more.util.convert.converters;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Locale;

import org.apache.wicket.util.convert.IConverter;

import fr.openwide.core.wicket.more.util.NumberAdapter;

public class PercentageConverter<N extends Number> implements IConverter {
	private static final long serialVersionUID = -4527255063711426051L;

	private String pattern;

	private boolean displayPercentSymbol;

	private NumberAdapter<N> numberAdapter;

	public PercentageConverter(String pattern, NumberAdapter<N> numberAdapter, boolean displayPercentSymbol) {
		super();
		this.pattern = pattern;
		this.displayPercentSymbol = displayPercentSymbol;
		this.numberAdapter = numberAdapter;
	}

	@Override
	public Object convertToObject(String value, Locale locale) {
		try {
			String valueWithPercent;
			if (displayPercentSymbol) {
				valueWithPercent = value.replaceAll("%", "");
			} else {
				valueWithPercent = value;
			}
			return numberAdapter.convert(new DecimalFormat(pattern).parse(valueWithPercent));
		} catch (ParseException e) {
			return numberAdapter.convert(-1);
		}
	}

	@Override
	public String convertToString(Object value, Locale locale) {
		if (value == null) {
			return "";
		} else {
			if (value.getClass().isAssignableFrom(numberAdapter.getNumberClass())) {
				String result = new DecimalFormat(pattern).format(value);
				if (displayPercentSymbol) {
					result = result + "%";
				}
				return result;
			} else {
				throw new IllegalArgumentException("Value type must be the same as adapter type");
			}
		}
	}
}

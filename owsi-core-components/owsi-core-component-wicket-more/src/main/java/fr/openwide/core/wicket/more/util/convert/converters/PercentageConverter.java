package fr.openwide.core.wicket.more.util.convert.converters;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.util.convert.IConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.openwide.core.wicket.more.util.NumberAdapter;

public class PercentageConverter<N extends Number> implements IConverter {
	private static final long serialVersionUID = -4527255063711426051L;

	private static final Logger LOGGER = LoggerFactory.getLogger(PercentageConverter.class);

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
		if (StringUtils.isNotBlank(value)) {
			try {
				String valueWithPercent;
				if (displayPercentSymbol) {
					valueWithPercent = value.replaceAll("%", "");
				} else {
					valueWithPercent = value;
				}
				return numberAdapter.convert(new DecimalFormat(pattern).parse(valueWithPercent));
			} catch (ParseException e) {
				LOGGER.error("Impossible to convert " + value + " in " + pattern, e);
				return numberAdapter.convert(-1);
			}
		}
		return null;
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

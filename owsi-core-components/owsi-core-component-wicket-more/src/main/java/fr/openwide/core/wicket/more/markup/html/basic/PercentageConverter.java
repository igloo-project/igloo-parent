package fr.openwide.core.wicket.more.markup.html.basic;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Locale;

import org.apache.wicket.util.convert.IConverter;

public class PercentageConverter implements IConverter<Float> {

	private static final long serialVersionUID = -4527255063711426051L;

	private String pattern;

	private boolean displayPercentSymbol;

	public PercentageConverter(String pattern, boolean displayPercentSymbol) {
		super();
		this.pattern = pattern;
		this.displayPercentSymbol = displayPercentSymbol;
	}

	@Override
	public Float convertToObject(String value, Locale locale) {
		try {
			String valueWithPercent;
			if (displayPercentSymbol) {
				valueWithPercent = value.replaceAll("%", "");
			} else {
				valueWithPercent = value;
			}
			return new DecimalFormat(pattern).parse(valueWithPercent).floatValue();
		} catch (ParseException e) {
			return -1f;
		}
	}

	@Override
	public String convertToString(Float value, Locale locale) {
		if (value == null) {
			return "";
		} else {
			String result = new DecimalFormat(pattern).format((Float) value);
			if (displayPercentSymbol) {
				result = result + "%";
			}
			return result;
		}
	}
	
}
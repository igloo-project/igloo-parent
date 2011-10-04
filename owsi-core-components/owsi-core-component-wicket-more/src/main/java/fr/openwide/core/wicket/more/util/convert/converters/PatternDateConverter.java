package fr.openwide.core.wicket.more.util.convert.converters;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.wicket.util.convert.converters.AbstractConverter;
import org.apache.wicket.util.string.Strings;

public class PatternDateConverter extends AbstractConverter {
	
	private static final long serialVersionUID = 5741008524800373419L;

	private String datePattern;

	public PatternDateConverter(String datePattern) {
		super();
		this.datePattern = datePattern;
	}

	@Override
	public Object convertToObject(String value, Locale locale) {
		if (value == null || Strings.isEmpty(value)) {
			return null;
		} else {
			return (Date) parse(getDateFormat(locale), value, locale);
		}
	}

	@Override
	public String convertToString(final Object value, Locale locale) {
		return getDateFormat(locale).format(value);
	}

	@Override
	protected Class<Date> getTargetType() {
		return Date.class;
	}

	public DateFormat getDateFormat(Locale currentLocale) {
		Locale locale;
		if (currentLocale != null) {
			locale = currentLocale;
		} else {
			locale = Locale.ENGLISH;
		}
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern, locale);
		return dateFormat;
	}
	
}
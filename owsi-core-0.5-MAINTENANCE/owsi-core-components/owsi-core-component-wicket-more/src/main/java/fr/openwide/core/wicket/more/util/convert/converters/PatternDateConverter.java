package fr.openwide.core.wicket.more.util.convert.converters;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.wicket.util.convert.converters.AbstractConverter;
import org.apache.wicket.util.string.Strings;

public class PatternDateConverter extends AbstractConverter {
	
	private static final long serialVersionUID = 5741008524800373419L;

	private String datePattern;

	/**
	 * Force GMT is useful for date storage, where it is important that if user
	 * stores 3 january 2010 0h0m0s in +1TZ, we don't want to store 2 january 23h0m0s GMT.
	 * We prefer to store the right date in GMT.
	 */
	private boolean forceGmt;

	/**
	 * Historically, this widget is used for date, so we use by default forceGmt = true
	 */
	public PatternDateConverter(String datePattern) {
		this(datePattern, true);
	}

	/**
	 * If you are interested in date, use forceGmt = true. If not use forceGmt = false (see above)
	 */
	public PatternDateConverter(String datePattern, boolean forceGmt) {
		super();
		this.forceGmt = forceGmt;
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
		if (forceGmt) {
			dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		}
		return dateFormat;
	}
	
}
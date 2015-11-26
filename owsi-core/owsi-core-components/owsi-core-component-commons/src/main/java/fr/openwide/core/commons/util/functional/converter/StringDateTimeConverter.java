package fr.openwide.core.commons.util.functional.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.time.DateUtils;

import com.google.common.base.Converter;

public class StringDateTimeConverter extends Converter<String, Date> {

	private static final StringDateTimeConverter INSTANCE = new StringDateTimeConverter();

	private static final String PATTERN = "yyyy-MM-dd HH:mm";
	private static final String PATTERN_FULL = "yyyy-MM-dd HH:mm:ss";

	public static StringDateTimeConverter get() {
		return INSTANCE;
	}

	protected StringDateTimeConverter() {
	}

	@Override
	protected Date doForward(String a) {
		try {
			return DateUtils.parseDate(a, Locale.ROOT, PATTERN, PATTERN_FULL);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Invalid date value '" + a + "'", e);
		}
	}

	@Override
	protected String doBackward(Date b) {
		return new SimpleDateFormat(PATTERN_FULL, Locale.ROOT).format(b);
	}

}

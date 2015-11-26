package fr.openwide.core.commons.util.functional.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.time.DateUtils;

import com.google.common.base.Converter;

public class StringDateConverter extends Converter<String, Date> {

	private static final StringDateConverter INSTANCE = new StringDateConverter();

	private static final String PATTERN = "yyyy-MM-dd";

	public static StringDateConverter get() {
		return INSTANCE;
	}

	protected StringDateConverter() {
	}

	@Override
	protected Date doForward(String a) {
		try {
			return DateUtils.parseDate(a, Locale.ROOT, PATTERN);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Invalid date value '" + a + "'", e);
		}
	}

	@Override
	protected String doBackward(Date b) {
		return new SimpleDateFormat(PATTERN, Locale.ROOT).format(b);
	}

}

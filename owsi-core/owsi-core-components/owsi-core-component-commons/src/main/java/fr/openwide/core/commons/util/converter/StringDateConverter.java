package fr.openwide.core.commons.util.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

import com.google.common.base.Converter;

public class StringDateConverter extends Converter<String, Date> {

	private static final StringDateConverter INSTANCE = new StringDateConverter();

	private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";

	public static StringDateConverter get() {
		return INSTANCE;
	}

	protected StringDateConverter() {
	}

	@Override
	protected Date doForward(String a) {
		try {
			return DateUtils.parseDate(a, PATTERN);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Invalid date value '" + a + "'", e);
		}
	}

	@Override
	protected String doBackward(Date b) {
		return new SimpleDateFormat(PATTERN).format(b);
	}

}

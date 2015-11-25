package fr.openwide.core.commons.util.converter;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Converter;

public class StringBooleanConverter extends Converter<String, Boolean> {

	private static final StringBooleanConverter INSTANCE = new StringBooleanConverter();

	private static final Set<String> trueValues = new HashSet<String>(4);

	private static final Set<String> falseValues = new HashSet<String>(4);

	static {
		trueValues.add("true");
		trueValues.add("on");
		trueValues.add("yes");
		trueValues.add("1");

		falseValues.add("false");
		falseValues.add("off");
		falseValues.add("no");
		falseValues.add("0");
	}

	public static StringBooleanConverter get() {
		return INSTANCE;
	}

	protected StringBooleanConverter() {
	}

	@Override
	protected Boolean doForward(String a) {
		String value = StringUtils.lowerCase(StringUtils.trim(a));
		if (trueValues.contains(value)) {
			return Boolean.TRUE;
		} else if (falseValues.contains(value)) {
			return Boolean.FALSE;
		} else {
			throw new IllegalArgumentException("Invalid boolean value '" + a + "'");
		}
	}

	@Override
	protected String doBackward(Boolean b) {
		return b.toString();
	}

}

package fr.openwide.core.wicket.more.util.convert.converters;

import java.util.Locale;

import org.apache.wicket.Session;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;

public class HumanReadableLocaleConverter implements IConverter<Locale> {

	private static final long serialVersionUID = 3616234068185075603L;
	
	private static HumanReadableLocaleConverter INSTANCE = new HumanReadableLocaleConverter();
	public static HumanReadableLocaleConverter get() {
		return INSTANCE;
	}
	
	private HumanReadableLocaleConverter() { }

	@Override
	public Locale convertToObject(String value, Locale locale) throws ConversionException {
		throw new UnsupportedOperationException("This converter cannot convert from String to Locale");
	}

	@Override
	public String convertToString(Locale value, Locale locale) {
		if (locale == null) {
			return null;
		}
		return locale != null ? locale.getDisplayName(Session.get().getLocale()) : null;
	}

}

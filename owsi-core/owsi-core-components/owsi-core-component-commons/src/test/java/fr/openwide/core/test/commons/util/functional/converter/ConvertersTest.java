package fr.openwide.core.test.commons.util.functional.converter;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import com.google.common.base.Converter;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import fr.openwide.core.commons.util.functional.Suppliers2;
import fr.openwide.core.commons.util.functional.converter.StringBigDecimalConverter;
import fr.openwide.core.commons.util.functional.converter.StringBooleanConverter;
import fr.openwide.core.commons.util.functional.converter.StringCollectionConverter;
import fr.openwide.core.commons.util.functional.converter.StringDateConverter;
import fr.openwide.core.commons.util.functional.converter.StringDateTimeConverter;
import fr.openwide.core.commons.util.functional.converter.StringLocaleConverter;
import fr.openwide.core.commons.util.functional.converter.StringURIConverter;

@RunWith(Parameterized.class)
public class ConvertersTest {

	@Parameters(name = "{index} - {1} expecting {2}")
	public static Iterable<Object[]> data() throws URISyntaxException {
		return Arrays.asList(new Object[][] {
				{ StringBigDecimalConverter.get(), "15.01", new BigDecimal("15.01") },
				{ StringBigDecimalConverter.get().reverse(), new BigDecimal("15.01"), "15.01" },
				{ StringBooleanConverter.get(), "true", true },
				{ StringBooleanConverter.get(), "on", true },
				{ StringBooleanConverter.get(), "yes", true },
				{ StringBooleanConverter.get(), "1", true },
				{ StringBooleanConverter.get(), "false", false },
				{ StringBooleanConverter.get(), "off", false },
				{ StringBooleanConverter.get(), "no", false },
				{ StringBooleanConverter.get(), "0", false },
				{ StringBooleanConverter.get().reverse(), true, "true" },
				{ StringBooleanConverter.get().reverse(), false, "false" },
				{ new StringCollectionConverter<>(StringBooleanConverter.get(), Suppliers2.<Boolean>arrayList()), "true false true", Lists.newArrayList(true, false, true) },
				{ new StringCollectionConverter<>(Converter.<String>identity(), Suppliers2.<String>arrayList()).separator("_"), "test1_test2_test3", Lists.newArrayList("test1", "test2", "test3") },
				{ new StringCollectionConverter<>(StringBooleanConverter.get(), Suppliers2.<Boolean>arrayList()).separator(";").reverse(), Lists.newArrayList(true, false, true), "true;false;true" },
				{ new StringCollectionConverter<>(Converter.<String>identity(), Suppliers2.<String>arrayList()).joiner(Joiner.on("@").skipNulls()).reverse(), Lists.newArrayList("test1", "test2", null), "test1@test2" },
				{ StringDateConverter.get(), "1990-04-18", new GregorianCalendar(1990, Calendar.APRIL, 18, 0, 0, 0).getTime() },
				{ StringDateConverter.get(), "2015-12-25", new GregorianCalendar(2015, Calendar.DECEMBER, 25, 0, 0, 0).getTime() },
				{ StringDateConverter.get().reverse(), new GregorianCalendar(1990, Calendar.APRIL, 18, 0, 0, 0).getTime(), "1990-04-18" },
				{ StringDateConverter.get().reverse(), new GregorianCalendar(2015, Calendar.DECEMBER, 25, 0, 0, 0).getTime(), "2015-12-25"},
				{ StringDateTimeConverter.get(), "1990-04-18 12:04", new GregorianCalendar(1990, Calendar.APRIL, 18, 12, 4, 0).getTime() },
				{ StringDateTimeConverter.get(), "2015-12-31 23:59:59", new GregorianCalendar(2015, Calendar.DECEMBER, 31, 23, 59, 59).getTime() },
				{ StringDateTimeConverter.get().reverse(), new GregorianCalendar(1990, Calendar.APRIL, 18, 12, 4, 0).getTime(), "1990-04-18 12:04:00" },
				{ StringDateTimeConverter.get().reverse(), new GregorianCalendar(2015, Calendar.DECEMBER, 31, 23, 59, 59).getTime(), "2015-12-31 23:59:59"},
				{ StringLocaleConverter.get(), "fr", Locale.FRENCH },
				{ StringLocaleConverter.get(), "fr-FR", Locale.FRANCE },
				{ StringLocaleConverter.get(), "en", Locale.ENGLISH },
				{ StringLocaleConverter.get(), "en-US", Locale.US },
				{ StringLocaleConverter.get(), "pt-BR", new Locale("pt", "BR") },
				{ StringLocaleConverter.get().reverse(), Locale.FRENCH, "fr" },
				{ StringLocaleConverter.get().reverse(), Locale.FRANCE, "fr-FR" },
				{ StringLocaleConverter.get().reverse(), Locale.ENGLISH, "en" },
				{ StringLocaleConverter.get().reverse(), Locale.US, "en-US" },
				{ StringLocaleConverter.get().reverse(), new Locale("pt", "BR"), "pt-BR" },
				{ StringURIConverter.get(), "http://www.openwide.fr", new URI("http://www.openwide.fr") },
				{ StringURIConverter.get().reverse(), new URI("www.openwide.fr"), "www.openwide.fr" }
		});
	}

	@Parameter(0)
	public Converter<Object, Object> converter;

	@Parameter(1)
	public Object value;

	@Parameter(2)
	public Object expectedValue;

	@Test
	public void testConverters() {
		Assert.assertEquals(converter.convert(value), expectedValue);
	}

}

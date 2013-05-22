package fr.openwide.core.wicket.more.util.convert.converters;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.Localizer;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.lang.Bytes;

import com.google.common.collect.Lists;

public class HumanReadableBytesConverter implements IConverter<Bytes> {
	
	private static final long serialVersionUID = -5011963259015482288L;

	private static final double BYTES_IN_KB = 1024;

	private static final double DISPLAY_LIMIT = 1000;

	private static final String BYTE_UNIT_KEY = "HumanReadableBytesConverter.byte";
	private static final String KB_UNIT_KEY = "HumanReadableBytesConverter.kilobyte";
	private static final String MB_UNIT_KEY = "HumanReadableBytesConverter.megabyte";
	private static final String GB_UNIT_KEY = "HumanReadableBytesConverter.gigabyte";
	private static final String TB_UNIT_KEY = "HumanReadableBytesConverter.terabyte";

	private static final String OUTPUT_DECIMAL_FORMAT = "#.#";

	private static final List<String> UNIT_KEYS = Lists.newArrayList(BYTE_UNIT_KEY, KB_UNIT_KEY, MB_UNIT_KEY,
			GB_UNIT_KEY, TB_UNIT_KEY);

	@Override
	public Bytes convertToObject(String value, Locale locale) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String convertToString(Bytes value, Locale locale) {
		Long sizeInByte = value.bytes();
		
		if (sizeInByte != null) {
			double humanReadableSize = sizeInByte.doubleValue();
			int unitKeyIndex = 0;
			
			while (humanReadableSize > DISPLAY_LIMIT && unitKeyIndex < UNIT_KEYS.size() - 1) {
				humanReadableSize = humanReadableSize / BYTES_IN_KB;
				unitKeyIndex++;
			}
			
			DecimalFormat twoDecimalsFormat = new DecimalFormat(OUTPUT_DECIMAL_FORMAT, DecimalFormatSymbols.getInstance(locale));
			
			return new StringBuilder(twoDecimalsFormat.format(humanReadableSize)).append(" ")
					.append(getString(UNIT_KEYS.get(unitKeyIndex), locale))
					.toString();
		}
		
		return null;
	}

	private Object getString(String key, Locale locale) {
		return Localizer.get().getString(key, null, null, locale, null, null);
	}

}

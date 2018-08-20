package org.iglooproject.commons.util;

import java.text.Collator;
import java.util.Locale;

import org.iglooproject.commons.util.ordering.SerializableCollator;

import com.google.common.collect.Ordering;

public final class LocaleUtils extends org.apache.commons.lang3.LocaleUtils {

	public static final Ordering<String> initCollator(Locale locale) {
		SerializableCollator collator = new SerializableCollator(locale);
		collator.setStrength(Collator.PRIMARY);
		return collator.nullsLast();
	}

	private LocaleUtils() {
	}

}

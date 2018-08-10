package org.iglooproject.basicapp.core.business.common.util;

import java.util.Collection;
import java.util.Comparator;
import java.util.Locale;
import java.util.Map;

import org.iglooproject.commons.util.LocaleUtils;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Ordering;

public final class BasicApplicationLocale {

	public static final Locale ENGLISH = Locale.ENGLISH;
	public static final Locale FRENCH = Locale.FRENCH;

	public static final Locale DEFAULT = ENGLISH;

	public static final Map<Locale, Comparator<String>> COMPARATORS = ImmutableMap.<Locale, Comparator<String>>builder()
			.put(ENGLISH, initCollator(ENGLISH))
			.put(FRENCH, initCollator(FRENCH))
			.build();

	/**
	 * Every possible locale used in this application, sorted by descending order of priority.
	 */
	public static final Collection<Locale> ALL = ImmutableList.of(
			FRENCH, ENGLISH
	);

	public static final Ordering<String> initCollator(Locale locale) {
		return LocaleUtils.initCollator(locale);
	}

	public static final Comparator<String> comparator() {
		return comparator(DEFAULT);
	}

	public static final Comparator<String> comparator(Locale locale) {
		Locale localeToUse = locale;
		
		if (localeToUse == null) {
			localeToUse = DEFAULT;
		}
		return MoreObjects.firstNonNull(COMPARATORS.get(localeToUse), COMPARATORS.get(DEFAULT));
	}

	private BasicApplicationLocale() {
	}

}

package org.iglooproject.basicapp.core.business.common.util;

import java.util.Collection;
import java.util.Locale;

import com.google.common.collect.ImmutableList;

public final class BasicApplicationLocale {

	public static final Locale ENGLISH = Locale.ENGLISH;
	public static final Locale FRENCH = Locale.FRENCH;

	public static final Locale DEFAULT = ENGLISH;

	/**
	 * Every possible locale used in this application, sorted by descending order of priority.
	 */
	public static final Collection<Locale> ALL = ImmutableList.of(
			FRENCH, ENGLISH
	);

	private BasicApplicationLocale() {
	}

}

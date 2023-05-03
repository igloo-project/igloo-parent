package org.iglooproject.basicapp.core.util.time;

import java.util.Objects;

import org.iglooproject.commons.util.time.IDateTimePattern;

public enum DateTimePattern implements IDateTimePattern {

	SHORT_DATE("datetime.pattern.shortDate"),
	SHORT_DATETIME("datetime.pattern.shortDateTime"),
	REALLY_SHORT_DATE("datetime.pattern.reallyShortDate"),
	REALLY_SHORT_DATETIME("datetime.pattern.reallyShortDateTime"),
	MONTH_YEAR("datetime.pattern.monthYear"),
	SHORT_MONTH_YEAR("datetime.pattern.shortMonthYear"),
	YEAR("datetime.pattern.year"),
	TIME("datetime.pattern.time");

	private String key;

	private DateTimePattern(String key) {
		this.key = Objects.requireNonNull(key);
	}

	@Override
	public String getKey() {
		return key;
	}

}

package org.iglooproject.commons.util.collections.range.time;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.WeekFields;

import com.google.common.collect.Range;

/**
 * Several discrete domains for dates.
 * <p><strong>WARNING:</strong> When using these domains in a contiguous set, you <strong>must</strong>
 * ensure that your range has been {@link PartitionDiscreteDomain#alignOut(Range) aligned} on this domain.
 * Otherwise, you will experience infinite loops.
 * 
 * @see ZonedDateTime
 * @see DateDiscreteDomain
 * @see InstantDiscreteDomain
 */
public class ZonedDateDiscreteDomain extends AbstractTemporalDiscreteDomain<ZonedDateTime> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * @return A discrete domain for the first day of each month at midnight.
	 * <p><strong>WARNING:</strong> When using this domain in a contiguous set, you <strong>must</strong>
	 * ensure that your range has been {@link ZonedDateDiscreteDomain#alignOut(Range) aligned} on this domain.
	 * Otherwise, you will experience infinite loops.
	 */
	public static ZonedDateDiscreteDomain months() {
		return MONTHS;
	}

	private static final ZonedDateDiscreteDomain MONTHS = new ZonedDateDiscreteDomain(ChronoUnit.MONTHS, ChronoField.DAY_OF_MONTH, ChronoUnit.DAYS) {
		private static final long serialVersionUID = 1L;
		private Object readResolve() {
			return MONTHS;
		}
		@Override
		public String toString() {
			return "DateDiscreteDomain.months()";
		}
	};

	/**
	 * @return A discrete domain for the first day of each week at midnight.
	 * <p><strong>WARNING:</strong> When using this domain in a contiguous set, you <strong>must</strong>
	 * ensure that your range has been {@link ZonedDateDiscreteDomain#align(Range) aligned} on this domain.
	 * Otherwise, you will experience infinite loops.
	 */
	public static ZonedDateDiscreteDomain weeks() {
		return WEEKS;
	}

	private static final ZonedDateDiscreteDomain WEEKS = new ZonedDateDiscreteDomain(ChronoUnit.WEEKS, WeekFields.of(DayOfWeek.MONDAY, 1).dayOfWeek(), ChronoUnit.DAYS) {
		private static final long serialVersionUID = 1L;
		private Object readResolve() {
			return WEEKS;
		}
		@Override
		public String toString() {
			return "DateDiscreteDomain.weeks()";
		}
	};

	/**
	 * Keep constructor private, as instances of theses objects must implement readResolve for custom deserialization.
	 */
	private ZonedDateDiscreteDomain(ChronoUnit periodUnit, TemporalField startOfPeriodField, TemporalUnit normalizationUnit) {
		super(periodUnit, startOfPeriodField, normalizationUnit);
	}

	@Override
	protected ZonedDateTime truncateTo(ZonedDateTime value, TemporalUnit unit) {
		return value.truncatedTo(unit);
	}

	@Override
	protected ZonedDateTime plus(ZonedDateTime value, long added, TemporalUnit unit) {
		return value.plus(added, unit);
	}

	@Override
	protected ZonedDateTime with(ZonedDateTime value, TemporalField field, long fieldValue) {
		return value.with(field, fieldValue);
	}

}

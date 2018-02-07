package org.iglooproject.commons.util.collections.range.time;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
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
 * @see LocalDateTime
 * @see ZonedDateDiscreteDomain
 * @see InstantDiscreteDomain
 */
public class DateDiscreteDomain extends AbstractTemporalDiscreteDomain<LocalDateTime> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * @return A discrete domain for the first day of each month at midnight.
	 * <p><strong>WARNING:</strong> When using this domain in a contiguous set, you <strong>must</strong>
	 * ensure that your range has been {@link DateDiscreteDomain#alignOut(Range) aligned} on this domain.
	 * Otherwise, you will experience infinite loops.
	 */
	public static DateDiscreteDomain months() {
		return MONTHS;
	}

	private static final DateDiscreteDomain MONTHS = new DateDiscreteDomain(ChronoUnit.MONTHS, ChronoField.DAY_OF_MONTH, ChronoUnit.DAYS) {
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
	 * ensure that your range has been {@link DateDiscreteDomain#align(Range) aligned} on this domain.
	 * Otherwise, you will experience infinite loops.
	 */
	public static DateDiscreteDomain weeks() {
		return WEEKS;
	}

	private static final DateDiscreteDomain WEEKS = new DateDiscreteDomain(ChronoUnit.WEEKS, WeekFields.of(DayOfWeek.MONDAY, 1).dayOfWeek(), ChronoUnit.DAYS) {
		private static final long serialVersionUID = 1L;
		private Object readResolve() {
			return WEEKS;
		}
		@Override
		public String toString() {
			return "DateDiscreteDomain.weeks()";
		}
	};
	
	public DateDiscreteDomain(ChronoUnit periodUnit, TemporalField startOfPeriodField, TemporalUnit normalizationUnit) {
		super(periodUnit, startOfPeriodField, normalizationUnit);
	}

	@Override
	protected LocalDateTime truncateTo(LocalDateTime value, TemporalUnit unit) {
		return value.truncatedTo(unit);
	}

	@Override
	protected LocalDateTime plus(LocalDateTime value, long added, TemporalUnit unit) {
		return value.plus(added, unit);
	}

	@Override
	protected LocalDateTime with(LocalDateTime value, TemporalField field, long fieldValue) {
		return value.with(field, fieldValue);
	}

}

package fr.openwide.core.commons.util.collections;

import java.io.Serializable;
import java.util.Date;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import com.google.common.collect.Range;

/**
 * Several discrete domains for dates.
 * <p><strong>WARNING:</strong> When using these domains in a contiguous set, you <strong>must</strong>
 * ensure that your range has been {@link PartitionDiscreteDomain#alignOut(Range) aligned} on this domain.
 * Otherwise, you will experience infinite loops.
 */
public class DateDiscreteDomain extends PartitionDiscreteDomain<Date> implements Serializable {

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
	
	private static final DateDiscreteDomain MONTHS = new DateDiscreteDomain(DurationFieldType.months(), DateTimeFieldType.monthOfYear()) {
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
	
	private static final DateDiscreteDomain WEEKS = new DateDiscreteDomain(DurationFieldType.weeks(), DateTimeFieldType.weekOfWeekyear()) {
		private static final long serialVersionUID = 1L;
		private Object readResolve() {
			return WEEKS;
		}
		@Override
		public String toString() {
			return "DateDiscreteDomain.weeks()";
		}
	};
	
	private final DurationFieldType iterationFieldType;
	private final DateTimeFieldType roundFieldType;
	private final PeriodType periodType;

	private DateDiscreteDomain(DurationFieldType iterationFieldType, DateTimeFieldType roundFloorFieldType) {
		this.iterationFieldType = iterationFieldType;
		this.roundFieldType = roundFloorFieldType;
		this.periodType = PeriodType.forFields(new DurationFieldType[] { iterationFieldType });
	}

	@Override
	public Date next(Date value) {
		return internalAlignPrevious(value)
				.withFieldAdded(iterationFieldType, 1).toDate();
	}

	@Override
	public Date previous(Date value) {
		return internalAlignNext(value)
				.withFieldAdded(iterationFieldType, -1).toDate();
	}

	@Override
	public long distance(Date start, Date end) {
		return new Period(new LocalDate(start), new LocalDate(end), periodType).get(iterationFieldType);
	}
	
	@Override
	public Date alignNext(Date value) {
		return internalAlignNext(value).toDate();
	}
	
	@Override
	public Date alignPrevious(Date value) {
		return internalAlignPrevious(value).toDate();
	}
	
	private LocalDate internalAlignNext(Date value) {
		return new LocalDate(value).property(roundFieldType).roundCeilingCopy();
	}
	
	private LocalDate internalAlignPrevious(Date value) {
		return new LocalDate(value).property(roundFieldType).roundFloorCopy();
	}

}

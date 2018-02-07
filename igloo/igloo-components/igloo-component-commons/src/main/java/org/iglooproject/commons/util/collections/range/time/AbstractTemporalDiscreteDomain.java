package org.iglooproject.commons.util.collections.range.time;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;

/**
 * <p>Base implementation to support {@link PartitionDiscreteDomain} for various java.time implementations.</p>
 * 
 * <p>Abstract methods allow to invoke *.truncateTo which is not part of any common interface (not available in
 * {@link Temporal}), and to manage {@link Temporal#with(TemporalField, long)} and
 * {@link Temporal#plus(long, TemporalUnit)} without any cast (Temporal version of this methods is loosely typed).</p>
 *
 * @param <T> an implementation of {@link Temporal} and {@link Comparable}
 * 
 * @see DateDiscreteDomain
 * @see LocalDateTime
 * @see ZonedDateDiscreteDomain
 * @see ZonedDateTime
 * @see InstantDiscreteDomain
 * @see Instant
 */
abstract class AbstractTemporalDiscreteDomain <T extends Temporal & Comparable<? super T>> extends PartitionDiscreteDomain<T> {

	private final TemporalUnit normalizationUnit;
	private final TemporalUnit periodUnit;
	private final TemporalField startOfPeriodField;

	protected AbstractTemporalDiscreteDomain(ChronoUnit periodUnit, TemporalField startOfPeriodField, TemporalUnit normalizationUnit) {
		this.periodUnit = periodUnit;
		this.startOfPeriodField = startOfPeriodField;
		this.normalizationUnit = normalizationUnit;
	}

	@Override
	public T next(T value) {
		return alignPrevious(plus(value, 1, periodUnit));
	}

	@Override
	public T previous(T value) {
		return alignNext(plus(value, -1, periodUnit));
	}

	@Override
	public long distance(T start, T end) {
		return periodUnit.between(start, end);
	}
	
	@Override
	public T alignNext(T value) {
		return internalAlignNext(value);
	}
	
	@Override
	public T alignPrevious(T value) {
		return internalAlignPrevious(value);
	}
	
	private T internalAlignNext(T value) {
		T valueStart = internalAlignPrevious(value);
		if (valueStart.equals(value)) {
			// we already are at a discrete domain value, return self
			return value;
		} else {
			// return next value from floor value
			return plus(valueStart, 1, periodUnit);
		}
	}
	
	private T internalAlignPrevious(T value) {
		T normalizedValue = truncateTo(value, normalizationUnit);
		long minimalIterationFieldValue = normalizedValue.range(startOfPeriodField).getMinimum();
		T startOfPeriodValue = with(normalizedValue, startOfPeriodField, minimalIterationFieldValue);
		return startOfPeriodValue;
	}

	protected abstract T truncateTo(T value, TemporalUnit unit);
	protected abstract T plus(T value, long added, TemporalUnit unit);
	protected abstract T with(T value, TemporalField field, long fieldValue);

}
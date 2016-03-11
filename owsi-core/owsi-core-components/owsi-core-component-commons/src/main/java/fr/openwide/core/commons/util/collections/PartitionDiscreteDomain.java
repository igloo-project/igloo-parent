package fr.openwide.core.commons.util.collections;

import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Range;

@SuppressWarnings("rawtypes")
public abstract class PartitionDiscreteDomain<T extends Comparable> extends DiscreteDomain<T> {

	public PartitionDiscreteDomain() {
		super();
	}

	/**
	 * Aligns the given value on a point of this partition.
	 * <p>If this value is already exactly on a point of this domain, this returns an object representing the same value.
	 * Otherwise, this will return an object representing the closest previous value in this domain. 
	 */
	public abstract T alignPrevious(T value);

	/**
	 * Aligns the given value on a point of this partition.
	 * <p>If this value is already exactly on a point of this domain, this returns an object representing the same value.
	 * Otherwise, this will return an object representing the closest next value in this domain. 
	 */
	public abstract T alignNext(T value);

	/**
	 * {@link #alignNext(T) align} a range on this domain, extending the range as necessary.
	 */
	public Range<T> alignOut(Range<T> value) {
		Range<T> result;
		
		if (value.hasLowerBound()) {
			if (value.hasUpperBound()) {
				result = Range.range(
						alignPrevious(value.lowerEndpoint()), value.lowerBoundType(),
						alignNext(value.upperEndpoint()), value.upperBoundType()
				);
			} else {
				result = Range.downTo(alignPrevious(value.lowerEndpoint()), value.lowerBoundType());
			}
		} else {
			if (value.hasUpperBound()) {
				result = Range.upTo(alignNext(value.upperEndpoint()), value.upperBoundType());
			} else {
				result = Range.all();
			}
		}
		
		return result;
	}

}
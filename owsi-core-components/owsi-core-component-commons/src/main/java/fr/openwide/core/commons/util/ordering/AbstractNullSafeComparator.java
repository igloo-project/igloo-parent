package fr.openwide.core.commons.util.ordering;

import java.io.Serializable;
import java.util.Comparator;

import com.google.common.collect.Ordering;

public abstract class AbstractNullSafeComparator<T> extends Ordering<T>
		implements Comparator<T>, Serializable {
	
	private static final long serialVersionUID = -8608152531755599680L;
	
	private final int orderIfLeftNull;
	private final int orderIfRightNull;

	protected AbstractNullSafeComparator() {
		this(true);
	}
	
	protected AbstractNullSafeComparator(boolean nullIsLow) {
		this.orderIfLeftNull = nullIsLow ? -1 : +1;
		this.orderIfRightNull = nullIsLow ? +1 : -1;
	}
	
	@Override
	public int compare(T left, T right) {
		if (left == null && right == null) {
			return 0;
		} else if (left == null) {
			return orderIfLeftNull;
		} else if (right == null) {
			return orderIfRightNull;
		} else {
			return compareNotNullObjects(left, right);
		}
	}
	
	protected abstract int compareNotNullObjects(T left, T right);

}

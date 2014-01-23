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
	public final int compare(T left, T right) {
		if (left == null && right == null) {
			return 0;
		} else if (left == null) {
			return orderIfLeftNull;
		} else if (right == null) {
			return orderIfRightNull;
		} else if (equalsNotNullObjects(left, right)) {
			return 0;
		} else {
			return compareNotNullObjects(left, right);
		}
	}
	
	/**
	 * Method called just before trying to compare the objects, in order to avoid potentially expensive comparison operations.
	 * <p>If your comparator is <em>consistent with equals</em> (see {@link Comparator}), you may safely override this
	 * and return <code>left.equals(right)</code>.
	 */
	protected boolean equalsNotNullObjects(T left, T right) {
		return left == right;
	}
	
	protected abstract int compareNotNullObjects(T left, T right);

}

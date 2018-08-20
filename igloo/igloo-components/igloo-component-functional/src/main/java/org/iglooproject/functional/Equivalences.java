package org.iglooproject.functional;

import java.util.Date;
import java.util.Objects;

import com.google.common.base.Equivalence;

public final class Equivalences {

	private Equivalences() { }
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <C extends Comparable> Equivalence<C> comparable() {
		return (Equivalence<C>) COMPARABLE_EQUIVALENCE;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static final Equivalence<? extends Comparable> COMPARABLE_EQUIVALENCE = new AbstractSerializableEquivalence<Comparable>() {
		private static final long serialVersionUID = -6403530720447387732L;

		@Override
		protected boolean doEquivalent(Comparable left, Comparable right) {
			return left == null
					? (right == null)
					: (right != null && left.compareTo(right) == 0);
		}

		@Override
		protected int doHash(Comparable object) {
			return Objects.hash(object);
		}

		private Object readResolve() {
			return COMPARABLE_EQUIVALENCE;
		}
	};
	
	public static Equivalence<Date> date() {
		return DATE_EQUIVALENCE;
	}
	
	private static final Equivalence<Date> DATE_EQUIVALENCE = new AbstractSerializableEquivalence<Date>() {
		private static final long serialVersionUID = 8680281926385852199L;

		@Override
		protected boolean doEquivalent(Date left, Date right) {
			return left.getTime() == right.getTime();
		}

		@Override
		protected int doHash(Date object) {
			long ht = object.getTime();
			return (int) ht ^ (int) (ht >> 32);
		}

		private Object readResolve() {
			return DATE_EQUIVALENCE;
		}
	};
}

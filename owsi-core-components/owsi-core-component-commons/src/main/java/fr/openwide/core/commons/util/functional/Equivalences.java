package fr.openwide.core.commons.util.functional;

import java.util.Date;

import com.google.common.base.Equivalence;

public final class Equivalences {

	private Equivalences() { }
	
	public static Equivalence<Date> date() {
		return DATE_EQUIVALENCE;
	}
	
	private static final Equivalence<Date> DATE_EQUIVALENCE = new Equivalence<Date>() {
		@Override
		protected boolean doEquivalent(Date left, Date right) {
			return left.getTime() == right.getTime();
		}

		@Override
		protected int doHash(Date object) {
			long ht = object.getTime();
			return (int) ht ^ (int) (ht >> 32);
		}
	};
}

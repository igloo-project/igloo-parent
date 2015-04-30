package fr.openwide.core.wicket.more.util.model;

import org.apache.wicket.model.IDetachable;

import com.google.common.collect.Lists;

public final class Detachables {

	private Detachables() {
	}
	
	public static void detach(IDetachable detachable) {
		if (detachable != null) {
			detachable.detach();
		}
	}
	
	public static void detach(IDetachable detachable, IDetachable ... others) {
		detach(Lists.asList(detachable, others));
	}
	
	public static void detach(Iterable<? extends IDetachable> detachables) {
		for (IDetachable detachable : detachables) {
			if (detachable != null) {
				detachable.detach();
			}
		}
	}
}

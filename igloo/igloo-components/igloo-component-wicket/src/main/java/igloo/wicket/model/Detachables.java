package igloo.wicket.model;

import java.util.Map;
import java.util.Map.Entry;

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
	
	public static void detach(Map<? extends IDetachable, ? extends IDetachable> detachablesMap) {
		for (Entry<? extends IDetachable, ? extends IDetachable> entry : detachablesMap.entrySet()) {
			Detachables.detach(entry.getKey(), entry.getValue());
		}
	}
}

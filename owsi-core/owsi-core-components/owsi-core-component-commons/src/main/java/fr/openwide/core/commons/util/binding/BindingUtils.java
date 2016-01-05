package fr.openwide.core.commons.util.binding;

import org.bindgen.Binding;

public final class BindingUtils {

	public static Class<?> getRootType(Binding<?> binding) {
		Binding<?> candidate = binding;
		Binding<?> parent;
		do {
			parent = candidate;
			candidate = parent.getParentBinding();
		} while (candidate != null);
		return parent.getType();
	}

	private BindingUtils() {
	}

}

package org.iglooproject.components.bindgen.trigger;

import org.bindgen.Bindable;
import org.iglooproject.components.bindgen.JavaBindings;

/**
 * Trigger binding generation for common java types
 */
@Bindable
public class TriggerJavaBindings {

	private JavaBindings bindings;

	public JavaBindings getBindings() {
		return bindings;
	}

	public void setBindings(JavaBindings bindings) {
		this.bindings = bindings;
	}

}

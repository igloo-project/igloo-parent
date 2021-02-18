package org.iglooproject.wicket.bootstrap5.markup.html.template.js.bootstrap.collapse;

import java.util.LinkedHashMap;
import java.util.Objects;

import org.wicketstuff.wiquery.core.javascript.JsStatement;

public class BootstrapCollapseOptions extends LinkedHashMap<String, String> implements IBootstrapCollapseOptions {

	private static final long serialVersionUID = 2407550226099765341L;

	public static final BootstrapCollapseOptions get() {
		return new BootstrapCollapseOptions();
	}

	public BootstrapCollapseOptions parent(String parent) {
		Objects.requireNonNull(parent);
		put("data-bs-parent", parent);
		return this;
	}

	public BootstrapCollapseOptions parent(JsStatement parent) {
		Objects.requireNonNull(parent);
		put("data-bs-parent", parent.render(false).toString());
		return this;
	}

}

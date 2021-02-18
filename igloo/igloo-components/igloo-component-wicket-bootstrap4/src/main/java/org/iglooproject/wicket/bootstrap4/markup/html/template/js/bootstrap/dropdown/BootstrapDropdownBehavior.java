package org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.dropdown;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.wicketstuff.wiquery.core.javascript.JsStatement;

public class BootstrapDropdownBehavior extends Behavior {

	private static final long serialVersionUID = 468977788703981632L;

	private static final String BOOTSTRAP_DROPDOWN = "dropdown";

	private static final String DEFAULT_SELECTOR = ".dropdown-toggle";

	public BootstrapDropdownBehavior() {
		super();
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		response.render(JavaScriptHeaderItem.forReference(BootstrapDropDownJavaScriptResourceReference.get()));
		response.render(OnDomReadyHeaderItem.forScript(new JsStatement().$(null, DEFAULT_SELECTOR).chain(BOOTSTRAP_DROPDOWN).render()));
	}

}

package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.dropdown;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.odlabs.wiquery.core.behavior.WiQueryAbstractBehavior;
import org.odlabs.wiquery.core.javascript.JsStatement;

public class BootstrapDropdownBehavior extends WiQueryAbstractBehavior {

	private static final long serialVersionUID = 468977788703981632L;

	private static final String BOOTSTRAP_DROPDOWN = "dropdown";
	
	private static final String DEFAULT_SELECTOR = ".dropdown-toggle";
	
	public BootstrapDropdownBehavior() {
		super();
	}
	
	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		response.renderJavaScriptReference(BootstrapDropDownJavaScriptResourceReference.get());
	}

	@Override
	public JsStatement statement() {
		return new JsStatement().$(null, DEFAULT_SELECTOR).chain(BOOTSTRAP_DROPDOWN);
	}

}

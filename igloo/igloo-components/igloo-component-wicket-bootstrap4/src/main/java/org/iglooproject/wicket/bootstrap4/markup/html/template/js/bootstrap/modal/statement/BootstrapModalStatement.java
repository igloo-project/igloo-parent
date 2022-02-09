package org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.modal.statement;

import org.apache.wicket.Component;
import org.wicketstuff.wiquery.core.javascript.JsStatement;

public final class BootstrapModalStatement {

	public static final JsStatement show(Component modal) {
		return show(modal, null);
	}

	public static final JsStatement show(Component modal, BootstrapModal options) {
		if (options == null) {
			return new JsStatement().$(modal).chain(BootstrapModal.modal()).append(";");
		} else {
			return new JsStatement().$(modal).chain(options).append(";");
		}
	}

	public static final JsStatement hide(Component modal) {
		return new JsStatement().$(modal).chain(BootstrapModal.hide()).append(";");
	}

	private BootstrapModalStatement() {
	}

}

package org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.confirm.statement;

import org.apache.wicket.Component;
import org.wicketstuff.wiquery.core.javascript.JsStatement;

public final class BootstrapConfirmStatement {

	public static final JsStatement confirm(Component component) {
		return new JsStatement().$(component).chain(BootstrapConfirm.confirm()).append(";");
	}
	
	private BootstrapConfirmStatement() {
	}

}

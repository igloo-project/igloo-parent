package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.confirm.statement;

import org.apache.wicket.Component;
import org.odlabs.wiquery.core.javascript.JsStatement;

public class BootstrapConfirmStatement {

	public final static JsStatement confirm(Component component) {
		return new JsStatement().$(component).chain(BootstrapConfirm.confirm()).append(";");
	}

}

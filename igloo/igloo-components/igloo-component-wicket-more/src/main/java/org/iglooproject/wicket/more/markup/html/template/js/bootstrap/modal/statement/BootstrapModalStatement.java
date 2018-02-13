package org.iglooproject.wicket.more.markup.html.template.js.bootstrap.modal.statement;

import org.apache.wicket.Component;
import org.wicketstuff.wiquery.core.javascript.JsStatement;

public final class BootstrapModalStatement {

	private static final int HIDE_TIMEOUT_DELAY = 1000;

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
		/*
		 * Bootstrap modal js doesn't allow to close a modal while until it's fully opened.
		 * We need to set up a timeout to delay the hide statement call.
		 */
		return new JsStatement()
				.append("setTimeout(function() {")
				.append(new JsStatement().$(modal).chain(BootstrapModal.hide()).append(";").render())
				.append("}, " + HIDE_TIMEOUT_DELAY + ");");
	}

	private BootstrapModalStatement() {
	}

}

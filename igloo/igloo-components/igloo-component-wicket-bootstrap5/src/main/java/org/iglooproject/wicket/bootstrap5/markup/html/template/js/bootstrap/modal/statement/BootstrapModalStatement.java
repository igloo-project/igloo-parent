package org.iglooproject.wicket.bootstrap5.markup.html.template.js.bootstrap.modal.statement;

import org.apache.wicket.Component;
import org.iglooproject.bootstrap.api.IBootstrapModal;
import org.wicketstuff.wiquery.core.javascript.JsStatement;
import org.wicketstuff.wiquery.core.javascript.JsUtils;

public final class BootstrapModalStatement {

	public static final JsStatement show(Component modal) {
		return show(modal, null);
	}

	public static final JsStatement show(Component modal, IBootstrapModal options) {
		return modal(modal, options).chain("show");
	}

	public static final JsStatement hide(Component modal) {
		return modal(modal, null).chain("hide");
	}

	private static final JsStatement modal(Component modal, IBootstrapModal options) {
		if (options == null) {
			return new JsStatement().append("new bootstrapMore.ModalMore(document.getElementById(" + JsUtils.quotes(modal.getMarkupId()) + "))");
		} else {
			return new JsStatement().append("new bootstrapMore.ModalMore(document.getElementById(" + JsUtils.quotes(modal.getMarkupId()) + "), " + options.statementArgs()[0] + ")");
		}
	}

	private BootstrapModalStatement() {
	}

}

package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.statement;

import org.apache.wicket.Component;
import org.wicketstuff.wiquery.core.javascript.JsStatement;

/**
 * @deprecated Use Bootstrap 4 CSS et JS from now on.
 */
@Deprecated
public final class BootstrapModalManagerStatement {

	public static final String MODAL_MANAGER = "modalmanager";

	protected static final JsStatement body() {
		return new JsStatement().$(null, "body");
	}

	public static final JsStatement loading() {
		return body().chain(BootstrapModalManager.loading()).append(";");
	}

	public static final JsStatement removeLoading() {
		return body().chain(BootstrapModalManager.removeLoading()).append(";");
	}

	public static final JsStatement show(Component modal) {
		return show(modal, null);
	}

	/**
	 * @param modal
	 * @param options - peut être null (utilise les options par défaut)
	 */
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
	
	private BootstrapModalManagerStatement() {
	}

}

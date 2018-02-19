package org.iglooproject.wicket.bootstrap3.markup.html.template.js.bootstrap.modal.statement;

import org.apache.wicket.Component;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.modal.statement.BootstrapModal;
import org.wicketstuff.wiquery.core.javascript.JsStatement;

/**
 * @deprecated Use Bootstrap 4 and default Bootstrap 4 modal plugin.
 * @see {@code BootstrapModalStatement}
 */
@Deprecated
public final class BootstrapModalManagerStatement {

	public static final String MODAL_MANAGER = "modalmanager";

	@Deprecated
	protected static final JsStatement body() {
		return new JsStatement().$(null, "body");
	}

	@Deprecated
	public static final JsStatement loading() {
		return body().chain(BootstrapModalManager.loading()).append(";");
	}

	@Deprecated
	public static final JsStatement removeLoading() {
		return body().chain(BootstrapModalManager.removeLoading()).append(";");
	}

	@Deprecated
	public static final JsStatement show(Component modal) {
		return show(modal, null);
	}

	/**
	 * @param modal
	 * @param options - peut être null (utilise les options par défaut)
	 */
	@Deprecated
	public static final JsStatement show(Component modal, BootstrapModal options) {
		if (options == null) {
			return new JsStatement().$(modal).chain(BootstrapModal.modal()).append(";");
		} else {
			return new JsStatement().$(modal).chain(options).append(";");
		}
	}

	@Deprecated
	public static final JsStatement hide(Component modal) {
		return new JsStatement().$(modal).chain(BootstrapModal.hide()).append(";");
	}

	@Deprecated
	private BootstrapModalManagerStatement() {
	}

}

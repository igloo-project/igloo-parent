package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.statement;

import org.apache.wicket.Component;
import org.odlabs.wiquery.core.javascript.JsStatement;

public class BootstrapModalManagerStatement {

	public static final String MODAL_MANAGER = "modalmanager";

	protected final static JsStatement body() {
		return new JsStatement().$(null, "body");
	}

	public final static JsStatement loading() {
		return body().chain(BootstrapModalManager.loading()).append(";");
	}

	public final static JsStatement removeLoading() {
		return body().chain(BootstrapModalManager.removeLoading()).append(";");
	}

	public final static JsStatement show(Component modal) {
		return show(modal, null);
	}

	/**
	 * @param modal
	 * @param options - peut être null (utilise les options par défaut)
	 */
	public final static JsStatement show(Component modal, BootstrapModal options) {
		return new JsStatement().$(modal).chain(options).append(";");
	}

	public final static JsStatement hide(Component modal) {
		return new JsStatement().$(modal).chain(BootstrapModal.hide()).append(";");
	}

}

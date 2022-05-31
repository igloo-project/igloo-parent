package org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.confirm;

import org.apache.wicket.Component;
import org.iglooproject.bootstrap.api.confirm.IBootstrapConfirm;
import org.wicketstuff.wiquery.core.javascript.ChainableStatement;
import org.wicketstuff.wiquery.core.javascript.JsStatement;

public class BootstrapConfirm implements ChainableStatement, IBootstrapConfirm {

	public static final String BOOTSTRAP_MODAL_CHAIN_LABEL = "confirm";

	@Override
	public String chainLabel() {
		return BOOTSTRAP_MODAL_CHAIN_LABEL;
	}

	@Override
	public CharSequence[] statementArgs() {
		return new CharSequence[0];
	}

	public static final BootstrapConfirm confirm() {
		return new BootstrapConfirm();
	}

	@Override
	public JsStatement confirm(Component confirm) {
		return new JsStatement().$(confirm).chain(this);
	}

}

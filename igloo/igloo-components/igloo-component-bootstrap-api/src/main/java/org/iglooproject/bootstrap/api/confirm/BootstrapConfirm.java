package org.iglooproject.bootstrap.api.confirm;

import org.wicketstuff.wiquery.core.javascript.ChainableStatement;

public class BootstrapConfirm implements ChainableStatement {

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

}

package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.confirm.statement;

import org.odlabs.wiquery.core.javascript.ChainableStatement;

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

	public final static BootstrapConfirm confirm() {
		return new BootstrapConfirm();
	}

}

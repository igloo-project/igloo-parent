package org.iglooproject.wicket.bootstrap3.markup.html.template.js.bootstrap.modal.statement;

import javax.annotation.Nonnull;

import org.wicketstuff.wiquery.core.javascript.ChainableStatement;
import org.wicketstuff.wiquery.core.options.LiteralOption;

public final class BootstrapModalManager implements ChainableStatement {

	public static final String BOOTSTRAP_MODAL_MANAGER_CHAIN_LABEL = "modalmanager";

	public static final CharSequence BOOTSTRAP_MODAL_MANAGER_LOADING_METHOD = new LiteralOption("loading").getJavascriptOption();

	public static final CharSequence BOOTSTRAP_MODAL_MANAGER_REMOVE_LOADING_METHOD = new LiteralOption("removeLoading").getJavascriptOption();

	private final CharSequence[] args;

	private BootstrapModalManager(@Nonnull CharSequence[] bootstrapArgs) {
		super();
		this.args = bootstrapArgs.clone();
	}

	@Override
	public String chainLabel() {
		return BOOTSTRAP_MODAL_MANAGER_CHAIN_LABEL;
	}

	@Override
	public CharSequence[] statementArgs() {
		return args;
	}

	public static final BootstrapModalManager loading() {
		return new BootstrapModalManager(new CharSequence[] { BOOTSTRAP_MODAL_MANAGER_LOADING_METHOD });
	}

	public static final BootstrapModalManager removeLoading() {
		return new BootstrapModalManager(new CharSequence[] { BOOTSTRAP_MODAL_MANAGER_REMOVE_LOADING_METHOD });
	}

}

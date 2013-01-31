package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.modal.statement;

import org.odlabs.wiquery.core.javascript.ChainableStatement;
import org.odlabs.wiquery.core.options.LiteralOption;
import org.odlabs.wiquery.core.options.Options;

public class Bootstrap implements ChainableStatement {

	public static final String BOOTSTRAP_MODAL_CHAIN_LABEL = "modal";

	public static final CharSequence BOOTSTRAP_MODAL_SHOW_METHOD = new LiteralOption("show").getJavascriptOption();

	public static final CharSequence BOOTSTRAP_MODAL_HIDE_METHOD = new LiteralOption("hide").getJavascriptOption();

	private final CharSequence method;

	private Options[] options;

	private Bootstrap(CharSequence method, Options... options) {
		super();
		this.method = method;
		this.options = options;
	}

	@Override
	public String chainLabel() {
		return BOOTSTRAP_MODAL_CHAIN_LABEL;
	}

	@Override
	public CharSequence[] statementArgs() {
		CharSequence[] args = new CharSequence[(method != null ? 1 : 0) + (options != null ? options.length : 0)];
		int i = 0;
		if (method != null) {
			args[i] = method;
			i++;
		}
		for (Options optionsItem : options) {
			args[i] = optionsItem.getJavaScriptOptions();
			i++;
		}
		return args;
	}

	/**
	 * TODO LAL : ajouter les options ?
	 */
	public static final Bootstrap show() {
		return new Bootstrap(BOOTSTRAP_MODAL_SHOW_METHOD);
	}

	public static final Bootstrap hide() {
		return new Bootstrap(BOOTSTRAP_MODAL_HIDE_METHOD);
	}

}

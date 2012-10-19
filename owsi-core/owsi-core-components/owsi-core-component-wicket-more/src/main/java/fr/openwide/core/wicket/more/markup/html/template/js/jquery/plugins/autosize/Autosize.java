package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.autosize;

import org.odlabs.wiquery.core.javascript.ChainableStatement;

public class Autosize implements ChainableStatement {

	private static final CharSequence[] EMPTY_STATEMENT_ARGS = new CharSequence[0];

	@Override
	public String chainLabel() {
		return "autosize";
	}

	@Override
	public CharSequence[] statementArgs() {
		return EMPTY_STATEMENT_ARGS;
	}

}

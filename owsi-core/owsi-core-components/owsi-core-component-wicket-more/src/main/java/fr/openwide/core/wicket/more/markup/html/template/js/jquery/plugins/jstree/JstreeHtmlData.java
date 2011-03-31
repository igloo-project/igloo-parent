package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.jstree;

import org.odlabs.wiquery.core.javascript.ChainableStatement;

public class JstreeHtmlData implements ChainableStatement {

	@Override
	public String chainLabel() {
		return "jstree";
	}

	@Override
	public CharSequence[] statementArgs() {
		// ne pas charger le plugin theme, il gère mal l'import des resources
		// dans wicket.
		return new CharSequence[] { "{ plugins: ['html_data'] }" };
	}

}

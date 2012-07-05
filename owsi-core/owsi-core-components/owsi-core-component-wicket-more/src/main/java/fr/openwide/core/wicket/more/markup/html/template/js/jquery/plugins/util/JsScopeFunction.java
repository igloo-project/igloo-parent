package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.util;

import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.core.javascript.JsScopeContext;

public class JsScopeFunction extends JsScope {
	private static final long serialVersionUID = 1684041623069205814L;

	private String function;

	public JsScopeFunction(String function) {
		super();
		this.function = function;
	}

	@Override
	protected void execute(JsScopeContext scopeContext) {
		// nothing
	}

	@Override
	public CharSequence render() {
		return function;
	}

}

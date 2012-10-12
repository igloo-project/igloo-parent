package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.tipsy;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.odlabs.wiquery.core.javascript.JsStatement;

public class TipsyBehavior extends Behavior {

	private static final long serialVersionUID = 6319723112229959901L;

	private String selector;

	private Tipsy tipsy;

	public TipsyBehavior(String selector, Tipsy tipsy) {
		super();
		this.selector = selector;
		this.tipsy = tipsy;
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		response.render(JavaScriptHeaderItem.forReference(TipsyJavascriptResourceReference.get()));
		response.render(OnDomReadyHeaderItem.forScript(new JsStatement().$(null, selector).chain(tipsy).render()));
	}

}

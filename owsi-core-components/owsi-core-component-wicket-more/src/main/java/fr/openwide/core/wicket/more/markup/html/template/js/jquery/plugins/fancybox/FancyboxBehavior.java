package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.fancybox;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.odlabs.wiquery.core.javascript.JsStatement;

public class FancyboxBehavior extends Behavior {

	private static final long serialVersionUID = 7556130610172681270L;

	private DefaultTipsyFancybox fancybox;

	private String selector;

	public FancyboxBehavior(String selector, DefaultTipsyFancybox fancybox) {
		super();
		this.fancybox = fancybox;
		this.selector = selector;
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		response.render(JavaScriptHeaderItem.forReference(FancyboxJavaScriptResourceReference.get()));
		response.render(CssHeaderItem.forReference(FancyboxStyleSheetResourceReference.get()));
		
		response.render(OnDomReadyHeaderItem.forScript(new JsStatement().$(component, selector).chain(fancybox).render()));
	}

}

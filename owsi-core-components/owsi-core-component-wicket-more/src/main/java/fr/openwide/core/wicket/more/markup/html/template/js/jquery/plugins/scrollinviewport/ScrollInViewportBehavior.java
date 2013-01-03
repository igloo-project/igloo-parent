package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.scrollinviewport;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.odlabs.wiquery.core.javascript.JsStatement;

public class ScrollInViewportBehavior extends Behavior {

	private static final long serialVersionUID = -6068559743555922420L;
	
	private String selector;
	
	private ScrollInViewport scrollInViewport;
	
	public ScrollInViewportBehavior() {
		this(null, null);
	}
	
	public ScrollInViewportBehavior(String selector) {
		this(selector, null);
	}
	
	public ScrollInViewportBehavior(ScrollInViewport scrollInViewport) {
		this(null, scrollInViewport);
	}
	
	public ScrollInViewportBehavior(String selector, ScrollInViewport scrollInViewport) {
		super();
		this.selector = selector;
		this.scrollInViewport = scrollInViewport;
	}
	
	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		response.render(JavaScriptHeaderItem.forReference(ScrollInViewportResourceReference.get()));
		response.render(OnDomReadyHeaderItem.forScript(statement(component).render()));
	}
	
	protected JsStatement statement(Component component) {
		JsStatement statement = new JsStatement();
		if (selector != null) {
			statement.$(component, selector);
		} else {
			statement.$(component);
		}
		if (scrollInViewport != null) {
			statement.chain(scrollInViewport);
		}
		return statement;
	}

}

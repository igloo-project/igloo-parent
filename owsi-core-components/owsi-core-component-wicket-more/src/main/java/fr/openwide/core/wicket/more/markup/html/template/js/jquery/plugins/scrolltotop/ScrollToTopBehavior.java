package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.scrolltotop;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.util.lang.Args;
import org.odlabs.wiquery.core.javascript.JsStatement;

public class ScrollToTopBehavior extends Behavior {

	private static final long serialVersionUID = -4834541827215125178L;

	private ScrollToTop scrollToTop;
	
	public ScrollToTopBehavior() {
		this(new ScrollToTop());
	}
	
	public ScrollToTopBehavior(ScrollToTop scrollToTop) {
		Args.notNull(scrollToTop, "scrollToTop");
		this.scrollToTop = scrollToTop;
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		response.render(JavaScriptHeaderItem.forReference(ScrollToTopJavaScriptResourceReference.get()));
		response.render(OnDomReadyHeaderItem.forScript(new JsStatement().$(component).chain(scrollToTop).render()));
		super.renderHead(component, response);
	}

}

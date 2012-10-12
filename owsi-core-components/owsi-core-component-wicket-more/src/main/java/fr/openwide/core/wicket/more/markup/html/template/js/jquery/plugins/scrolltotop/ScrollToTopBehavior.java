package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.scrolltotop;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.odlabs.wiquery.core.javascript.JsStatement;

public class ScrollToTopBehavior extends Behavior {

	private static final long serialVersionUID = -4834541827215125178L;

	private static final String SCROLL_TO_TOP = "scrollToTop";

	public ScrollToTopBehavior() {
		super();
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		response.render(JavaScriptHeaderItem.forReference(ScrollToTopJavaScriptResourceReference.get()));
		response.render(OnDomReadyHeaderItem.forScript(new JsStatement().$(component).chain(SCROLL_TO_TOP).render()));
		super.renderHead(component, response);
	}

}

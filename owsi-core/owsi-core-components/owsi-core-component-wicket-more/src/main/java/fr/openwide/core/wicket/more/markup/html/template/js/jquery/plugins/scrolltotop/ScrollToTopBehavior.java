package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.scrolltotop;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.odlabs.wiquery.core.behavior.WiQueryAbstractBehavior;
import org.odlabs.wiquery.core.javascript.JsStatement;

public class ScrollToTopBehavior extends WiQueryAbstractBehavior {

	private static final long serialVersionUID = -4834541827215125178L;

	private static final String SCROLL_TO_TOP = "scrollToTop";

	public ScrollToTopBehavior() {
		super();
	}

	@Override
	public JsStatement statement() {
		return new JsStatement().$(getComponent()).chain(SCROLL_TO_TOP);
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		response.renderJavaScriptReference(ScrollToTopJavaScriptResourceReference.get());
		super.renderHead(component, response);
	}

}

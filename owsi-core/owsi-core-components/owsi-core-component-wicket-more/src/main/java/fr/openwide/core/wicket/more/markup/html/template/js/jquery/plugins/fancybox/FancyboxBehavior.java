package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.fancybox;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.odlabs.wiquery.core.behavior.WiQueryAbstractBehavior;
import org.odlabs.wiquery.core.javascript.JsStatement;

public class FancyboxBehavior extends WiQueryAbstractBehavior{
	private static final long serialVersionUID = 7556130610172681270L;
	
	private DefaultTipsyFancybox fancybox;
	
	private String selector;
	
	public FancyboxBehavior(String selector, DefaultTipsyFancybox fancybox) {
		super();
		this.fancybox = fancybox;
		this.selector = selector;
	}

	@Override
	public JsStatement statement() {
		return new JsStatement().$(getComponent(), selector).chain(fancybox);
	}
	
	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		response.renderJavaScriptReference(FancyboxJavaScriptResourceReference.get());
		response.renderCSSReference(FancyboxStyleSheetResourceReference.get());
	}

}

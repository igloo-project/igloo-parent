package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.fancybox;

import org.apache.wicket.Component;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.odlabs.wiquery.core.javascript.JsStatement;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.util.JQueryAbstractBehavior;

public class FancyboxBehavior extends JQueryAbstractBehavior {

	private static final long serialVersionUID = 7556130610172681270L;
	
	private DefaultTipsyFancybox fancybox;
	
	private String selector;
	
	public FancyboxBehavior(String selector, DefaultTipsyFancybox fancybox) {
		super();
		this.fancybox = fancybox;
		this.selector = selector;
	}

	public JsStatement statement() {
		return new JsStatement().$(getComponent(), selector).chain(fancybox);
	}
	
	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		response.render(JavaScriptHeaderItem.forReference(FancyboxJavaScriptResourceReference.get()));
		response.render(CssHeaderItem.forReference(FancyboxStyleSheetResourceReference.get()));
		
		response.render(OnDomReadyHeaderItem.forScript(statement().render()));
	}

}

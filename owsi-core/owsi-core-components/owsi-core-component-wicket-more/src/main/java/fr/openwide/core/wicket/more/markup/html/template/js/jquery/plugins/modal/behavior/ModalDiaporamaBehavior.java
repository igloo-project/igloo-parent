package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.modal.behavior;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.odlabs.wiquery.core.behavior.WiQueryAbstractBehavior;
import org.odlabs.wiquery.core.javascript.ChainableStatement;
import org.odlabs.wiquery.core.javascript.JsStatement;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.easing.EasingJavaScriptResourceReference;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.modal.ModalJavaScriptResourceReference;

public class ModalDiaporamaBehavior extends WiQueryAbstractBehavior{
	private static final long serialVersionUID = 7556130610172681270L;
	
	private final ChainableStatement fancybox;
	
	private final String selector;
	
	public ModalDiaporamaBehavior(String selector, ChainableStatement fancybox) {
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
		response.renderJavaScriptReference(EasingJavaScriptResourceReference.get());
		response.renderJavaScriptReference(ModalJavaScriptResourceReference.get());
	}

}

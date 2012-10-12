package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.modal.behavior;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.odlabs.wiquery.core.javascript.ChainableStatement;
import org.odlabs.wiquery.core.javascript.JsStatement;

import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.easing.EasingJavaScriptResourceReference;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.modal.ModalJavaScriptResourceReference;

public class ModalDiaporamaBehavior extends Behavior {

	private static final long serialVersionUID = 7556130610172681270L;

	private final ChainableStatement fancybox;

	private final String selector;

	public ModalDiaporamaBehavior(String selector, ChainableStatement fancybox) {
		super();
		this.fancybox = fancybox;
		this.selector = selector;
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		response.render(JavaScriptHeaderItem.forReference(EasingJavaScriptResourceReference.get()));
		response.render(JavaScriptHeaderItem.forReference(ModalJavaScriptResourceReference.get()));
		response.render(OnDomReadyHeaderItem.forScript(new JsStatement().$(component, selector).chain(fancybox).render()));
	}

}

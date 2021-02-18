package org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.tab;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.wicketstuff.wiquery.core.javascript.JsStatement;

public class BootstrapTabBehavior extends Behavior {

	private static final long serialVersionUID = 1645525017124363380L;

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		response.render(JavaScriptHeaderItem.forReference(BootstrapTabJavaScriptResourceReference.get()));
		response.render(JavaScriptHeaderItem.forReference(BootstrapTabMoreJavaScriptResourceReference.get()));
		response.render(OnDomReadyHeaderItem.forScript(new JsStatement().$(component).chain(new BootstrapTab()).render()));
	}

}
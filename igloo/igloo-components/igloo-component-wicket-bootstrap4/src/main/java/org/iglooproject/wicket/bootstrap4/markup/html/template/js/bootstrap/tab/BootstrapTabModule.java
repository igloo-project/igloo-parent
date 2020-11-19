package org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.tab;

import org.apache.wicket.Component;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.tab.IBootstrapTabModule;

@org.springframework.stereotype.Component
public class BootstrapTabModule implements IBootstrapTabModule {

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		response.render(JavaScriptHeaderItem.forReference(BootstrapTabJavaScriptResourceReference.get()));
		response.render(JavaScriptHeaderItem.forReference(BootstrapTabMoreJavaScriptResourceReference.get()));
	}

}

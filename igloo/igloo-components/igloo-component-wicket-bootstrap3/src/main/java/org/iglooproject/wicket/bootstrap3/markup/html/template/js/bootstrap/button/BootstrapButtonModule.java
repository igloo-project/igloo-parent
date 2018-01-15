package org.iglooproject.wicket.bootstrap3.markup.html.template.js.bootstrap.button;

import org.apache.wicket.Component;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.button.IBootstrapButtonModule;

@org.springframework.stereotype.Component
public class BootstrapButtonModule implements IBootstrapButtonModule {

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		response.render(JavaScriptHeaderItem.forReference(BootstrapButtonJavaScriptResourceReference.get()));
	}

}

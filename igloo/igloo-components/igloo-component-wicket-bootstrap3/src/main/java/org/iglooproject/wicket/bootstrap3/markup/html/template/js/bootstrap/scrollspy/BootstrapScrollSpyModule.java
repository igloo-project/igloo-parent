package org.iglooproject.wicket.bootstrap3.markup.html.template.js.bootstrap.scrollspy;

import org.apache.wicket.Component;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.scrollspy.IBootstrapScrollSpyModule;

@org.springframework.stereotype.Component
public class BootstrapScrollSpyModule implements IBootstrapScrollSpyModule {

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		response.render(JavaScriptHeaderItem.forReference(BootstrapScrollSpyJavaScriptResourceReference.get()));
	}

}

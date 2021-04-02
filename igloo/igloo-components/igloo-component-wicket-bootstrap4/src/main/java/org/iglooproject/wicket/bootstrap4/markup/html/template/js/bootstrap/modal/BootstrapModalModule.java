package org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.modal;

import org.apache.wicket.Component;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.modal.IBootstrapModalModule;

@org.springframework.stereotype.Component
public class BootstrapModalModule implements IBootstrapModalModule {

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		response.render(JavaScriptHeaderItem.forReference(BootstrapModalJavaScriptResourceReference.get()));
	}

}

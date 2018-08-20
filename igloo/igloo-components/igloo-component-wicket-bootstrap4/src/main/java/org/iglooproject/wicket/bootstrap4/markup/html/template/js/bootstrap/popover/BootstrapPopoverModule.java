package org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.popover;

import org.apache.wicket.Component;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.popover.IBootstrapPopoverModule;

@org.springframework.stereotype.Component
public class BootstrapPopoverModule implements IBootstrapPopoverModule {

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		response.render(JavaScriptHeaderItem.forReference(BootstrapPopoverJavaScriptResourceReference.get()));
	}

}

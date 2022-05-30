package org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.tooltip;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.iglooproject.wicket.api.util.Detachables;

public class AbstractBootstrapTooltipBehavior extends Behavior {

	private static final long serialVersionUID = 290297757411081052L;

	protected final IBootstrapTooltip bootstrapTooltip;

	public AbstractBootstrapTooltipBehavior(final IBootstrapTooltip bootstrapTooltip) {
		super();
		this.bootstrapTooltip = bootstrapTooltip;
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		response.render(JavaScriptHeaderItem.forReference(BootstrapTooltipJavaScriptResourceReference.get()));
	}

	@Override
	public void detach(Component component) {
		super.detach(component);
		Detachables.detach(bootstrapTooltip);
	}

}

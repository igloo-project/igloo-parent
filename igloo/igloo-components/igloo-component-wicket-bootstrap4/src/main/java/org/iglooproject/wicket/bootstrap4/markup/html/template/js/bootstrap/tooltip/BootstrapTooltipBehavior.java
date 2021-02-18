package org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.tooltip;

import org.apache.wicket.Component;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.wicketstuff.wiquery.core.javascript.JsStatement;

public class BootstrapTooltipBehavior extends AbstractBootstrapTooltipBehavior {

	private static final long serialVersionUID = 1645525017124363380L;

	public BootstrapTooltipBehavior(IBootstrapTooltip bootstrapTooltip) {
		super(bootstrapTooltip);
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		super.renderHead(component, response);
		response.render(OnDomReadyHeaderItem.forScript(new JsStatement().$(component).chain(bootstrapTooltip).render()));
	}

}

package org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.tooltip;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.iglooproject.wicket.api.Models;
import org.wicketstuff.wiquery.core.javascript.JsStatement;

public class BootstrapTooltipDocumentBehavior extends AbstractBootstrapTooltipBehavior {

	private static final long serialVersionUID = 1645525017124363380L;

	public BootstrapTooltipDocumentBehavior(IBootstrapTooltip bootstrapTooltip) {
		super(bootstrapTooltip);
		if (StringUtils.isBlank(Models.<CharSequence>getObject().apply(bootstrapTooltip.getSelectorModel()))) {
			throw new IllegalStateException("Option 'selector' is mandatory for " + BootstrapTooltipDocumentBehavior.class.getName());
		}
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		super.renderHead(component, response);
		response.render(OnDomReadyHeaderItem.forScript(new JsStatement().document().chain(bootstrapTooltip).render()));
	}

}

package org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.bootstrap4.tooltip;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.wicketstuff.wiquery.core.javascript.JsStatement;

public class BootstrapTooltipDocumentBehavior extends Behavior {

	private static final long serialVersionUID = 1645525017124363380L;

	private IBootstrapTooltip bootstrapTooltip;

	public BootstrapTooltipDocumentBehavior(IBootstrapTooltip bootstrapTooltip) {
		super();
		if (StringUtils.isBlank(bootstrapTooltip.getSelector())) {
			throw new IllegalStateException("L'option « selector » est obligatoire pour un "
					+ BootstrapTooltipDocumentBehavior.class.getName());
		}
		this.bootstrapTooltip = bootstrapTooltip;
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		response.render(JavaScriptHeaderItem.forReference(BootstrapTooltipJavascriptResourceReference.get()));
		response.render(OnDomReadyHeaderItem.forScript(new JsStatement().document().chain(bootstrapTooltip).render()));
	}

}

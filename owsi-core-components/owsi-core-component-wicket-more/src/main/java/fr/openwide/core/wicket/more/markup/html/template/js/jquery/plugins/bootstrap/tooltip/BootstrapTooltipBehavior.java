package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.tooltip;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.odlabs.wiquery.core.javascript.JsStatement;

public class BootstrapTooltipBehavior extends Behavior {

	private static final long serialVersionUID = 1645525017124363380L;

	private IBootstrapTooltip bootstrapTooltip;

	public BootstrapTooltipBehavior(IBootstrapTooltip bootstrapTooltip) {
		super();
		this.bootstrapTooltip = bootstrapTooltip;
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		response.render(JavaScriptHeaderItem.forReference(BootstrapTooltipJavascriptResourceReference.get()));
		response.render(OnDomReadyHeaderItem.forScript(new JsStatement().$(component).chain(bootstrapTooltip).render()));
	}

}

package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.tooltip;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.odlabs.wiquery.core.behavior.WiQueryAbstractBehavior;
import org.odlabs.wiquery.core.javascript.JsStatement;

public class BootstrapTooltipDocumentBehavior extends WiQueryAbstractBehavior {
	
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
	public JsStatement statement() {
		return new JsStatement().document().chain(bootstrapTooltip);
	}
	
	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		response.renderJavaScriptReference(BootstrapTooltipJavascriptResourceReference.get());
	}

}

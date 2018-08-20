package org.iglooproject.wicket.more.markup.html.template.js.bootstrap.tooltip;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.wiquery.core.javascript.JsStatement;

public class BootstrapTooltipDocumentBehavior extends Behavior {

	private static final long serialVersionUID = 1645525017124363380L;

	@SpringBean
	private List<IBootstrapTooltipModule> modules;

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
		modules.forEach(module -> module.renderHead(component, response));
		response.render(OnDomReadyHeaderItem.forScript(new JsStatement().document().chain(bootstrapTooltip).render()));
	}

}

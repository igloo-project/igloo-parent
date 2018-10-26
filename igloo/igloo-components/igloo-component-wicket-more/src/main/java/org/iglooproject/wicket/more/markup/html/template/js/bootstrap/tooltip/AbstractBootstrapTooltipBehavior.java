package org.iglooproject.wicket.more.markup.html.template.js.bootstrap.tooltip;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.wicket.more.util.model.Detachables;

public class AbstractBootstrapTooltipBehavior extends Behavior {

	private static final long serialVersionUID = 290297757411081052L;

	@SpringBean
	private List<IBootstrapTooltipModule> modules;

	protected final IBootstrapTooltip bootstrapTooltip;

	public AbstractBootstrapTooltipBehavior(final IBootstrapTooltip bootstrapTooltip) {
		super();
		this.bootstrapTooltip = bootstrapTooltip;
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		modules.forEach(module -> module.renderHead(component, response));
	}

	@Override
	public void detach(Component component) {
		super.detach(component);
		Detachables.detach(bootstrapTooltip);
	}

}

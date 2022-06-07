package org.iglooproject.bootstrap.api.tooltip;

import java.util.Objects;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.model.IModel;
import org.iglooproject.bootstrap.api.BootstrapRequestCycle;
import org.iglooproject.spring.util.StringUtils;
import org.iglooproject.wicket.model.Detachables;

public class BootstrapTooltipBehavior extends Behavior {

	private static final long serialVersionUID = 7614489253481226605L;

	private final IModel<? extends IBootstrapTooltipOptions> optionsModel;

	public BootstrapTooltipBehavior(IModel<? extends IBootstrapTooltipOptions> optionsModel) {
		super();
		this.optionsModel = Objects.requireNonNull(optionsModel);
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		super.renderHead(component, response);
		
		IBootstrapTooltipOptions options = optionsModel.getObject();
		
		if (!StringUtils.hasText(options.getSelector())) {
			throw new IllegalStateException("Option 'selector' is mandatory for " + BootstrapTooltipBehavior.class.getName());
		}
		
		BootstrapRequestCycle.getSettings().tooltipRenderHead(component, response, options);
	}

	@Override
	public void detach(Component component) {
		super.detach(component);
		Detachables.detach(optionsModel);
	}

}

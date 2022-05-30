package org.iglooproject.wicket.bootstrap5.markup.html.template.js.bootstrap.popover;

import java.util.Objects;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.iglooproject.wicket.api.util.Detachables;

public class BootstrapPopoverBehavior extends Behavior {

	private static final long serialVersionUID = 4466209676897772566L;

	private final IModel<? extends IBootstrapPopoverOptions> optionsModel;

	public BootstrapPopoverBehavior() {
		this(LoadableDetachableModel.of(BootstrapPopoverOptions::get));
	}

	public BootstrapPopoverBehavior(IModel<? extends IBootstrapPopoverOptions> optionsModel) {
		super();
		this.optionsModel = Objects.requireNonNull(optionsModel);
	}

	@Override
	public void onComponentTag(Component component, ComponentTag tag) {
		tag.put("data-bs-toggle", "popover");
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		super.renderHead(component, response);
		response.render(OnDomReadyHeaderItem.forScript("new bootstrap.Popover(document.getElementById('" + component.getMarkupId() + "'), " + optionsModel.getObject().getJavaScriptOptions() + ");"));
	}

	@Override
	public void detach(Component component) {
		super.detach(component);
		Detachables.detach(optionsModel);
	}

}

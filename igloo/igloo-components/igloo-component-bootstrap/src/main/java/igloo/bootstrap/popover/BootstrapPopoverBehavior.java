package igloo.bootstrap.popover;

import java.io.Serializable;
import java.util.Objects;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

import igloo.bootstrap.BootstrapRequestCycle;
import igloo.wicket.model.Detachables;

public class BootstrapPopoverBehavior extends Behavior {

	private static final long serialVersionUID = 4466209676897772566L;

	private final IModel<? extends IBootstrapPopoverOptions> optionsModel;

	public BootstrapPopoverBehavior() {
		this(LoadableDetachableModel.of(BootstrapPopoverOptions::get));
	}

	/**
	 * @deprecated You should manage your own <code>IModel&lt;? extends IBootstrapPopoverOptions&gt;</code> and pass
	 * it to the alternative constructor.
	 * 
	 * @param must be not-null and serializable
	 */
	@Deprecated
	public BootstrapPopoverBehavior(IBootstrapPopoverOptions options) {
		super();
		this.optionsModel = Model.of((Serializable&IBootstrapPopoverOptions) Objects.requireNonNull(options));
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
		BootstrapRequestCycle.getSettings().popoverRenderHead(component, response, optionsModel.getObject());
	}

	@Override
	public void detach(Component component) {
		super.detach(component);
		Detachables.detach(optionsModel);
	}

}

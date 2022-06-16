package igloo.bootstrap.popover;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;

import igloo.bootstrap.BootstrapRequestCycle;
import igloo.bootstrap.jsmodel.JsDetachablesVisitor;
import igloo.bootstrap.jsmodel.JsModelVisitor;

public class PopoverBehavior extends Behavior {

	private static final long serialVersionUID = 7918322316697244768L;

	private final IPopover popover;

	public PopoverBehavior(IPopover popover) {
		this.popover = popover;
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		super.renderHead(component, response);
		BootstrapRequestCycle.getSettings().popoverRenderHead(component, response, popover::render);
	}

	@Override
	public void bind(Component component) {
		super.bind(component);
		new JsModelVisitor().visitAndBind(component, popover);
	}

	@Override
	public void detach(Component component) {
		new JsDetachablesVisitor().visitAndDetach(popover);
		super.detach(component);
	}

}

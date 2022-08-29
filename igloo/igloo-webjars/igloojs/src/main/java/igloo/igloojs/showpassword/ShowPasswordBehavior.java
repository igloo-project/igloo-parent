package igloo.igloojs.showpassword;

import java.util.Objects;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;

public class ShowPasswordBehavior extends Behavior {

	private static final long serialVersionUID = -4723509811711123272L;

	private final Component target;

	public ShowPasswordBehavior(Component target) {
		this.target = Objects.requireNonNull(target);
	}

	@Override
	public void onComponentTag(Component component, ComponentTag tag) {
		super.onComponentTag(component, tag);
		
		tag.put("data-show-password-target", target.getMarkupId());
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		super.renderHead(component, response);
		response.render(JavaScriptHeaderItem.forReference(ShowPasswordJavaScriptResourceReference.get()));
		response.render(OnDomReadyHeaderItem.forScript(String.format("showPassword.init('%s')", component.getMarkupId())));
	}

}

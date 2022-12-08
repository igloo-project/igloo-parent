package igloo.bootstrap4.markup.html.template.js.sidebar;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;

public class SidebarBehavior extends Behavior {

	private static final long serialVersionUID = -2815649505603347770L;

	public SidebarBehavior() {
		super();
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		response.render(JavaScriptHeaderItem.forReference(SidebarJavaScriptResourceReference.get()));
	}

}

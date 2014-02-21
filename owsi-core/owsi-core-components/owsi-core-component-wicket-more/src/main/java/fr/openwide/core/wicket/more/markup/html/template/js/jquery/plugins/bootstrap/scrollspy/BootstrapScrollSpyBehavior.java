package fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.scrollspy;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.core.javascript.JsStatement;
import org.odlabs.wiquery.core.javascript.JsUtils;

public class BootstrapScrollSpyBehavior extends Behavior {

	private static final long serialVersionUID = -5665173800881237350L;

	private static final String BOOTSTRAP_SCROLLSPY = "scrollspy";

	private final BootstrapScrollSpyOptions options;

	public BootstrapScrollSpyBehavior(BootstrapScrollSpyOptions options) {
		super();
		this.options = options;
	}

	public JsStatement statement(Component component) {
		return new JsStatement().$(component).chain(BOOTSTRAP_SCROLLSPY, options.getJavaScriptOptions());
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		response.render(JavaScriptHeaderItem.forReference(BootstrapScrollSpyJavaScriptResourceReference.get()));
		response.render(OnDomReadyHeaderItem.forScript(statement(component).render()));
	}
	
	@Override
	public void onComponentTag(Component component, ComponentTag tag) {
		super.onComponentTag(component, tag);
		// Just a marker for the refresh statement below
		tag.getAttributes().put("data-spy", "scroll");
	}
	
	public static JsStatement refreshStatement() {
		return new JsStatement().$(null, "[data-spy=\"scroll\"]").each(
				JsScope.quickScope(
						new JsStatement().self().chain(BOOTSTRAP_SCROLLSPY, JsUtils.quotes("refresh"))
		));
	}

}

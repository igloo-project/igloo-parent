package org.iglooproject.wicket.more.markup.html.template.js.bootstrap.scrollspy;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.wicket.more.util.model.Detachables;
import org.wicketstuff.wiquery.core.javascript.JsScope;
import org.wicketstuff.wiquery.core.javascript.JsStatement;
import org.wicketstuff.wiquery.core.javascript.JsUtils;

public class BootstrapScrollSpyBehavior extends Behavior {

	private static final long serialVersionUID = -5665173800881237350L;

	@SpringBean
	private List<IBootstrapScrollSpyModule> modules;

	private final IBootstrapScrollSpy bootstrapScrollspy;

	public BootstrapScrollSpyBehavior(IBootstrapScrollSpy bootstrapScrollspy) {
		super();
		this.bootstrapScrollspy = bootstrapScrollspy;
	}

	public JsStatement statement(Component component) {
		return new JsStatement().$(component).chain(bootstrapScrollspy);
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		modules.forEach(module -> module.renderHead(component, response));
		response.render(OnDomReadyHeaderItem.forScript(statement(component).render()));
	}

	@Override
	public void detach(Component component) {
		super.detach(component);
		Detachables.detach(bootstrapScrollspy);
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
						new JsStatement().self().chain(IBootstrapScrollSpy.CHAIN_LABEL, JsUtils.quotes("refresh"))
		));
	}

}

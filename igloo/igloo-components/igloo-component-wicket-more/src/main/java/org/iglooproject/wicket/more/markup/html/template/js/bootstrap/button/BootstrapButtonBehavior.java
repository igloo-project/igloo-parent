package org.iglooproject.wicket.more.markup.html.template.js.bootstrap.button;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.wiquery.core.javascript.JsStatement;

public class BootstrapButtonBehavior extends Behavior {

	private static final long serialVersionUID = -5665173800881237350L;

	private static final String BOOTSTRAP_BUTTON = "button";

	@SpringBean
	private List<IBootstrapButtonModule> modules;

	public BootstrapButtonBehavior() {
		super();
	}

	public JsStatement statement(Component component) {
		return new JsStatement().$(component, ".btn").chain(BOOTSTRAP_BUTTON);
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		modules.forEach(module -> module.renderHead(component, response));
		response.render(OnDomReadyHeaderItem.forScript(statement(component).render()));
	}

}

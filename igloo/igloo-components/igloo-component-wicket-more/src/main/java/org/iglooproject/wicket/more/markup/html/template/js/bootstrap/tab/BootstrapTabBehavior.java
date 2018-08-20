package org.iglooproject.wicket.more.markup.html.template.js.bootstrap.tab;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.wiquery.core.javascript.JsStatement;

public class BootstrapTabBehavior extends Behavior {

	private static final long serialVersionUID = 1645525017124363380L;

	@SpringBean
	private List<IBootstrapTabModule> modules;

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		modules.forEach(module -> module.renderHead(component, response));
		response.render(OnDomReadyHeaderItem.forScript(new JsStatement().$(component).chain(new BootstrapTab()).render()));
	}
}
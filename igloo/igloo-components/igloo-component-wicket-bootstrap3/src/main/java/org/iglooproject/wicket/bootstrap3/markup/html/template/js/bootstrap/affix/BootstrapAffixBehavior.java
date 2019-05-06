package org.iglooproject.wicket.bootstrap3.markup.html.template.js.bootstrap.affix;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.wicketstuff.wiquery.core.javascript.JsStatement;
import org.wicketstuff.wiquery.core.javascript.JsUtils;

public class BootstrapAffixBehavior extends Behavior {

	private static final long serialVersionUID = -5665173800881237350L;

	private static final String BOOTSTRAP_AFFIX = "affix";

	private BootstrapAffixOptions options;

	public BootstrapAffixBehavior(BootstrapAffixOptions options) {
		super();
		this.options = options;
	}

	public JsStatement statement(Component component) {
		return new JsStatement().$(component).chain(BOOTSTRAP_AFFIX, options.getJavaScriptOptions());
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		response.render(JavaScriptHeaderItem.forReference(BootstrapAffixJavaScriptResourceReference.get()));
		response.render(OnDomReadyHeaderItem.forScript(statement(component).render()));
		response.render(OnDomReadyHeaderItem.forScript(new JsStatement().$(component).chain("affix", JsUtils.quotes("checkPosition")).render()));
	}

}

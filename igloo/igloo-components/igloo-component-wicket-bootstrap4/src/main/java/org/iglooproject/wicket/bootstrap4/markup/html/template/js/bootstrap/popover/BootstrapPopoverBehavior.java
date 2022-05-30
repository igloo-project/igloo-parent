package org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.popover;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.iglooproject.wicket.api.util.Detachables;
import org.wicketstuff.wiquery.core.javascript.JsScope;
import org.wicketstuff.wiquery.core.javascript.JsScopeContext;
import org.wicketstuff.wiquery.core.javascript.JsScopeEvent;
import org.wicketstuff.wiquery.core.javascript.JsStatement;
import org.wicketstuff.wiquery.core.javascript.JsUtils;
import org.wicketstuff.wiquery.core.javascript.helper.EventsHelper;

public class BootstrapPopoverBehavior extends Behavior {

	private static final long serialVersionUID = -4381194681563091269L;

	private static final String BOOTSTRAP_POPOVER = "popover";

	private IBootstrapPopoverOptions options;

	public BootstrapPopoverBehavior(IBootstrapPopoverOptions options) {
		super();
		this.options = options;
	}

	public JsStatement statement(Component component) {
		return new JsStatement().$(component)
			.chain(BOOTSTRAP_POPOVER, options.getJavaScriptOptions(component))
			.chain(
				"on",
				JsUtils.quotes("shown.bs.popover"),
				JsScope.quickScope(
					new JsStatement()
						.append("$($(this).data('bs.popover').tip)")
						.chain("find", JsUtils.quotes(".popover-dismiss"))
						.chain(EventsHelper.click(new JsScopeEvent() {
							private static final long serialVersionUID = 1L;
							@Override
							protected void execute(JsScopeContext scopeContext) {
								scopeContext.append("event.preventDefault();");
								scopeContext.append(new JsStatement().$(component).chain(BOOTSTRAP_POPOVER, JsUtils.quotes("hide")).render(true));
							}
						}))
						.render()
				)
					.render()
			);
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		response.render(JavaScriptHeaderItem.forReference(BootstrapPopoverJavaScriptResourceReference.get()));
		response.render(OnDomReadyHeaderItem.forScript(statement(component).render()));
	}

	@Override
	public void detach(Component component) {
		super.detach(component);
		Detachables.detach(options);
	}

}
